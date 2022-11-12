package com.jordyf15.storyapp.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.jordyf15.storyapp.data.Result
import com.jordyf15.storyapp.data.UserRepository
import com.jordyf15.storyapp.data.remote.response.LoginResponse
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
class LoginViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var userRepository: UserRepository

    private lateinit var loginViewModel: LoginViewModel
    private val dummyData = DataDummy.generateDummyLoginResponse()

    @Before
    fun setUp() {
        loginViewModel = LoginViewModel(userRepository)
    }

    @Test
    fun `when login successful should return correct LoginResponse data and Return Success`() {
        val expectedData = MutableLiveData<Result<LoginResponse>>()
        expectedData.value = Result.Success(dummyData)

        `when`(userRepository.login("email@gmail.com", "password123")).thenReturn(expectedData)

        val actualData = loginViewModel.login("email@gmail.com", "password123").getOrAwaitValue()

        Mockito.verify(userRepository).login("email@gmail.com", "password123")
        Assert.assertNotNull(actualData)
        Assert.assertTrue(actualData is Result.Success)
        Assert.assertEquals(
            dummyData.loginResult.name,
            (actualData as Result.Success).data.loginResult.name
        )
        Assert.assertEquals(
            dummyData.loginResult.token,
            (actualData as Result.Success).data.loginResult.token
        )
        Assert.assertEquals(
            dummyData.loginResult.userId,
            (actualData as Result.Success).data.loginResult.userId
        )
        Assert.assertEquals(dummyData.message, (actualData as Result.Success).data.message)
        Assert.assertEquals(dummyData.error, (actualData as Result.Success).data.error)
    }

    @Test
    fun `when login failed should Return Error`() {
        val loginResponse = MutableLiveData<Result<LoginResponse>>()
        loginResponse.value = Result.Error("Error")
        `when`(userRepository.login("email@gmail.com", "password123")).thenReturn(loginResponse)
        val actualData = loginViewModel.login("email@gmail.com", "password123").getOrAwaitValue()
        Mockito.verify(userRepository).login("email@gmail.com", "password123")
        Assert.assertNotNull(actualData)
        Assert.assertTrue(actualData is Result.Error)
    }

}
