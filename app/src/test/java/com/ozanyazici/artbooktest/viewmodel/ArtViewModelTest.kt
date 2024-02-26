package com.ozanyazici.artbooktest.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.ozanyazici.artbooktest.MainCoroutineRule
import com.ozanyazici.artbooktest.getOrAwaitValueTest
import com.ozanyazici.artbooktest.repo.FakeArtRepository
import com.ozanyazici.artbooktest.util.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

// Burada yapacağımız testleri mainthreadde çalışturmak isitiyoruz ama bu bir ınstrumental test değil
// bu yüzden emülatörde çalışmayacak dolayısıyla main thread diye bir şey yok.
// Bu sebeple MainCoroutineRule u kullanmak durumundayız.

@ExperimentalCoroutinesApi
class ArtViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: ArtViewModel

    @Before
    fun setup() {
        //Test Doubles -> Kopyasını test etme
        viewModel = ArtViewModel(FakeArtRepository())
    }

    //Test isimleri endüstride bu kadar açık yazılmalı. Ne yapacağımız ve karşılığında ne beklediğimizi isim olarak yazarız.
    //Bu testleri yapmadan önce uygulamamın önbelleğini silmek testin güveniliriliği açısından iyi olur.
    @Test
    fun `insert art without year retruns error`(){
        viewModel.makeArt("Mona Lisa","Da Vinci","")
        //liveData olan inserrArtMessage ı getorAwaitValueTest sınıfı sayesinde liveData olmaktan çıkardık.
        val value = viewModel.insertArtMessage.getOrAwaitValueTest()
        //kayıt yapılırken yıl girilmediğinde uygulama erorr veriyor mu onu test ediyoruz.
        assertThat(value.status).isEqualTo(Resource.Status.ERROR)
    }

    @Test
    fun `insert art without name retruns error`(){
        viewModel.makeArt("","Da Vinci","1900")
        val value = viewModel.insertArtMessage.getOrAwaitValueTest()
        assertThat(value.status).isEqualTo(Resource.Status.ERROR)
    }

    @Test
    fun `insert art without artistName retruns error`(){
        viewModel.makeArt("Mona Lisa","","1900")
        val value = viewModel.insertArtMessage.getOrAwaitValueTest()
        assertThat(value.status).isEqualTo(Resource.Status.ERROR)
    }
}