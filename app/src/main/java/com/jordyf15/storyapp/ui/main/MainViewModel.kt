package com.jordyf15.storyapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.jordyf15.storyapp.data.StoryRepository
import com.jordyf15.storyapp.data.UserRepository
import com.jordyf15.storyapp.data.remote.response.ErrorResponse
import com.jordyf15.storyapp.data.remote.response.Story

class MainViewModel(
    private val storyRepository: StoryRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    val isLoading: LiveData<Boolean> = storyRepository.mainViewIsLoading
    val errorResponse: LiveData<ErrorResponse> = storyRepository.mainViewErrorResponse
    val stories: LiveData<List<Story>> = storyRepository.listStories
    val isLoggedIn: LiveData<Boolean> = userRepository.isLoggedIn

    fun getAllStories() = storyRepository.getAllStories()

    fun logout() = userRepository.logout()
}