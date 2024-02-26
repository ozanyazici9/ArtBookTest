package com.ozanyazici.artbooktest.roomdb

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.ozanyazici.artbooktest.getOrAwaitValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

// @SmallTest -> Unit testler, @MediumTest -> Integration testler, @LargeTest -> UI testler
// Room u test etmek için bize context lazım o yüzden bu testleri ınstrumented test olarak yani android test paketinin içinde yapmamız lazım
@SmallTest
@ExperimentalCoroutinesApi
@HiltAndroidTest
class ArtDaoTest {

    // InstantTaskExecutorRule, Android'de Room, LiveData gibi yapılarla birlikte kullanıldığında,
    // testlerin senkronize çalışmasını sağlar. Normalde, Android'de arka planda gerçekleşen işlemler nedeniyle
    // testler asenkron olarak çalışır. Bu, testlerin doğru sonuçlar vermesini zorlaştırabilir.
    // Ancak, InstantTaskExecutorRule kullanılarak, LiveData'nın setValue() veya postValue() çağrıları hemen gerçekleştirilir
    // ve testlerin bekleme yapmasına gerek kalmaz.

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    // Bu kural, her test çalıştırılmadan önce Hilt bileşenlerinin hazır olduğunu ve
    // bağımlılıklarınızı enjekte etmeye hazır olduğunu sağlar.

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    @Named("testDatabase")
    lateinit var database: ArtDatabase

    private lateinit var dao: ArtDao

    @Before
    fun setup(){

        // InMemoryDatabaseBuilder, testleriniz için ram içinde geçici bir veritabanı oluşturur.
        // Bu veritabanı, testlerin çalışması bittiğinde otomatik olarak yok edilir,
        // böylece testlerinizin durumu birbirine karışmaz ve her test kendi izole edilmiş ortamında çalışır.

        // Hilt kullandığımız için buna gerek kalmadı.
        /*
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),ArtDatabase::class.java
        ).allowMainThreadQueries().build()

         */

        // Hilt tarafından yönetilen bileşenleri test edilen sınıfa enjekte etmiş olursunuz.
        hiltRule.inject()

        dao = database.artDao()
    }

    @After
    fun teardown(){
        database.close()
    }

    // Paralel threading yapılmasın diye runBlocking kullanıyorum. Coroutine kullanmamın sebebi kullanacağım metodların suspend olması
    // Normalde bu testlerin adının açıklayıcı ve uzun olması lazım ama burada biz bizeyiz gerek yok :D
    @Test
    fun insertArtTesting() = runBlocking {

        val exampleArt = Art("Mona Lisa","Da Vinci",1700,"test.com",1)
        dao.insertArt(exampleArt)

        // db deki veriyi gözlemliyorum ve normal data ya dönüştürüp list e atıyorum.
        // Sonra kaydettiğim veri bu listenin içinde var mı diye kontrol ediyorum.
        val list = dao.observeArts().getOrAwaitValue()
        assertThat(list).contains(exampleArt)

    }

    @Test
    fun deleteArtTesting() = runBlocking {

        val exampleArt = Art("Mona Lisa","Da Vinci",1700,"test.com",1)
        dao.insertArt(exampleArt)

        dao.deleteArt(exampleArt)
        val list = dao.observeArts().getOrAwaitValue()

        assertThat(list).doesNotContain(exampleArt)
    }

}