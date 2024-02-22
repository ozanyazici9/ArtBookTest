package com.ozanyazici.artbooktest.roomdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "arts")
data class Art(
    //@ColumnInfo kullanılmazsa, Room veritabanındaki sütun adları varsayılan olarak değişken adlarını kullanacaktır
    var name: String,
    var artistName: String,
    var year: Int,
    var imageUrl: String,
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
)