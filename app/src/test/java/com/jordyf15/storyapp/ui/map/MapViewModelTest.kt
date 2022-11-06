package com.jordyf15.storyapp.ui.map

import com.google.android.gms.maps.MapView
import com.jordyf15.storyapp.data.StoryRepository
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MapViewModelTest{
    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var mapViewModel: MapViewModel

    @Before
    fun setUp() {
        mapViewModel = MapViewModel(storyRepository)
    }

    @Test
    fun `when getAllStoryWithLocations should call getAllStoryWithLocations method of storyRepository`() {
        mapViewModel.getAllStoryWithLocations()
        Mockito.verify(storyRepository).getAllStoryWithLocations()
    }

}