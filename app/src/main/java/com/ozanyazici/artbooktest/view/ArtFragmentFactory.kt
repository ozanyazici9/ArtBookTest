package com.ozanyazici.artbooktest.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.bumptech.glide.RequestManager
import com.ozanyazici.artbooktest.adapter.ArtRecyclerAdapter
import com.ozanyazici.artbooktest.adapter.ImageRecyclerAdapter
import javax.inject.Inject

//bir Fragment'ın nasıl oluşturulacağına ilişkin mantığı bu fabrika sınıfında toplamış oluruz.
//Bu, uygulamanın başka yerlerinde Fragment oluştururken kodun daha temiz ve yönetilebilir olmasını sağlar.

class ArtFragmentFactory @Inject constructor(
    private val imageRecyclerAdapter: ImageRecyclerAdapter,
    private val artRecyclerAdapter: ArtRecyclerAdapter,
    private val glide: RequestManager
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {

        return when(className) {
            ImageApiFragment::class.java.name -> ImageApiFragment(imageRecyclerAdapter)
            ArtDetailsFragment::class.java.name -> ArtDetailsFragment(glide)
            ArtFragment::class.java.name -> ArtFragment(artRecyclerAdapter)
            else -> super.instantiate(classLoader, className)
        }
    }
}