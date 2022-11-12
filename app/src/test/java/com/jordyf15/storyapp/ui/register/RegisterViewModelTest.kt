package com.jordyf15.storyapp.ui.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.jordyf15.storyapp.data.Result
import com.jordyf15.storyapp.data.UserRepository
import com.jordyf15.storyapp.data.remote.response.RegisterResponse
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
class RegisterViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var userRepository: UserRepository

    private lateinit var registerViewModel: RegisterViewModel
    private val dummyData = DataDummy.generateDummyRegisterResponse()

    @Before
    fun setUp() {
        registerViewModel = RegisterViewModel(userRepository)
    }

    @Test
    fun `when register successful should return correct RegisterResponse data and Return Success`() {
        val expectedData = MutableLiveData<Result<RegisterResponse>>()
        expectedData.value = Result.Success(dummyData)

        `when`(userRepository.register("jojo", "jojo@gmail.com", "password123")).thenReturn(
            expectedData
        )

        val actualData =
            registerViewModel.register("jojo", "jojo@gmail.com", "password123").getOrAwaitValue()

        Mockito.verify(userRepository).register("jojo", "jojo@gmail.com", "password123")
        Assert.assertNotNull(actualData)
        Assert.assertTrue(actualData is Result.Success)
        Assert.assertEquals(dummyData.message, (actualData as Result.Success).data.message)
        Assert.assertEquals(dummyData.error, (actualData as Result.Success).data.error)
    }

    @Test
    fun `when register failed should Return Error`() {
        val registerResponse = MutableLiveData<Result<RegisterResponse>>()
        registerResponse.value = Result.Error("Error")
        `when`(userRepository.register("jojo", "jojo@gmail.com", "password123")).thenReturn(
            registerResponse
        )
        val actualData =
            registerViewModel.register("jojo", "jojo@gmail.com", "password123").getOrAwaitValue()
        Mockito.verify(userRepository).register("jojo", "jojo@gmail.com", "password123")
        Assert.assertNotNull(actualData)
        Assert.assertTrue(actualData is Result.Error)
    }

}