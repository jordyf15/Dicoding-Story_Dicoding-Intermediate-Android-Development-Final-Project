package com.jordyf15.storyapp.utils

import com.jordyf15.storyapp.data.remote.response.ErrorResponse
import com.jordyf15.storyapp.data.remote.response.LoginResponse
import com.jordyf15.storyapp.data.remote.response.LoginResult
import com.jordyf15.storyapp.data.remote.response.RegisterResponse

object DataDummy {
    fun generateDummyRegisterResponse(): RegisterResponse {
        return RegisterResponse(false, "Register successful")
    }

    fun generateDummyLoginResponse(): LoginResponse {
        val loginResult = LoginResult("userId-1", "jojo", "token-123")
        return LoginResponse(false, "Login successful", loginResult)
    }

    fun generateDummyErrorResponse(): ErrorResponse {
        return ErrorResponse(true, "An error has occured")
    }
}