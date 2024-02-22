package com.ozanyazici.artbooktest.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.ozanyazici.artbooktest.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var fragmentFactory: ArtFragmentFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //supportFragmentManager üzerinden fragmentFactory özelliğine bu nesne atanır ve bu sayede uygulama,
        //bu FragmentFactory'nin yönergelerine uygun olarak Fragment'ları oluşturur.
        supportFragmentManager.fragmentFactory = fragmentFactory
        setContentView(R.layout.activity_main)
        //Details fragmentta bilgileri girerken klavye edittextlerin görünürlüğünü engellemesindiye.
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }
}