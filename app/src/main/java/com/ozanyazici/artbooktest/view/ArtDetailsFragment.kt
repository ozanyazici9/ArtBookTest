package com.ozanyazici.artbooktest.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import com.ozanyazici.artbooktest.R
import com.ozanyazici.artbooktest.databinding.FragmentArtDetailsBinding
import com.ozanyazici.artbooktest.util.Resource
import com.ozanyazici.artbooktest.viewmodel.ArtViewModel
import javax.inject.Inject

class ArtDetailsFragment @Inject constructor(
    val glide: RequestManager
) : Fragment(R.layout.fragment_art_details) {

    private lateinit var viewModel: ArtViewModel
    private var fragmentBinding: FragmentArtDetailsBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(ArtViewModel::class.java)

        val binding = FragmentArtDetailsBinding.bind(view)
        fragmentBinding = binding

        val args = ArtDetailsFragmentArgs.fromBundle(requireArguments())
        val receivedUrlFromImageApiFragment = args.url
        receivedUrlFromImageApiFragment?.let {
            viewModel.setSelectedImage(receivedUrlFromImageApiFragment)
        }

        if (args.artName == "null" || args.artistName == "null" || args.year == "null") {
            binding.artText.text.clear()
            binding.artistText.text.clear()
            binding.yearText.text.clear()
        } else {
            binding.artText.setText(args.artName)
            binding.artistText.setText(args.artistName)
            binding.yearText.setText(args.year)
            glide.load(args.url).into(binding.artImageView)

            binding.saveButton.visibility = View.GONE
        }

        subscribeToObservers()

        binding.artImageView.setOnClickListener{
            findNavController().navigate(ArtDetailsFragmentDirections.actionArtDetailsFragmentToImageApiFragment())
        }

        //Geri tuşuna basıldığında ne olacağını burada yazıyoruz.
        val callback = object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                //Yığında geri gidiyor.
                findNavController().popBackStack()
            }
        }
        //requireActivity().onBackPressedDispatcher.addCallback(callback) çağrısı, callback değişkeninin
        //OnBackPressedCallback nesnesini geri tuşu işleyicisine ekler.
        //Bu sayede, belirtilen işlev, geri tuşuna basıldığında çağrılacaktır.
        requireActivity().onBackPressedDispatcher.addCallback(callback)

        binding.saveButton.setOnClickListener {
            viewModel.makeArt(binding.artText.text.toString() ,binding.artistText.text.toString(),binding.yearText.text.toString())
            val action = ArtDetailsFragmentDirections.actionArtDetailsFragmentToArtFragment()
            Navigation.findNavController(it).navigate(action)
        }
    }

    private fun subscribeToObservers() {
        viewModel.selectedImageUrl.observe(viewLifecycleOwner, Observer {url ->
            fragmentBinding?.let{
                glide.load(url).into(it.artImageView)
            }
        })

        viewModel.insertArtMessage.observe(viewLifecycleOwner, Observer {
            when(it.status) {
                Resource.Status.SUCCESS -> {
                    Toast.makeText(requireContext(),"Success",Toast.LENGTH_LONG).show()
                    viewModel.resetInsertArtMsg()
                }

                Resource.Status.ERROR -> {
                    Toast.makeText(requireContext(),it.message ?: "Error",Toast.LENGTH_LONG).show()
                }

                Resource.Status.LOADING -> {

                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentBinding = null
    }
}