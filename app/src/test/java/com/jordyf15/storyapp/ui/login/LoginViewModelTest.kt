package com.jordyf15.storyapp.ui.login

import com.jordyf15.storyapp.data.UserRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest{
    @Mock
    private lateinit var userRepository: UserRepository
    private lateinit var loginViewModel: LoginViewModel

    @Before
    fun setUp() {
        loginViewModel = LoginViewModel(userRepository)
    }

    @Test
    fun `when login should call login method of userRepository with correct parameter`() {
        loginViewModel.login("email@email.com", "password")
        Mockito.verify(userRepository).login("email@email.com", "password")
    }

}
