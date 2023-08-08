package com.example.base.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news")
data class Item constructor(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var title: String = "",
    var description: String = "",
    var link: String = "",
    var pubDate: String = "",
    var guid: String = "",
    var slash: String = "",
    var like: Boolean = false
)