package com.ozanyazici.artbooktest.repo

import androidx.lifecycle.LiveData
import com.ozanyazici.artbooktest.api.RetrofitAPI
import com.ozanyazici.artbooktest.model.ImageResponse
import com.ozanyazici.artbooktest.roomdb.Art
import com.ozanyazici.artbooktest.roomdb.ArtDao
import com.ozanyazici.artbooktest.util.Resource
import javax.inject.Inject
//Eğer hilt kullanmasaydım database i dao u retrofit i kullanacağım her yerde oluşturmam gerekecekti.
class ArtRepository @Inject constructor(
    private val artDao: ArtDao,
    private val retrofitAPI: RetrofitAPI
) : ArtRepositoryInterface {
    override suspend fun insertArt(art: Art) {
        artDao.insertArt(art)
    }

    override suspend fun deleteArt(art: Art) {
        artDao.deleteArt(art)
    }

    override fun getArt(): LiveData<List<Art>> {
        return artDao.observeArts()
    }

    override suspend fun searchImage(imageString: String): Resource<ImageResponse> {
        return try {

            val response = retrofitAPI.imageSearch(imageString)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("Error", null)
            } else {
                Resource.error("Error", null)
            }

        } catch (e: Exception) {
            Resource.error("No data", null)
        }
    }
}

    //Test yaparken threading ve network işlemleri kullanılmaz çünkü asıl görülmek istenen app in çalışıp çalışmadığıdır.
    //Bunun için repo paketimizi ve util içindeki Resource sınıfını oluşturduk.
    //ArtrepositoryInterface i oluşturmamızın sebebi test yapıcağımız zaman bir fake repo daha ouşturacağımız için kolaylık olsun diye. Normalde gerek olmazdı.
    //MVVM mimarisinde, repository paketi genellikle veri erişim katmanını temsil eder.
    //Bu katman, veri kaynaklarına (örneğin: yerel veritabanı, ağ API'leri, dosya sistemleri vb.)
    //erişmek ve uygulama katmanının veri işleme mantığını ayırmak için kullanılır.