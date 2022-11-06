package com.jordyf15.storyapp.data.remote.retrofit

import com.jordyf15.storyapp.data.remote.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @Multipart
    @POST("stories")
    suspend fun addNewStoryWithLocation(
        @Header("Authorization") token: String,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody,
        @Part("lon") lon: RequestBody,
        @Part file: MultipartBody.Part,
    ): AddStoryResponse

    @Multipart
    @POST("stories")
    suspend fun addNewStoryWithoutLocation(
        @Header("Authorization") token: String,
        @Part("description") description: RequestBody,
        @Part file: MultipartBody.Part,
    ): AddStoryResponse

    @GET("stories")
    suspend fun getStories(
        @Header("Authorization") token: String,
        @Query("location") location: Int,
        @Query("page") page: Int?,
        @Query("size") size: Int?
    ): GetAllStoryResponse

    @GET("stories")
    suspend fun getStoryWithLocations(
        @Header("Authorization") token: String,
        @Query("location") location: Int,
    ): GetAllStoryResponse
}