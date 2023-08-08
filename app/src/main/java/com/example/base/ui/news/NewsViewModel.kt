package com.example.base.ui.news

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.base.base.BaseViewModel
import com.example.base.model.Item
import com.example.base.network.Response
import com.example.base.repository.translate.NewsRepositoryImpl
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import java.io.InputStream
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class NewsViewModel(private val newsRepository: NewsRepositoryImpl) :
    BaseViewModel() {
    val responseLiveData = MutableLiveData<Response<ResponseBody>>()
    val result = MutableLiveData<String>()
    val data = MutableLiveData<ArrayList<Item>>()
    fun getNews() {
        val handler = CoroutineExceptionHandler { _, throwable ->
            responseLiveData.postValue(Response.Error(throwable))
        }
        responseLiveData.postValue(Response.Loading(true))
        viewModelScope.launch(Dispatchers.IO + handler) {
            val result = newsRepository.getNews()
            responseLiveData.postValue(Response.Success(result))
        }
    }

    fun convertStreamToString(stream: InputStream) {
        viewModelScope.launch(Dispatchers.IO) {
            result.postValue(newsRepository.convertStreamToString(stream))
        }
    }

    fun parseXmlData(xmlData: String) {
        viewModelScope.launch(Dispatchers.IO) {
            data.postValue(newsRepository.parseXmlData(xmlData))
        }
    }

    fun insertNews(item: Item) {
        viewModelScope.launch(Dispatchers.IO) {
            newsRepository.insertNews(item)
        }
    }

    fun deleteNews(item: Item) {
        viewModelScope.launch(Dispatchers.IO) {
            newsRepository.deleteNews(item)
        }
    }

    fun sortItem1Hour() {
        viewModelScope.launch(Dispatchers.IO) {
            val currentTime = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"))
            val filteredList = data.value?.filter { item ->
                val itemDate = LocalDateTime.parse(
                    item.pubDate,
                    DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH)
                )
                val itemDateWithZone = itemDate.atZone(ZoneId.of("GMT+7"))
                val difference = Duration.between(itemDateWithZone, currentTime)
                difference.toHours() <= 1
            }
            data.postValue(filteredList as ArrayList<Item>?)
        }
    }

    fun sortItemDays(day: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentTime = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"))
            val filteredList = data.value?.filter { item ->
                val itemDate = LocalDateTime.parse(
                    item.pubDate,
                    DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH)
                )
                val itemDateWithZone = itemDate.atZone(ZoneId.of("GMT+7"))
                val difference = Duration.between(itemDateWithZone, currentTime)
                difference.toDays() <= day
            }
            data.postValue(filteredList as ArrayList<Item>?)
        }
    }
}