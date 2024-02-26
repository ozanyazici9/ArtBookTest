package com.ozanyazici.artbooktest

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

// Bu sınıf, coroutine'leri test etmek için kullanılan bir kuralı uygular ve testlerin coroutine'lerin doğru bir şekilde çalışmasını sağlar.v
@ExperimentalCoroutinesApi
class MainCoroutineRule(private val dispatcher: TestDispatcher = StandardTestDispatcher()) :
    TestWatcher() {

        // TestWatcher, JUnit kütüphanesinde bir testin hayat döngüsündeki belirli olayları dinlemek için kullanılır.

        // starting() metodu, Dispatchers.setMain(dispatcher) çağrısı ile ana coroutine dispatcher'ını belirtilen dispatcher ile
        // değiştirir. Bu, testlerin ana coroutine context'ini belirli bir dispatcher üzerinde çalışacak şekilde ayarlar.
    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(dispatcher)
    }

    // finished() metodu ise, Dispatchers.resetMain() çağrısı ile ana coroutine dispatcher'ını eski durumuna geri getirir.
    override fun finished(description: Description?) {
        super.finished(description)
        Dispatchers.resetMain()
    }
}