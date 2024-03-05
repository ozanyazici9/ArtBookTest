package com.ozanyazici.artbooktest.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ozanyazici.artbooktest.model.ImageResponse
import com.ozanyazici.artbooktest.roomdb.Art
import com.ozanyazici.artbooktest.util.Resource

//Fake terimini kullandık çünkü endüstride böyle kullanılıyor.
//RepositoryInterface i oluşturmamın sebebi bu fake repositoryide oluşturacak olmamdı.
//Test yaparken asenkron işlemleri kullanmadığımız için kendimiz bir liste oluşturup onun üzerinden testlerimizi yapacağız.

class FakeArtRepositoryTest: ArtRepositoryInterface {

    private val arts = mutableListOf<Art>()
    private val artsLiveData = MutableLiveData<List<Art>>(arts)
    override suspend fun insertArt(art: Art) {
       arts.add(art)
        refreshData()
    }

    override suspend fun deleteArt(art: Art) {
        arts.remove(art)
        refreshData()
    }

    override fun getArt(): LiveData<List<Art>> {
        return artsLiveData
    }

    //buradada tamamen sahte bir retrun yapıyoruz.
    override suspend fun searchImage(imageString: String): Resource<ImageResponse> {
        return Resource.success(ImageResponse(listOf(),0,0))
    }

    private fun refreshData(){
        artsLiveData.postValue(arts)
    }
}