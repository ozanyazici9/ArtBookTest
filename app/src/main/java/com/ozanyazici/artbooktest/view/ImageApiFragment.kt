package com.ozanyazici.artbooktest.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.ozanyazici.artbooktest.R
import com.ozanyazici.artbooktest.adapter.ImageRecyclerAdapter
import com.ozanyazici.artbooktest.databinding.FragmentImageApiBinding
import com.ozanyazici.artbooktest.util.Resource
import com.ozanyazici.artbooktest.viewmodel.ArtViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class ImageApiFragment @Inject constructor(private val imageRecyclerAdapter: ImageRecyclerAdapter): Fragment(R.layout.fragment_image_api) {

    private lateinit var viewModel: ArtViewModel
    private var fragmentBinding: FragmentImageApiBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(ArtViewModel::class.java)

        val binding = FragmentImageApiBinding.bind(view)
        fragmentBinding = binding

        //searchText e birşey yazıldıkça resimlerde arama yapılsın istiyorum ama bunu delaylerle yapıcam.
        //Çünkü klavyeye çok hızlı basılırsa sürekli api den veri gelicek bu uygulamayı kititleyebilir.

        var job: Job? = null

        binding.searchText.addTextChangedListener {
            job?.cancel() //a yazdım diyelim 1 saniye beklenirken ar oldu yazı job iptal edilir ve ar için yeni bir job oluşturulur.
            job = lifecycleScope.launch {
                delay(1000)
                it?.let {
                    if (it.toString().isNotEmpty()) {
                        viewModel.searchForImage(it.toString())
                    }
                }
            }
        }

        binding.imageRecyclerView.adapter = imageRecyclerAdapter
        //SpanCount kaç tane resmin yanyana gösterileceğini belirtir.
        binding.imageRecyclerView.layoutManager = GridLayoutManager(requireContext(),3)

        subscribeToObservers()
    }

    private fun subscribeToObservers() {
        viewModel.imageList.observe(viewLifecycleOwner, Observer {
            when(it.status) {
                Resource.Status.SUCCESS -> {
                    //map() metodu bir koleksiyonun her elemanına belirli bir dönüşüm işlemi uygulamanıza olanak tanır.
                    //Ve sonuç olarak yeni bir koleksiyon oluşturur.
                    //Sadece previewURL lazım olduğu için onu alıyorum.
                    val urls = it.data?.hits?.map { imageResult ->
                        imageResult.previewURL
                    }
                    imageRecyclerAdapter.images = urls ?: listOf()
                    fragmentBinding?.progressBar?.visibility = View.GONE
                }

                Resource.Status.ERROR -> {
                    Toast.makeText(requireContext(),it.message ?: "Error",Toast.LENGTH_LONG).show()
                    fragmentBinding?.progressBar?.visibility = View.GONE
                }

                Resource.Status.LOADING -> {
                    fragmentBinding?.progressBar?.visibility = View.VISIBLE
                }
            }
        })
    }
}