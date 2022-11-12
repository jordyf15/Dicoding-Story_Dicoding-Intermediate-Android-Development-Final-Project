package com.jordyf15.storyapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.jordyf15.storyapp.data.UserRepository

class LoginViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    val isLoggedIn: LiveData<Boolean> = userRepository.isLoggedIn

    fun login(email: String, password: String) = userRepository.login(email, password)
}