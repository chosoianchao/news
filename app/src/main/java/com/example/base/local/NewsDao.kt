package com.example.base.local

import androidx.room.*
import com.example.base.model.Item

@Dao
interface NewsDao {
    @Query("SELECT * FROM news")
    suspend fun getAll(): List<Item>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(news: Item)

    @Delete
    fun delete(item: Item)
}