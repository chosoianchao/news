package com.example.base.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.base.model.Item

@Database(entities = [Item::class], version = 1, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {
    abstract fun newsDao(): NewsDao
}