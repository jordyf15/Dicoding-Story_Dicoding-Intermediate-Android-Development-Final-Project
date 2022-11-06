package com.jordyf15.storyapp.data

import com.jordyf15.storyapp.data.remote.response.AddStoryResponse
import com.jordyf15.storyapp.data.remote.response.GetAllStoryResponse
import com.jordyf15.storyapp.data.remote.response.LoginResponse
import com.jordyf15.storyapp.data.remote.response.RegisterResponse
import com.jordyf15.storyapp.data.remote.retrofit.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call

class FakeApiService:ApiService {
    override suspend fun register(name: String, email: String, password: String): RegisterResponse {
        TODO("Not yet implemented")
    }

    override suspend fun login(email: String, password: String): LoginResponse {
        TODO("Not yet implemented")
    }

    override suspend fun addNewStoryWithLocation(
        token: String,
        description: RequestBody,
        lat: RequestBody,
        lon: RequestBody,
        file: MultipartBody.Part
    ): AddStoryResponse {
        TODO("Not yet implemented")
    }

    override suspend fun addNewStoryWithoutLocation(
        token: String,
        description: RequestBody,
        file: MultipartBody.Part
    ): AddStoryResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getStories(
        token: String,
        location: Int,
        page: Int?,
        size: Int?
    ): GetAllStoryResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getStoryWithLocations(token: String, location: Int): GetAllStoryResponse {
        TODO("Not yet implemented")
    }

}