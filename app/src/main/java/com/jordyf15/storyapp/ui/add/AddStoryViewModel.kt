package com.jordyf15.storyapp.ui.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.jordyf15.storyapp.data.StoryRepository
import com.jordyf15.storyapp.data.remote.response.ErrorResponse
import java.io.File

class AddStoryViewModel(
    private val storyRepository: StoryRepository
) : ViewModel() {
    fun addStory(storyImg: File, description: String, latitude: Float?, longitude: Float?) =
        storyRepository.addStory(storyImg, description, latitude, longitude)
}