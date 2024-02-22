package com.ozanyazici.artbooktest.adapter

import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import javax.inject.Inject
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.ozanyazici.artbooktest.databinding.ArtRowBinding
import com.ozanyazici.artbooktest.roomdb.Art
import com.ozanyazici.artbooktest.view.ArtFragmentDirections

class ArtRecyclerAdapter @Inject constructor(
    val glide : RequestManager
) : RecyclerView.Adapter<ArtRecyclerAdapter.ArtViewHolder>() {

    class ArtViewHolder(val binding: ArtRowBinding): RecyclerView.ViewHolder(binding.root)

    //DiffUtil: Liste içeriğinde yapılan değişiklikleri algılayarak, sadece değişen verilerin güncellenmesini sağlar ve gereksiz yenilemeleri önler.
    //AsnycListDiffer DiffUtil'u kullanarak farklı bir thread üzerinde liste içeriğinin değişikliklerini hesaplar ve bu değişiklikleri ana thread üzerinde
    //günceller. Bu sayede, liste içeriğindeki değişiklikler arka planda işlenir ve ana thread'in bloke olmasını önler.

    //Anonim sınıflar genellikle listener (dinleyici) implementasyonları, callback'ler veya belirli bir yerde
    //tek seferlik kullanılacak küçük işlemler için tercih edilir. Bu, kodunuzu daha kısa ve okunabilir hale getirebilir.
    //Ancak, daha karmaşık veya daha geniş bir kullanım alanı için isimlendirilmiş sınıflar daha uygundur.
    //object kelimesi, bu anonim sınıfın bir nesne olduğunu belirtir

    private val diffUtil = object : DiffUtil.ItemCallback<Art>() {
        override fun areItemsTheSame(oldItem: Art, newItem: Art): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Art, newItem: Art): Boolean {
            return oldItem == newItem
        }
    }

    private val recyclerListDiffer = AsyncListDiffer(this,diffUtil)

    var arts: List<Art>
        get() = recyclerListDiffer.currentList
        set(value) = recyclerListDiffer.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtViewHolder {
        val binding = ArtRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ArtViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return arts.size
    }

    override fun onBindViewHolder(holder: ArtViewHolder, position: Int) {
        val imageView = holder.binding.artRowImageView
        val nameText = holder.binding.artRowNameText
        val artistNameText = holder.binding.artRowArtistNameText
        val yearText = holder.binding.artRowYearText
        val art = arts[position]
        holder.itemView.apply {
            nameText.text = "Name: ${art.name}"
            artistNameText.text = "Artist Name: ${art.artistName}"
            yearText.text = "Year: ${art.year}"
            glide.load(art.imageUrl).into(imageView)

            holder.itemView.setOnClickListener {
                val action = ArtFragmentDirections.actionArtFragmentToArtDetailsFragment(art.imageUrl, art.name, art.artistName, art.year.toString())
                Navigation.findNavController(it).navigate(action)
            }
        }
    }
}