package com.jordyf15.storyapp.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import com.jordyf15.storyapp.ui.main.MainActivity
import com.jordyf15.storyapp.ui.register.RegisterActivity
import com.jordyf15.storyapp.databinding.ActivityLoginBinding
import com.jordyf15.storyapp.utils.ViewModelFactory

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var viewModelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModelFactory = ViewModelFactory.getInstance(this)
        loginViewModel = viewModelFactory.create(LoginViewModel::class.java)

        if (loginViewModel.isLoggedIn()) {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }

        loginViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        loginViewModel.loginResponse.observe(this) {
            moveToMainActivity(it.loginResult.token)
        }
        loginViewModel.errorResponse.observe(this) {
            binding.tvErrorMsg.text = it.message
        }
        binding.loginBtn.isEnabled = false
        binding.tvToRegister.setOnClickListener {
            moveToRegisterActivity()
        }
        binding.loginBtn.setOnClickListener {
            login()
        }
        binding.edtEmail.addTextChangedListener {
            validateForm()
        }
        binding.edtPassword.addTextChangedListener {
            validateForm()
        }
        playAnimation()
    }

    private fun playAnimation() {
        val loginHeading =
            ObjectAnimator.ofFloat(binding.loginHeading, View.ALPHA, 1f).setDuration(500)
        val emailLbl =
            ObjectAnimator.ofFloat(binding.edtEmailLabel, View.ALPHA, 1f).setDuration(500)
        val emailEdt = ObjectAnimator.ofFloat(binding.edtEmail, View.ALPHA, 1f).setDuration(500)
        val passwordLbl =
            ObjectAnimator.ofFloat(binding.edtPasswordLabel, View.ALPHA, 1f).setDuration(500)
        val passwordEdt =
            ObjectAnimator.ofFloat(binding.edtPassword, View.ALPHA, 1f).setDuration(500)
        val loginBtn = ObjectAnimator.ofFloat(binding.loginBtn, View.ALPHA, 1f).setDuration(500)
        val registerLink =
            ObjectAnimator.ofFloat(binding.registerLink, View.ALPHA, 1f).setDuration(500)

        val email = AnimatorSet().apply {
            playTogether(emailLbl, emailEdt)
        }

        val password = AnimatorSet().apply {
            playTogether(passwordLbl, passwordEdt)
        }

        val clickable = AnimatorSet().apply {
            playTogether(loginBtn, registerLink)
        }

        AnimatorSet().apply {
            playSequentially(loginHeading, email, password, clickable)
            start()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun moveToRegisterActivity() {
        val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun moveToMainActivity(token: String) {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }

    private fun validateForm() {
        binding.loginBtn.isEnabled =
            android.util.Patterns.EMAIL_ADDRESS.matcher(binding.edtEmail.text.toString())
                .matches() && binding.edtPassword.text.toString().length >= 6
    }

    private fun login() {
        val email = binding.edtEmail.text.toString()
        val password = binding.edtPassword.text.toString()
        loginViewModel.login(email, password)
    }
}