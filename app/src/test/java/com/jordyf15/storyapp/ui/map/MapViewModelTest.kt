package com.jordyf15.storyapp.ui.map

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.jordyf15.storyapp.data.Result
import com.jordyf15.storyapp.data.StoryRepository
import com.jordyf15.storyapp.data.remote.response.GetAllStoryResponse
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

@RunWith(MockitoJUnitRunner::class)
class MapViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository

    private lateinit var mapViewModel: MapViewModel
    private val dummyData = DataDummy.generateDummyGetAllStoryResponse()

    @Before
    fun setUp() {
        mapViewModel = MapViewModel(storyRepository)
    }

    @Test
    fun `when get all story with locations successful should return correct GetAllStoryResponse data and Return Success`() {
        val expectedData = MutableLiveData<Result<GetAllStoryResponse>>()
        expectedData.value = Result.Success(dummyData)

        `when`(storyRepository.getAllStoryWithLocations()).thenReturn(expectedData)

        val actualData = mapViewModel.getAllStoryWithLocations().getOrAwaitValue()

        Mockito.verify(storyRepository).getAllStoryWithLocations()
        Assert.assertNotNull(actualData)
        Assert.assertTrue(actualData is Result.Success)
        Assert.assertEquals(dummyData.message, (actualData as Result.Success).data.message)
        Assert.assertEquals(dummyData.error, (actualData as Result.Success).data.error)
        Assert.assertEquals(
            dummyData.listStory.size,
            (actualData as Result.Success).data.listStory.size
        )
    }

    @Test
    fun `when get all story with locations failed should Return Error`() {
        val getAllStoryWithLocationResponse = MutableLiveData<Result<GetAllStoryResponse>>()
        getAllStoryWithLocationResponse.value = Result.Error("Error")
        `when`(storyRepository.getAllStoryWithLocations()).thenReturn(
            getAllStoryWithLocationResponse
        )
        val actualData = mapViewModel.getAllStoryWithLocations().getOrAwaitValue()
        Mockito.verify(storyRepository).getAllStoryWithLocations()
        Assert.assertNotNull(actualData)
        Assert.assertTrue(actualData is Result.Error)
    }
}