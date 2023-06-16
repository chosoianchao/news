package com.example.base.repository.splash

interface SplashRepository {
   suspend fun getRepoSelectedStatus(): Boolean
}