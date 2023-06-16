package com.example.base.repository.splash

import kotlinx.coroutines.delay

class SplashRepositoryImpl : SplashRepository {
    override suspend fun getRepoSelectedStatus(): Boolean {
        delay(2000)
        return false
    }
}