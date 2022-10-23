package com.jordyf15.storyapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.jordyf15.storyapp.data.UserRepository
import com.jordyf15.storyapp.data.remote.response.ErrorResponse
import com.jordyf15.storyapp.data.remote.response.LoginResponse

class LoginViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    val isLoading: LiveData<Boolean> = userRepository.loginViewIsLoading
    val loginResponse: LiveData<LoginResponse> = userRepository.loginResponse
    val errorResponse: LiveData<ErrorResponse> = userRepository.loginViewErrorResponse

    fun login(email: String, password: String) = userRepository.login(email, password)

    fun isLoggedIn() = userRepository.isLoggedIn()
}