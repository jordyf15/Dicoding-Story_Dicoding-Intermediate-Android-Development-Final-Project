package com.jordyf15.storyapp.ui.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.jordyf15.storyapp.data.StoryRepository
import com.jordyf15.storyapp.data.remote.response.ErrorResponse
import java.io.File

class AddStoryViewModel(
    private val storyRepository: StoryRepository
) : ViewModel() {
    val errorResponse: LiveData<ErrorResponse> = storyRepository.addViewErrorResponse
    val isLoading: LiveData<Boolean> = storyRepository.addViewIsLoading
    val finishAddStory: LiveData<Boolean> = storyRepository.finishAddStory

    fun addStory(storyImg: File, description: String) =
        storyRepository.addStory(storyImg, description)

    fun resetAddStory() = storyRepository.resetAddStory()
}