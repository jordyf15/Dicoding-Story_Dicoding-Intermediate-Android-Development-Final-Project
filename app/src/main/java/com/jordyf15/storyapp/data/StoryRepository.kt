package com.jordyf15.storyapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.*
import com.google.gson.Gson
import com.jordyf15.storyapp.data.local.preference.UserPreference
import com.jordyf15.storyapp.data.local.room.StoryDatabase
import com.jordyf15.storyapp.data.remote.response.*
import com.jordyf15.storyapp.data.remote.retrofit.ApiService
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class StoryRepository constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference,
    private val storyDatabase: StoryDatabase
) {
    fun addStory(
        storyImg: File,
        description: String,
        latitude: Float?,
        longitude: Float?
    ): LiveData<Result<AddStoryResponse>> = liveData {
        emit(Result.Loading)

        val desc = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = storyImg.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            storyImg.name,
            requestImageFile
        )
        val accessToken = "Bearer ${userPreference.getToken()}"
        if (latitude != null && longitude != null) {
            val lat = latitude.toString().toRequestBody("text/plain".toMediaType())
            val lon = longitude.toString().toRequestBody("text/plain".toMediaType())
            try {
                val response =
                    apiService.addNewStoryWithLocation(accessToken, desc, lat, lon, imageMultipart)
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
        } else {
            try {
                val response =
                    apiService.addNewStoryWithoutLocation(accessToken, desc, imageMultipart)
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

    }

    fun getAllStoryWithLocations(): LiveData<Result<GetAllStoryResponse>> = liveData {
        emit(Result.Loading)
        val accessToken = "Bearer ${userPreference.getToken()}"
        try {
            val response = apiService.getStoryWithLocations(accessToken, 1)
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

    @OptIn(ExperimentalPagingApi::class)
    fun getStories(): LiveData<PagingData<Story>> {
        val accessToken = "Bearer ${userPreference.getToken()}"
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService, accessToken),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }

    companion object {
        private const val TAG = "StoryRepository"

        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference,
            storyDatabase: StoryDatabase
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService, userPreference, storyDatabase)
            }.also { instance = it }
    }
}