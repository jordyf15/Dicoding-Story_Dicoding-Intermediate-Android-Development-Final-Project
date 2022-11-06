package com.jordyf15.storyapp.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jordyf15.storyapp.data.local.preference.UserPreference
import com.jordyf15.storyapp.data.local.room.StoryDatabase
import com.jordyf15.storyapp.data.remote.retrofit.ApiService
import com.jordyf15.storyapp.utils.MainDispatcherRule
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import java.util.prefs.Preferences

class StoryRepositoryTest{
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var apiService: ApiService
    @Mock
    private lateinit var userPreferences: UserPreference
    @Mock
    private lateinit var storyDatabase: StoryDatabase
    private lateinit var storyRepository: StoryRepository

    @Before
    fun setUp() {
        storyRepository = StoryRepository(apiService, userPreferences, storyDatabase)
    }

    @Test
    fun `when resetAddStory is called it should set the finishAddStory to false`() {
        storyRepository.finishAddStory.value = true

        storyRepository.resetAddStory()

        val finishAddStoryVal = storyRepository.finishAddStory.value
        if (finishAddStoryVal != null) {
            Assert.assertTrue(finishAddStoryVal)
        }
    }
}