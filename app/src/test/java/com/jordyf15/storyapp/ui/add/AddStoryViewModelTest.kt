package com.jordyf15.storyapp.ui.add

import com.jordyf15.storyapp.data.StoryRepository
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.io.File

@RunWith(MockitoJUnitRunner::class)
class AddStoryViewModelTest{
    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var addStoryViewModel: AddStoryViewModel

    @Before
    fun setUp() {
        addStoryViewModel = AddStoryViewModel(storyRepository)
    }

    @Test
    fun `when addStory should call addStory method of storyRepository with correct parameter`(){
        var file: File = File.createTempFile("jpg","jpg")
        addStoryViewModel.addStory(file,"description",1F,1F)
        Mockito.verify(storyRepository).addStory(file, "description", 1F,1F)
    }

    @Test
    fun `when resetAddStory should call resetAddStory method of storyRepository`() {
        addStoryViewModel.resetAddStory()
        Mockito.verify(storyRepository).resetAddStory()
    }
}