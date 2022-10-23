package com.jordyf15.storyapp.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.jordyf15.storyapp.data.local.UserPreference
import com.jordyf15.storyapp.data.remote.response.ErrorResponse
import com.jordyf15.storyapp.data.remote.response.LoginResponse
import com.jordyf15.storyapp.data.remote.response.RegisterResponse
import com.jordyf15.storyapp.data.remote.retrofit.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {
    val registerResponse = MutableLiveData<RegisterResponse>()
    val registerViewErrorResponse = MutableLiveData<ErrorResponse>()
    val registerViewIsLoading = MutableLiveData<Boolean>()
    val loginResponse = MutableLiveData<LoginResponse>()
    val loginViewErrorResponse = MutableLiveData<ErrorResponse>()
    val loginViewIsLoading = MutableLiveData<Boolean>()

    fun isLoggedIn(): Boolean = userPreference.isLogin()

    fun logout() = userPreference.clearSession()



    fun login(email: String, password: String) {
        loginViewIsLoading.value = true
        val client = apiService.login(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                loginViewIsLoading.value = false
                if (response.isSuccessful) {
                    loginResponse.value = response.body()
                    response.body()?.loginResult?.let { userPreference.saveSession(it.token) }
                } else {
                    loginViewErrorResponse.value = Gson().fromJson(
                        response.errorBody()?.charStream(),
                        ErrorResponse::class.java
                    )
                    response.errorBody()?.let {
                        Log.e(TAG, "onFailure: ${it.string()}")
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                loginViewIsLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
                loginViewErrorResponse.value = ErrorResponse(true, t.message.toString())
            }
        })
    }

    fun register(name: String, email: String, password: String) {
        registerViewIsLoading.value = true
        val client = apiService.register(name, email, password)
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                res: Response<RegisterResponse>
            ) {
                registerViewIsLoading.value = false
                if (res.isSuccessful) {
                    registerResponse.value = res.body()
                } else {
                    registerViewErrorResponse.value =
                        Gson().fromJson(res.errorBody()?.charStream(), ErrorResponse::class.java)
                    res.errorBody()?.let { Log.e(TAG, it.string()) }
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                registerViewIsLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
                registerViewErrorResponse.value = ErrorResponse(true, t.message.toString())
            }
        })
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