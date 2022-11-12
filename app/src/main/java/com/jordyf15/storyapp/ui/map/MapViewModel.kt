package com.jordyf15.storyapp.ui.map

import androidx.lifecycle.ViewModel
import com.jordyf15.storyapp.data.StoryRepository

class MapViewModel(
    private val storyRepository: StoryRepository
) : ViewModel() {
    fun getAllStoryWithLocations() = storyRepository.getAllStoryWithLocations()
}