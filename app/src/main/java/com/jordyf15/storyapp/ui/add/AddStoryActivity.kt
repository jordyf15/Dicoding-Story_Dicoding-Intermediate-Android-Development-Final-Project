package com.jordyf15.storyapp.ui.add

import android.Manifest
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.jordyf15.storyapp.ui.camera.CameraActivity
import com.jordyf15.storyapp.ui.main.MainActivity
import com.jordyf15.storyapp.R
import com.jordyf15.storyapp.databinding.ActivityAddStoryBinding
import com.jordyf15.storyapp.utils.Utils
import com.jordyf15.storyapp.utils.ViewModelFactory
import java.io.File

class AddStoryActivity : AppCompatActivity() {
    private var getFile: File? = null
    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var addStoryViewModel: AddStoryViewModel

    companion object {
        const val CAMERA_X_RESULT = 200

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModelFactory = ViewModelFactory.getInstance(this)
        addStoryViewModel = viewModelFactory.create(AddStoryViewModel::class.java)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        addStoryViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        addStoryViewModel.finishAddStory.observe(this) {
            if (it) {
                binding.tvErrorMsg.text = ""
                addStoryViewModel.resetAddStory()
                moveToMainActivity()
            }
        }
        addStoryViewModel.errorResponse.observe(this) {
            if (it != null && it.error) {
                binding.tvErrorMsg.text = it.message
            }
        }
        binding.btnUpload.isEnabled = false

        binding.btnGallery.setOnClickListener {
            startGallery()
        }
        binding.btnCamera.setOnClickListener {
            startCameraX()
        }
        binding.btnUpload.setOnClickListener {
            if (getFile != null) {
                val file: File = getFile as File
                val description = binding.edtDescription.text.toString()
                addStoryViewModel.addStory(file, description)
            }
        }

        binding.edtDescription.addTextChangedListener {
            validateForm()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    getString(R.string.no_permission),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val selectedImg: Uri = it.data?.data as Uri
            val myFile = Utils.uriToFile(selectedImg, this@AddStoryActivity)
            getFile = myFile
            binding.imgStory.setImageURI(selectedImg)
            validateForm()
        }
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            it.data?.getBooleanExtra("isBackCamera", true) as Boolean
            getFile = myFile
            val result = BitmapFactory.decodeFile(myFile.path)

            binding.imgStory.setImageBitmap(result)
            validateForm()
        }
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun moveToMainActivity() {
        val intent = Intent(this@AddStoryActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun validateForm() {
        binding.btnUpload.isEnabled =
            binding.edtDescription.text.toString().isNotEmpty() && getFile != null
    }
}