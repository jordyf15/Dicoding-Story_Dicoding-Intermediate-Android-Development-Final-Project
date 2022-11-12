package com.jordyf15.storyapp.ui.register

import androidx.lifecycle.ViewModel
import com.jordyf15.storyapp.data.UserRepository

class RegisterViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    fun register(name: String, email: String, password: String) =
        userRepository.register(name, email, password)
}