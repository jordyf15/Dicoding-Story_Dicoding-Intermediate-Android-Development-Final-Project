package com.jordyf15.storyapp.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jordyf15.storyapp.data.StoryRepository
import com.jordyf15.storyapp.data.UserRepository
import com.jordyf15.storyapp.di.Injection
import com.jordyf15.storyapp.ui.add.AddStoryViewModel
import com.jordyf15.storyapp.ui.detail.StoryDetailViewModel
import com.jordyf15.storyapp.ui.login.LoginViewModel
import com.jordyf15.storyapp.ui.main.MainViewModel
import com.jordyf15.storyapp.ui.register.RegisterViewModel

class ViewModelFactory private constructor(
    private val storyRepository: StoryRepository,
    private val userRepository: UserRepository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(storyRepository, userRepository) as T
        } else if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(userRepository) as T
        } else if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(userRepository) as T
        } else if (modelClass.isAssignableFrom(AddStoryViewModel::class.java)) {
            return AddStoryViewModel(storyRepository) as T
        } else if (modelClass.isAssignableFrom(StoryDetailViewModel::class.java)) {
            return StoryDetailViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    Injection.provideStoryRepository(context),
                    Injection.provideUserRepository(context)
                )
            }
    }
}