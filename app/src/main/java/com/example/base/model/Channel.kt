package com.example.base.model


data class Channel(

    var title: String = "",

    var description: String = "",

    var image: Image = Image(),

    var pubDate: String = "",

    var generator: String = "",

    var link: String = "",
    var item: ArrayList<Item> = arrayListOf()
)