package com.ozanyazici.artbooktest.repo

import androidx.lifecycle.LiveData
import com.ozanyazici.artbooktest.model.ImageResponse
import com.ozanyazici.artbooktest.roomdb.Art
import com.ozanyazici.artbooktest.util.Resource

interface ArtRepositoryInterface {

    suspend fun insertArt(art: Art)

    suspend fun deleteArt(art: Art)

    //Livedata zaten asenkron o yüzden suspend yapmadık.
    fun getArt(): LiveData<List<Art>>

    suspend fun searchImage(imageString: String): Resource<ImageResponse>

}