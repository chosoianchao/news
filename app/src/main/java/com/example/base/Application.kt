package com.example.base

import android.app.Application
import com.example.base.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class Application : Application() {
    companion object{
        var Instance: Application? = null
    }
    override fun onCreate() {
        super.onCreate()
        Instance = this
        startKoin {
            androidContext(this@Application)
            androidLogger()
            modules(appModule)
        }
    }
}