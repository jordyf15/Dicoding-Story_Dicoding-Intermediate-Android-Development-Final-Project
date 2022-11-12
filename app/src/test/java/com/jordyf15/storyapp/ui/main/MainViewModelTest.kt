package com.jordyf15.storyapp.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jordyf15.storyapp.data.StoryRepository
import com.jordyf15.storyapp.data.UserRepository
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var storyRepository: StoryRepository

    private lateinit var mainViewModel: MainViewModel

    @Before
    fun setUp() {
        mainViewModel = MainViewModel(storyRepository, userRepository)
    }

    @Test
    fun `when logout should call logout method of userRepository`() {
        mainViewModel.logout()
        Mockito.verify(userRepository).logout()
    }
}