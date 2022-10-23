package com.jordyf15.storyapp.ui.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import com.jordyf15.storyapp.databinding.ActivityRegisterBinding
import com.jordyf15.storyapp.ui.login.LoginActivity
import com.jordyf15.storyapp.utils.ViewModelFactory

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var registerViewModel: RegisterViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModelFactory = ViewModelFactory.getInstance(this)
        registerViewModel = viewModelFactory.create(RegisterViewModel::class.java)

        registerViewModel.registerResponse.observe(this) {
            binding.tvErrorMsg.text = ""
            moveToLoginActivity()
        }
        registerViewModel.errorResponse.observe(this) {
            binding.tvErrorMsg.text = it.message
        }
        registerViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        binding.registerBtn.isEnabled = false
        binding.tvToLogin.setOnClickListener {
            moveToLoginActivity()
        }
        binding.registerBtn.setOnClickListener {
            register()
        }
        binding.edtName.addTextChangedListener {
            validateForm()
        }
        binding.edtPassword.addTextChangedListener {
            validateForm()
        }
        binding.edtEmail.addTextChangedListener {
            validateForm()
        }
        playAnimation()
    }

    private fun playAnimation() {
        val registerHeading =
            ObjectAnimator.ofFloat(binding.registerHeading, View.ALPHA, 1f).setDuration(500)
        val fullNameLbl =
            ObjectAnimator.ofFloat(binding.edtNameLabel, View.ALPHA, 1f).setDuration(500)
        val fullNameEdt = ObjectAnimator.ofFloat(binding.edtName, View.ALPHA, 1f).setDuration(500)
        val emailLbl =
            ObjectAnimator.ofFloat(binding.edtEmailLabel, View.ALPHA, 1f).setDuration(500)
        val emailEdt = ObjectAnimator.ofFloat(binding.edtEmail, View.ALPHA, 1f).setDuration(500)
        val passwordLbl =
            ObjectAnimator.ofFloat(binding.edtPasswordLabel, View.ALPHA, 1f).setDuration(500)
        val passwordEdt =
            ObjectAnimator.ofFloat(binding.edtPassword, View.ALPHA, 1f).setDuration(500)
        val registerBtn =
            ObjectAnimator.ofFloat(binding.registerBtn, View.ALPHA, 1f).setDuration(500)
        val loginLink = ObjectAnimator.ofFloat(binding.loginLink, View.ALPHA, 1f).setDuration(500)

        val fullName = AnimatorSet().apply {
            playTogether(fullNameLbl, fullNameEdt)
        }

        val email = AnimatorSet().apply {
            playTogether(emailLbl, emailEdt)
        }

        val password = AnimatorSet().apply {
            playTogether(passwordLbl, passwordEdt)
        }

        val clickable = AnimatorSet().apply {
            playTogether(registerBtn, loginLink)
        }
        AnimatorSet().apply {
            playSequentially(
                registerHeading,
                fullName,
                email,
                password,
                clickable
            )
            start()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun register() {
        val name = binding.edtName.text.toString()
        val email = binding.edtEmail.text.toString()
        val password = binding.edtPassword.text.toString()
        registerViewModel.register(name, email, password)
    }

    private fun moveToLoginActivity() {
        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun validateForm() {
        binding.registerBtn.isEnabled = binding.edtName.text.toString().isNotEmpty() &&
                binding.edtPassword.text.toString().length >= 6 &&
                android.util.Patterns.EMAIL_ADDRESS.matcher(binding.edtEmail.text.toString())
                    .matches()
    }
}