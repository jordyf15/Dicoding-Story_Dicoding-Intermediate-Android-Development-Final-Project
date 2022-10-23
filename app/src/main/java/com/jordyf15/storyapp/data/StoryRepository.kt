package com.jordyf15.storyapp.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.jordyf15.storyapp.data.local.UserPreference
import com.jordyf15.storyapp.data.remote.response.*
import com.jordyf15.storyapp.data.remote.retrofit.ApiService
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class StoryRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {
    val listStories = MutableLiveData<List<Story>>()
    val mainViewIsLoading = MutableLiveData<Boolean>()
    val mainViewErrorResponse = MutableLiveData<ErrorResponse>()
    val addViewErrorResponse = MutableLiveData<ErrorResponse>()
    val addViewIsLoading = MutableLiveData<Boolean>()
    val finishAddStory = MutableLiveData(false)

    fun addStory(storyImg: File, description: String) {
        addViewIsLoading.value = true
        val desc = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = storyImg.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            storyImg.name,
            requestImageFile
        )
        val accessToken = "Bearer ${userPreference.getToken()}"
        val client = apiService.addNewStory(accessToken, desc, imageMultipart)
        client.enqueue(object : Callback<AddStoryResponse> {
            override fun onResponse(
                call: Call<AddStoryResponse>,
                response: Response<AddStoryResponse>
            ) {
                addViewIsLoading.value = false
                if (response.isSuccessful) {
                    finishAddStory.value = true
                } else {
                    addViewErrorResponse.value = Gson().fromJson(
                        response.errorBody()?.charStream(),
                        ErrorResponse::class.java
                    )
                    response.errorBody()?.let { Log.e(TAG, "onFailure: ${it.string()}") }
                }
            }

            override fun onFailure(call: Call<AddStoryResponse>, t: Throwable) {
                addViewIsLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
                addViewErrorResponse.value = ErrorResponse(true, t.message.toString())
            }
        })
    }

    fun resetAddStory() {
        finishAddStory.value = false
    }

    fun getAllStories() {
        mainViewIsLoading.value = true
        val accessToken = "Bearer ${userPreference.getToken()}"
        val client = apiService.getAllStories(accessToken)
        client.enqueue(object : Callback<GetAllStoryResponse> {
            override fun onResponse(
                call: Call<GetAllStoryResponse>,
                response: Response<GetAllStoryResponse>
            ) {
                mainViewIsLoading.value = false
                if (response.isSuccessful) {
                    listStories.value = response.body()?.listStory
                } else {
                    mainViewErrorResponse.value = Gson().fromJson(
                        response.errorBody()?.charStream(),
                        ErrorResponse::class.java
                    )
                    response.errorBody()?.let {
                        Log.e(TAG, "onFailure: ${it.string()}")
                    }
                }
            }

            override fun onFailure(call: Call<GetAllStoryResponse>, t: Throwable) {
                mainViewIsLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
                mainViewErrorResponse.value = ErrorResponse(true, t.message.toString())
            }
        })
    }

    companion object {
        private const val TAG = "StoryRepository"

        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService, userPreference)
            }.also { instance = it }
    }
}