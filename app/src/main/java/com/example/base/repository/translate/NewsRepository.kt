package com.example.base.repository.translate

import com.example.base.model.Item
import okhttp3.ResponseBody
import java.io.InputStream

interface NewsRepository {
    suspend fun getNews(): ResponseBody
    suspend fun convertStreamToString(inputStream: InputStream): String?
    suspend fun parseXmlData(xmlData: String): ArrayList<Item>
}