package com.jordyf15.storyapp.ui.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.jordyf15.storyapp.R
import com.jordyf15.storyapp.data.remote.response.Story
import com.jordyf15.storyapp.databinding.ActivityStoryDetailBinding
import com.jordyf15.storyapp.ui.add.AddStoryActivity
import com.jordyf15.storyapp.ui.login.LoginActivity
import com.jordyf15.storyapp.utils.Utils
import com.jordyf15.storyapp.utils.ViewModelFactory

class StoryDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryDetailBinding
    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var storyDetailViewModel: StoryDetailViewModel

    companion object {
        const val EXTRA_STORY = "extra_story"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModelFactory = ViewModelFactory.getInstance(this)
        storyDetailViewModel = viewModelFactory.create(StoryDetailViewModel::class.java)

        storyDetailViewModel.isLoggedIn.observe(this) {
            if (!it) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        val story = intent.getParcelableExtra<Story>(EXTRA_STORY) as Story

        binding.tvName.text = story.name
        binding.tvDescription.text = story.description
        binding.tvTimestamp.text = Utils.createdAtToNow(story.createdAt)
        Glide.with(this)
            .load(story.photoUrl)
            .into(binding.imgPhoto)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_add_story -> {
                val intent = Intent(this@StoryDetailActivity, AddStoryActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.menu_logout -> {
                storyDetailViewModel.logout()
                true
            }
            else -> true
        }
    }

}