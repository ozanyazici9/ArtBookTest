package com.ozanyazici.artbooktest.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ozanyazici.artbooktest.R
import com.ozanyazici.artbooktest.adapter.ArtRecyclerAdapter
import com.ozanyazici.artbooktest.databinding.FragmentArtsBinding
import com.ozanyazici.artbooktest.viewmodel.ArtViewModel
import javax.inject.Inject

class ArtFragment @Inject constructor(val artRecyclerAdapter: ArtRecyclerAdapter) : Fragment(R.layout.fragment_arts) {

    private var fragmentBinding: FragmentArtsBinding? = null
    private lateinit var viewModel: ArtViewModel

    //Resmi sağa sola kaydırdığında silinmesi işlemini. Normalde tek bir yön seçilir ama not olsun diye biz ikisinide yazıyoruz.
    //onMove fonksiyonunun genellikle kullanıldığı senaryolar, kullanıcıların öğeleri sürükleyip farklı konumlara taşıyabileceği
    //uygulamalardır. Bu durumlarda, öğelerin sürükleme işlemine izin verip vermemeyi kontrol edebiliriz.
    //Eğer kullanıcı öğeleri sürükleyerek yeniden konumlandırabiliyorsa, bu fonksiyon genellikle true döndürülür.
    //onMove kullanılacağı zaman dragdirs değerine görünümün hangi yönlere tutup taşınabileceğini swipedirs de olduğu gibi vermeliyiz.
    //Burada bunu kullanmadığımız için 0 veriyoruz. 0 değeri onmove işlevini etkisiz kılar.
    private val swipeCallBack = object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
           val layoutPosition = viewHolder.layoutPosition
            val selectedArt = artRecyclerAdapter.arts[layoutPosition]
            viewModel.deleteArt(selectedArt)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(ArtViewModel::class.java)

        val binding = FragmentArtsBinding.bind(view)
        fragmentBinding = binding

        subscribeToObserves()

        binding.recyclerViewArt.adapter = artRecyclerAdapter
        binding.recyclerViewArt.layoutManager = LinearLayoutManager(requireContext())
        //Yukarıda oluşturduğum swipeCallback ı ItemTouchHelper a veriyorum ve hangi recyclerview a eklenmesi gerektiğini söylüyorum.
        ItemTouchHelper(swipeCallBack).attachToRecyclerView(binding.recyclerViewArt)

        binding.fab.setOnClickListener {
            findNavController().navigate(ArtFragmentDirections.actionArtFragmentToArtDetailsFragment())
        }

    }

    private fun subscribeToObserves() {
        viewModel.artList.observe(viewLifecycleOwner, Observer {
            artRecyclerAdapter.arts = it
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentBinding = null
    }

}