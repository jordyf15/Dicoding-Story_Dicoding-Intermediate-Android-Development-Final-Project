package com.jordyf15.storyapp.ui.detail

import com.jordyf15.storyapp.data.UserRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class StoryDetailViewModelTest {
    @Mock
    private lateinit var userRepository: UserRepository
    private lateinit var storyDetailViewModel: StoryDetailViewModel

    @Before
    fun setUp() {
        storyDetailViewModel = StoryDetailViewModel(userRepository)
    }

    @Test
    fun `when logout should call logout method of userRepository`() {
        storyDetailViewModel.logout()
        Mockito.verify(userRepository).logout()
    }
}