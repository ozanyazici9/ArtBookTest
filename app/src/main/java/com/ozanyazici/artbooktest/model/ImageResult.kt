package com.ozanyazici.artbooktest.model

import com.google.gson.annotations.SerializedName

data class ImageResult(
    //değişkenlerin adı gelen verinin adıyla aynı olabilir o yüzden serializedname yazmadım.
    val comments: Int,
    val downloads: Int,
    val favorites: Int,
    val id: Int,
    val imageHeight: Int,
    val imageSize: Int,
    val imageWidth: Int,
    val largeImageURL: String,
    val likes: Int,
    val pageURL: String,
    val previewHeight: Int,
    val previewURL: String,
    val previewWidth: Int,
    val tags: String,
    val type: String,
    val user: String,
    @SerializedName("user_id")
    val userId: Int,
    val userImageURL: String,
    val views: Int,
    val webformatHeight: Int,


)
