package com.ozanyazici.artbooktest.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ozanyazici.artbooktest.model.ImageResponse
import com.ozanyazici.artbooktest.repo.ArtRepositoryInterface
import com.ozanyazici.artbooktest.roomdb.Art
import com.ozanyazici.artbooktest.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

//Projedeki asıl amaç test olduğu için normalde yapılması gerektiği gibi her fragment için bir viewmodel oluşturmadık,
//onun yerine hepsini bu viewmodelda hallediceğiz.

@HiltViewModel
class ArtViewModel @Inject constructor(
    private val repository: ArtRepositoryInterface
): ViewModel() {

    //Art Fragment

    val artList = repository.getArt()

    //Image API Fragment

    //LiveData yı, private ve MutableLiveData olarak tanımlıyoruz çünkü sadece bu sınıftan değiştirilebilir olmasını istiyoruz.
    //Diğer sınıflardan sadece gözlemlenebilmesi için imageList diye bir değişken daha oluşturup get metoduna images i veriyoruz.
    //Burada MutableLiveData dan bir nesne oluşturup images in içine atıyoruz.
    private val images = MutableLiveData<Resource<ImageResponse>>()
    val imageList: LiveData<Resource<ImageResponse>>
        get() = images

    private val selectedImage = MutableLiveData<String>()
    val selectedImageUrl: LiveData<String>
        get() = selectedImage

    //Art Details Fragment

    private var insertArtMsg = MutableLiveData<Resource<Art>>()
    val insertArtMessage: LiveData<Resource<Art>>
        get() = insertArtMsg

    fun resetInsertArtMsg(){
        insertArtMsg = MutableLiveData<Resource<Art>>()
    }

    fun setSelectedImage(url: String) {
        selectedImage.postValue(url)
    }

    fun deleteArt(art: Art) = viewModelScope.launch {
        repository.deleteArt(art)
    }

    private fun insertArt(art: Art) = viewModelScope.launch {
        repository.insertArt(art)
    }

    //insertArtMsg ı burada durumumuzu kontrol ederken kullandık.
    fun makeArt(name: String, artistName: String, year: String) {
        if (name.isEmpty() || artistName.isEmpty() || year.isEmpty()) {
            insertArtMsg.postValue(Resource.error("Enter name, artist, year", null))
            return
        }

        val yearInt = try {
            year.toInt()
        } catch (e: Exception) {
            insertArtMsg.postValue(Resource.error("Year should be number", null))
            return
        }

        val art = Art(name,artistName,yearInt,selectedImage.value ?: "")
        insertArt(art)
        setSelectedImage("")
        insertArtMsg.postValue(Resource.success(art))
    }

    //Arama yaptığımız zaman aradığımız kelimelere göre resim gelecek.
    fun searchForImage(searchString: String) {

        //Burada searchString boşsa bir şey yapma diyoruz.
        if (searchString.isEmpty()){
            return
        }

        images.value = Resource.loading(null)
        viewModelScope.launch {
            val response = repository.searchImage(searchString)
            images.value = response
        }
    }
}