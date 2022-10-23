package com.jordyf15.storyapp.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.jordyf15.storyapp.data.UserRepository
import com.jordyf15.storyapp.data.remote.response.ErrorResponse
import com.jordyf15.storyapp.data.remote.response.RegisterResponse

class RegisterViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    val registerResponse: LiveData<RegisterResponse> = userRepository.registerResponse
    val errorResponse: LiveData<ErrorResponse> = userRepository.registerViewErrorResponse
    val isLoading: LiveData<Boolean> = userRepository.registerViewIsLoading

    fun register(name: String, email: String, password: String) =
        userRepository.register(name, email, password)
}