package com.jordyf15.storyapp.utils

import com.jordyf15.storyapp.data.remote.response.*

object DataDummy {
    fun generateDummyRegisterResponse(): RegisterResponse {
        return RegisterResponse(false, "Register successful")
    }

    fun generateDummyLoginResponse(): LoginResponse {
        val loginResult = LoginResult("userId-1", "jojo", "token-123")
        return LoginResponse(false, "Login successful", loginResult)
    }

    fun generateDummyAddStoryResponse(): AddStoryResponse {
        return AddStoryResponse(false, "Add Story Successful")
    }

    fun generateDummyGetAllStoryResponse(): GetAllStoryResponse {
        val storyList = ArrayList<Story>()
        for (i in 0..10) {
            val story = Story(
                "id $i",
                "name $i",
                "description $i",
                "/https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/story/image$i",
                "2022-02-22T22:22:22Z",
                1.1,
                1.1,
            )
            storyList.add(story)
        }
        return GetAllStoryResponse(false, "Get Story Successful", storyList)
    }
}