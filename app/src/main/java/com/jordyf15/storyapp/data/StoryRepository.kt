package com.jordyf15.storyapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class StoryRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference,
    private val storyDatabase: StoryDatabase
) {
    val mainViewIsLoading = MutableLiveData<Boolean>()
    val mainViewErrorResponse = MutableLiveData<ErrorResponse>()
    val addViewErrorResponse = MutableLiveData<ErrorResponse>()
    val addViewIsLoading = MutableLiveData<Boolean>()
    val mapViewIsLoading = MutableLiveData<Boolean>()
    val mapViewErrorResponse = MutableLiveData<ErrorResponse>()
    val listStoryWithLocations = MutableLiveData<List<Story>>()
    val finishAddStory = MutableLiveData(false)

    fun addStory(storyImg: File, description: String, latitude: Float?, longitude: Float?) {
        addViewIsLoading.value = true
        val client: Call<AddStoryResponse>
        val desc = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = storyImg.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            storyImg.name,
            requestImageFile
        )
        val accessToken = "Bearer ${userPreference.getToken()}"

        client = if (latitude != null && longitude != null) {
            val lat = latitude.toString().toRequestBody("text/plain".toMediaType())
            val lon = longitude.toString().toRequestBody("text/plain".toMediaType())
            apiService.addNewStoryWithLocation(accessToken, desc, lat, lon, imageMultipart)
        } else {
            apiService.addNewStoryWithoutLocation(accessToken, desc, imageMultipart)
        }

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

    fun getAllStoryWithLocations() {
        mapViewIsLoading.value = true
        val accessToken = "Bearer ${userPreference.getToken()}"
        val client = apiService.getStoryWithLocations(accessToken, 1)
        client.enqueue(object : Callback<GetAllStoryResponse> {
            override fun onResponse(
                call: Call<GetAllStoryResponse>,
                response: Response<GetAllStoryResponse>
            ) {
                mapViewIsLoading.value = false
                if (response.isSuccessful) {
                    listStoryWithLocations.value = response.body()?.listStory
                } else {
                    mapViewErrorResponse.value = Gson().fromJson(
                        response.errorBody()?.charStream(),
                        ErrorResponse::class.java
                    )
                    response.errorBody()?.let {
                        Log.e(TAG, "onFailure: ${it.string()}")
                    }
                }
            }

            override fun onFailure(call: Call<GetAllStoryResponse>, t: Throwable) {
                mapViewIsLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
                mapViewErrorResponse.value = ErrorResponse(true, t.message.toString())
            }
        })
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