package com.jordyf15.storyapp.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.jordyf15.storyapp.data.UserRepository

class StoryDetailViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    val isLoggedIn: LiveData<Boolean> = userRepository.isLoggedIn
    fun logout() = userRepository.logout()
}