package com.jordyf15.storyapp.ui.add

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.jordyf15.storyapp.data.Result
import com.jordyf15.storyapp.data.StoryRepository
import com.jordyf15.storyapp.data.remote.response.AddStoryResponse
import com.jordyf15.storyapp.utils.DataDummy
import com.jordyf15.storyapp.utils.getOrAwaitValue
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import java.io.File

@RunWith(MockitoJUnitRunner::class)
class AddStoryViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository

    private lateinit var addStoryViewModel: AddStoryViewModel
    private val dummyData = DataDummy.generateDummyAddStoryResponse()

    @Before
    fun setUp() {
        addStoryViewModel = AddStoryViewModel(storyRepository)
    }

    @Test
    fun `when add story successful should return correct AddStoryResponse and Return Success`() {
        val expectedData = MutableLiveData<Result<AddStoryResponse>>()
        expectedData.value = Result.Success(dummyData)

        var file: File = File.createTempFile("jpg", "jpg")
        `when`(storyRepository.addStory(file, "description", 1F, 1F)).thenReturn(expectedData)

        val actualData = addStoryViewModel.addStory(file, "description", 1F, 1F).getOrAwaitValue()

        Mockito.verify(storyRepository).addStory(file, "description", 1F, 1F)
        Assert.assertNotNull(actualData)
        Assert.assertTrue(actualData is Result.Success)
        Assert.assertEquals(dummyData.message, (actualData as Result.Success).data.message)
        Assert.assertEquals(dummyData.error, (actualData as Result.Success).data.error)
    }

    @Test
    fun `when add story failed should Return Error`() {
        val addStoryResponse = MutableLiveData<Result<AddStoryResponse>>()
        addStoryResponse.value = Result.Error("Error")

        var file: File = File.createTempFile("jpg", "jpg")
        `when`(storyRepository.addStory(file, "description", 1F, 1F)).thenReturn(addStoryResponse)

        val actualData = addStoryViewModel.addStory(file, "description", 1F, 1F).getOrAwaitValue()
        Mockito.verify(storyRepository).addStory(file, "description", 1F, 1F)
        Assert.assertNotNull(actualData)
        Assert.assertTrue(actualData is Result.Error)
    }

}