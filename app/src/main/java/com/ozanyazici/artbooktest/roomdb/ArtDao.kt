package com.ozanyazici.artbooktest.roomdb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ArtDao {

    //Conflict bir çakışma olursa mesela ıd ler çakışırsa ne yapılması gerektiğini burada belirtiyoruz.Üstüne yaz dedik.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArt(art: Art)

    @Delete
    suspend fun deleteArt(art: Art)

    //LiveData zaten asenkron çalıştığı için suspend yazmaya gerek yok ama yazılabilirde.
    @Query("SELECT * FROM arts")
    fun observeArts(): LiveData<List<Art>>


}