package com.ozanyazici.artbooktest.model

data class ImageResponse(
    //Gelen veride hits bir liste olrak geldiği için o listedeki parametreleri ayrı bir model olarak tanımladık.
    //API den veriyi ImageResponse olarak döndürüyoruz.
    //Burada da hits in içindeki veriler ImageResult modelinin değişkenlerine eşitlenip öyle tutuluyor.
    val hits: List<ImageResult>,
    val total: Int,
    val totalHits: Int
)
