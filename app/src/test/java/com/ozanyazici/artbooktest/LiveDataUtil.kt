package com.ozanyazici.artbooktest

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

// CountDownLatch: threadler arasında koordinasyon sağlamak için kullanılabildiği gibi,
// belirli sayıda işlemin tamamlanmasını beklemek için kullanılabilir.
// Özetle, CountDownLatch birden fazla thread arasında senkronizasyonu sağlamak için kullanılır.

/* Copyright 2019 Google LLC.
   SPDX-License-Identifier: Apache-2.0 */

// extension fuction özelliği kullanılıyor.
fun <T> LiveData<T>.getOrAwaitValueTest(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS
): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(value: T) {
            data = value
            latch.countDown()
            this@getOrAwaitValueTest.removeObserver(this)
        }

    }

    this.observeForever(observer)

    // Don't wait indefinitely if the LiveData is not set.
    if (!latch.await(time, timeUnit)) {
        throw TimeoutException("LiveData value was never set.")
    }

    // @Suppress("UNCHECKED_CAST") ifadesi, Kotlin derleyicisinin "UNCHECKED_CAST" uyarısını bastırır.
    // Bu uyarı, kodun tür dönüşümlerinde tür güvenliğini ihlal edebilecek potansiyel riskleri işaret eder.
    // Ancak, bazı durumlarda, geliştirici kesin olarak tür dönüşümünün güvenli olduğunu bildiğinde,
    // bu uyarıyı bastırmak isteyebilir.

    @Suppress("UNCHECKED_CAST")
    return data as T
}
// Eğer belirtilen zaman aşımı süresi içinde (time parametresi olarak belirtilen süre içinde) LiveData'ya bir değer atanmazsa,
// TimeoutException hatası fırlatılır. Bu, LiveData'nın belirli bir süre içinde değer almadığını
// ve test senaryosunun beklenen davranışa uygun olmadığını gösterir.
// Bu şekilde, testler zaman aşımı süresini geçen durumları ele alabilir ve bu tür hataları raporlayabilir.