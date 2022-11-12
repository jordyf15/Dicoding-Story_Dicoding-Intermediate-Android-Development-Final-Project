package com.jordyf15.storyapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.jordyf15.storyapp.data.local.preference.UserPreference
import com.jordyf15.storyapp.data.remote.response.ErrorResponse
import com.jordyf15.storyapp.data.remote.response.LoginResponse
import com.jordyf15.storyapp.data.remote.response.RegisterResponse
import com.jordyf15.storyapp.data.remote.retrofit.ApiService
import retrofit2.HttpException

class UserRepository constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {
    val isLoggedIn = MutableLiveData(userPreference.isLogin())

    fun logout() {
        isLoggedIn.value = false
        userPreference.clearSession()
    }

    fun login(email: String, password: String): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.login(email, password)
            isLoggedIn.value = true
            userPreference.saveSession(response.loginResult.token)
            emit(Result.Success(response))
        } catch (t: Throwable) {
            when (t) {
                is HttpException -> {
                    try {
                        val errResponse = Gson().fromJson(
                            t.response()?.errorBody()?.charStream(),
                            ErrorResponse::class.java
                        )
                        Log.e(TAG, "onFailure: ${errResponse.message}")
                        emit(Result.Error(errResponse.message))
                    } catch (e: Exception) {
                        Log.e(TAG, "onFailure: ${e.message.toString()}")
                        emit(Result.Error(e.message.toString()))
                    }
                }
                else -> {
                    Log.e(TAG, "onFailure: ${t.message.toString()}")
                    emit(Result.Error(t.message.toString()))
                }
            }
        }
    }

    fun register(
        name: String,
        email: String,
        password: String
    ): LiveData<Result<RegisterResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.register(name, email, password)
            emit(Result.Success(response))
        } catch (t: Throwable) {
            when (t) {
                is HttpException -> {
                    try {
                        val errResponse = Gson().fromJson(
                            t.response()?.errorBody()?.charStream(),
                            ErrorResponse::class.java
                        )
                        Log.e(TAG, "onFailure: ${errResponse.message}")
                        emit(Result.Error(errResponse.message))
                    } catch (e: Exception) {
                        Log.e(TAG, "onFailure: ${e.message.toString()}")
                        emit(Result.Error(e.message.toString()))
                    }
                }
                else -> {
                    Log.e(TAG, "onFailure: ${t.message.toString()}")
                    emit(Result.Error(t.message.toString()))
                }
            }
        }
    }

    companion object {
        private const val TAG = "UserRepository"

        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, userPreference)
            }.also { instance = it }
    }
}