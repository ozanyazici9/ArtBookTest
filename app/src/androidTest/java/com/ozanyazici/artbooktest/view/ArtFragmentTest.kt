package com.ozanyazici.artbooktest.view

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.MediumTest
import com.ozanyazici.artbooktest.R
import com.ozanyazici.artbooktest.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import javax.inject.Inject

// @MediumTest Integration testler, bir uygulamanın farklı parçaları arasındaki etkileşimleri değerlendirmek için kullanılır
// ve genellikle uygulamanın gerçek kullanım senaryolarını temsil eder.
// Örneğin, bir kullanıcının belirli bir ekranı etkileşime geçirdiğinde beklenen davranışın gerçekleşip gerçekleşmediğini kontrol edebilirler.

@MediumTest
@HiltAndroidTest
class ArtFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var fragmentFactory: ArtFragmentFactory

    @Before
    fun setup (){
        hiltRule.inject()
    }

    // launchFragmentInHiltContainer
    // Fragmentları hilt ile birlikte böyle test ediyoruz. GOOGle tarafından yazılan HiltExtension dosyasındaki kodlarıda eklememiz gerekli
    // Bu sayede launchFragmentInHiltContainer metodunu kullanabiliriz. BU metod  belirtilen fragment'i başlatır ve Hilt konteynerinde işler.
    // Sahte bir activitede fragmentı çalıştırmış gibi oluruz yani.

    // Mock
    // Android geliştirme bağlamında "mock", genellikle "sahte nesne" veya "taklit nesne" anlamına gelir.
    // Sınıf Arayüz vb. nin testler için basitleştirilmiş hallerini kullanmak gerekebilir. Bu basitleştirilmiş versiyonlara "mock" denir.
    // Mock nesneler, test sırasında beklenen davranışları taklit etmek için programlanmıştır.
    // Gerçek veritabanına, ağa veya dış bağımlılıklara erişmeden, uygulama içindeki işlevselliği test etmek için kullanılırlar.
    // Bu, testlerin daha hızlı çalışmasını sağlar ve dış bağımlılıkların test sonuçları üzerindeki etkisini azaltır.


    // Espresso
    // Espresso, Android uygulamalarının UI (Kullanıcı Arayüzü) testlerini otomatize etmek için kullanılan bir test çerçevesidir.

    // 1 -> navControllerın mock unu çıkardık. 2 -> HiltContainer içerisinde fragmnetımızı başlattık.
    // 3 -> navController ı mock ettiğimiz navcontroller a eşitledik. 4 -> Espresso ya fab ı bulup ona tıkla dedik.
    // 5 -> Mockitoya mock unu yaptığımız navcontrollerda bir navigate olacak onu doğrula dedik.
    @Test
    fun testNavigationFromArtToDetails() {

        val navController = Mockito.mock(NavController::class.java)

        launchFragmentInHiltContainer<ArtFragment>(
            factory = fragmentFactory
        ) {
            Navigation.setViewNavController(requireView(),navController)
        }

        // Burada fab isimli butona tıkla diyoruz.
        Espresso.onView(ViewMatchers.withId(R.id.fab)).perform(ViewActions.click())

        Mockito.verify(navController).navigate(
            ArtFragmentDirections.actionArtFragmentToArtDetailsFragment()
        )

    }

}