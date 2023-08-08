package com.example.base.di

import androidx.room.Room
import com.example.base.local.AppDataBase
import com.example.base.network.HeaderInterceptor
import com.example.base.network.NetworkService
import com.example.base.repository.splash.SplashRepository
import com.example.base.repository.splash.SplashRepositoryImpl
import com.example.base.repository.translate.NewsRepository
import com.example.base.repository.translate.NewsRepositoryImpl
import com.example.base.ui.news.NewsViewModel
import com.example.base.ui.splash.SplashViewModel
import com.example.base.ui.webview.WebViewViewModel
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val appModule = module {

    factory { HeaderInterceptor() }
    factory { provideOkHttpClient(get()) }
    factory { provideNetworkApi(get()) }
    single { provideRetrofit(get()) }
    single { Room.databaseBuilder(get(), AppDataBase::class.java, "news_db").build() }

    singleOf(::SplashRepositoryImpl) { bind<SplashRepository>() }
    viewModelOf(::SplashViewModel)
    singleOf(::NewsRepositoryImpl) { bind<NewsRepository>() }
    viewModelOf(::NewsViewModel)
    viewModel { WebViewViewModel() }
}

fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder().baseUrl("https://vnexpress.net/rss/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

fun provideOkHttpClient(authInterceptor: HeaderInterceptor): OkHttpClient {
    return OkHttpClient().newBuilder().apply {
        addInterceptor(authInterceptor)
        connectTimeout(60, TimeUnit.SECONDS)
        readTimeout(60, TimeUnit.SECONDS)
        writeTimeout(60, TimeUnit.SECONDS)
    }.build()
}

fun provideNetworkApi(retrofit: Retrofit): NetworkService =
    retrofit.create(NetworkService::class.java)
