package com.jordyf15.storyapp.data.remote.retrofit

import com.jordyf15.storyapp.data.remote.response.*
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
    fun addNewStoryWithLocation(
        @Header("Authorization") token: String,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody,
        @Part("lon") lon: RequestBody,
        @Part file: MultipartBody.Part,
    ): Call<AddStoryResponse>

    @Multipart
    @POST("stories")
    fun addNewStoryWithoutLocation(
        @Header("Authorization") token: String,
        @Part("description") description: RequestBody,
        @Part file: MultipartBody.Part,
    ): Call<AddStoryResponse>

    @GET("stories")
    suspend fun getStories(
        @Header("Authorization") token: String,
        @Query("location") location: Int,
        @Query("page") page: Int?,
        @Query("size") size: Int?
    ): GetAllStoryResponse

    @GET("stories")
    fun getStoryWithLocations(
        @Header("Authorization") token: String,
        @Query("location") location: Int,
    ): Call<GetAllStoryResponse>
}