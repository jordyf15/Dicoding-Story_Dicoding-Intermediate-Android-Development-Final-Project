package com.jordyf15.storyapp.data.remote.retrofit

import com.jordyf15.storyapp.data.remote.response.AddStoryResponse
import com.jordyf15.storyapp.data.remote.response.GetAllStoryResponse
import com.jordyf15.storyapp.data.remote.response.LoginResponse
import com.jordyf15.storyapp.data.remote.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @Multipart
    @POST("stories")
    fun addNewStory(
        @Header("Authorization") token: String,
        @Part("description") description: RequestBody,
        @Part file: MultipartBody.Part,
    ): Call<AddStoryResponse>

    @GET("stories")
    fun getAllStories(
        @Header("Authorization") token: String,
    ): Call<GetAllStoryResponse>
}