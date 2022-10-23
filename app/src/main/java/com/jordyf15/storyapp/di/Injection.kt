package com.jordyf15.storyapp.di

import android.content.Context
import com.jordyf15.storyapp.data.StoryRepository
import com.jordyf15.storyapp.data.UserRepository
import com.jordyf15.storyapp.data.local.UserPreference
import com.jordyf15.storyapp.data.remote.retrofit.ApiConfig

object Injection {
    fun provideStoryRepository(context: Context): StoryRepository {
        val apiService = ApiConfig.getApiService()
        val userPreference = UserPreference.getInstance(context)
        return StoryRepository.getInstance(apiService, userPreference)
    }

    fun provideUserRepository(context: Context): UserRepository {
        val apiService = ApiConfig.getApiService()
        val userPreference = UserPreference.getInstance(context)
        return UserRepository.getInstance(apiService, userPreference)
    }
}