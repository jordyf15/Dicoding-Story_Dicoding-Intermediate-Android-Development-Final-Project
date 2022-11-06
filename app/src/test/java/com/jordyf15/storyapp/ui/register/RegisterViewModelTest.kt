package com.jordyf15.storyapp.ui.register

import com.jordyf15.storyapp.data.UserRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RegisterViewModelTest {
    @Mock
    private lateinit var userRepository: UserRepository
    private lateinit var registerViewModel: RegisterViewModel

    @Before
    fun setUp() {
        registerViewModel = RegisterViewModel(userRepository)
    }

    @Test
    fun `when register should call register method of userRepository with correct parameter`() {
        registerViewModel.register("jojo","jojo@gmail.com","password123")
        Mockito.verify(userRepository).register("jojo", "jojo@gmail.com", "password123")
    }
}