package com.ozanyazici.artbooktest.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.ozanyazici.artbooktest.databinding.ImageRowBinding
import com.ozanyazici.artbooktest.roomdb.Art
import com.ozanyazici.artbooktest.view.ImageApiFragmentDirections
import javax.inject.Inject

class ImageRecyclerAdapter @Inject constructor(val glide: RequestManager): RecyclerView.Adapter<ImageRecyclerAdapter.ImageViewHolder>() {

    class ImageViewHolder(val binding: ImageRowBinding): RecyclerView.ViewHolder(binding.root)

    private val onItemClickListener: ((String) -> Unit) ? = null

    private val diffUtil = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    private val recyclerListDiffer = AsyncListDiffer(this,diffUtil)

    var images: List<String>
        get() = recyclerListDiffer.currentList
        set(value) = recyclerListDiffer.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
       val binding = ImageRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
       return ImageViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageView = holder.binding.singleArtImageView
        val url = images[position]
        holder.binding.apply {
            glide.load(url).into(imageView)
        }

        imageView.setOnClickListener{
            val action = ImageApiFragmentDirections.actionImageApiFragmentToArtDetailsFragment(url)
            Navigation.findNavController(it).navigate(action)
        }
    }
}