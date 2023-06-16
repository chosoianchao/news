package com.example.base.ui.translate

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.base.base.BaseViewModel
import com.example.base.model.Channel
import com.example.base.model.Item
import com.example.base.network.Response
import com.example.base.repository.translate.NewsRepositoryImpl
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import java.io.InputStream


class NewsViewModel(private val translateRepository: NewsRepositoryImpl) :
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
            val result = translateRepository.getNews()
            responseLiveData.postValue(Response.Success(result))
        }
    }

    fun convertStreamToString(stream: InputStream) {
        viewModelScope.launch(Dispatchers.IO) {
            result.postValue(translateRepository.convertStreamToString(stream))
        }
    }

    fun parseXmlData(xmlData: String) {
        viewModelScope.launch(Dispatchers.IO) {
            data.postValue(translateRepository.parseXmlData(xmlData))
        }
    }
}