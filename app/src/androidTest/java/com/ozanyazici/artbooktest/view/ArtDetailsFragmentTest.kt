package com.ozanyazici.artbooktest.view

import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import com.ozanyazici.artbooktest.R
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.MediumTest
import com.google.common.truth.Truth.assertThat
import com.ozanyazici.artbooktest.getOrAwaitValue
import com.ozanyazici.artbooktest.launchFragmentInHiltContainer
import com.ozanyazici.artbooktest.repo.FakeArtRepositoryTest
import com.ozanyazici.artbooktest.roomdb.Art
import com.ozanyazici.artbooktest.viewmodel.ArtViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import javax.inject.Inject

// Uygulama içerisinde kullanılan animasyonlar, özellikle de UI ile ilgili testler
// yaparken farklı sonuçlar ortaya çıkmasına sebebiyet verebilir.
// O yüzden RecyclerView gibi yapıları test ederken animasyonların telefonunuzda ya da emulatörünüzde kapalı olması doğru olacaktır.
// Developer options -> Drawing -> window, transition ve animator duration scale off olmalı.

@HiltAndroidTest
@MediumTest
@ExperimentalCoroutinesApi
class ArtDetailsFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var fragmentFactory: ArtFragmentFactory

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun testNavigationFromDetailsToImageAPI() {

        val navController = Mockito.mock(NavController::class.java)

        // Söz konusu fragmentın argümanları varsa fragmenta bunları vermek gereklidir.
        // Benim argümanlarım nullable olduğu için bir değer atamama gerek kalmadı.
        launchFragmentInHiltContainer<ArtDetailsFragment>(
            factory = fragmentFactory,
            fragmentArgs = Bundle().apply {
                // Burada gerekli argümanları ekleyin
            }
        ){
            Navigation.setViewNavController(requireView(),navController)
        }

        Espresso.onView(ViewMatchers.withId(R.id.artImageView)).perform(ViewActions.click())

        Mockito.verify(navController).navigate(
            ArtDetailsFragmentDirections.actionArtDetailsFragmentToImageApiFragment()
        )
    }

    @Test
    fun onBackPressed(){
        val navController = Mockito.mock(NavController::class.java)

        launchFragmentInHiltContainer<ArtDetailsFragment>(
            factory = fragmentFactory,
            fragmentArgs = Bundle().apply {
                // Burada gerekli argümanları ekleyin
            }
        ){
            Navigation.setViewNavController(requireView(),navController)
        }

        pressBack()
        Mockito.verify(navController).popBackStack()
    }

    @Test
    fun testSave() {
        // Kayıt işlemini test edeceğimiz için viewmodel oluşturduk.
        val testViewModel = ArtViewModel(FakeArtRepositoryTest())

        // Ayrıca saveButtona basıldığımda bir navigate işlemide olduğu için onu test etmemiz lazım.
        val navController = Mockito.mock(NavController::class.java)

        launchFragmentInHiltContainer<ArtDetailsFragment>(
            factory = fragmentFactory,
            fragmentArgs = Bundle().apply {
            }
        ){
            viewModel = testViewModel
            Navigation.setViewNavController(requireView(),navController)
        }
        // Edittextleri doldurduk
        Espresso.onView(ViewMatchers.withId(R.id.artText)).perform(ViewActions.replaceText("Mona Lisa"))
        Espresso.onView(ViewMatchers.withId(R.id.artistText)).perform(ViewActions.replaceText("Da Vinci"))
        Espresso.onView(ViewMatchers.withId(R.id.yearText)).perform(ViewActions.replaceText("1500"))
        // Butona tıklayıp kaydettik.
        Espresso.onView(ViewMatchers.withId(R.id.saveButton)).perform(click())

        Mockito.verify(navController).navigate(
            ArtDetailsFragmentDirections.actionArtDetailsFragmentToArtFragment()
        )

        // Bizim verdiğimiz değeri içeren bir art nesnesi varmı gözlenen veride kontrol ediyoruz.
        assertThat(testViewModel.artList.getOrAwaitValue()).contains(
            Art("Mona Lisa", "Da Vinci", 1500, "")
        )
    }

    @Test
    fun whenNavigatefromImageAPItoDetails() {

        val testViewModel = ArtViewModel(FakeArtRepositoryTest())

        val selectedImageUrl = "ozanyazici.com"

        var receivedUrlFromImageApiFragment: String = ""

        launchFragmentInHiltContainer<ArtDetailsFragment>(
            factory = fragmentFactory,
            fragmentArgs = Bundle().apply {
                putString("url", selectedImageUrl)
            }
        ){
            viewModel = testViewModel

            val args = ArtDetailsFragmentArgs.fromBundle(requireArguments())

            args.url?.let {
                receivedUrlFromImageApiFragment = it
                viewModel.setSelectedImage(receivedUrlFromImageApiFragment)
            }
        }

        assertThat(testViewModel.selectedImageUrl.getOrAwaitValue()).isEqualTo(selectedImageUrl)
    }
}