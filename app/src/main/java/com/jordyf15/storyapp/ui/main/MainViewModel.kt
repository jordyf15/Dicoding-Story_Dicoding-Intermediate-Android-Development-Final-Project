package com.jordyf15.storyapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.jordyf15.storyapp.data.StoryRepository
import com.jordyf15.storyapp.data.UserRepository
import com.jordyf15.storyapp.data.remote.response.Story

class MainViewModel(
    private val storyRepository: StoryRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    val stories: LiveData<PagingData<Story>> by lazy {
        storyRepository.getStories().cachedIn(viewModelScope)
    }
    val isLoggedIn: LiveData<Boolean> = userRepository.isLoggedIn

    fun logout() = userRepository.logout()
}