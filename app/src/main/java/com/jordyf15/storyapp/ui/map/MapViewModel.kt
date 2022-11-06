package com.jordyf15.storyapp.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.jordyf15.storyapp.data.StoryRepository
import com.jordyf15.storyapp.data.remote.response.ErrorResponse
import com.jordyf15.storyapp.data.remote.response.Story

class MapViewModel(
    private val storyRepository: StoryRepository
) : ViewModel() {
    fun getAllStoryWithLocations() = storyRepository.getAllStoryWithLocations()
}