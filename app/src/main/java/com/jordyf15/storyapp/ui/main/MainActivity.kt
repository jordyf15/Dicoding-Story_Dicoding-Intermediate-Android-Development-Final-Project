package com.jordyf15.storyapp.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.jordyf15.storyapp.R
import com.jordyf15.storyapp.data.remote.response.Story
import com.jordyf15.storyapp.ui.detail.StoryDetailActivity
import com.jordyf15.storyapp.databinding.ActivityMainBinding
import com.jordyf15.storyapp.ui.add.AddStoryActivity
import com.jordyf15.storyapp.ui.login.LoginActivity
import com.jordyf15.storyapp.utils.ViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModelFactory = ViewModelFactory.getInstance(this)
        mainViewModel = viewModelFactory.create(MainViewModel::class.java)

        if (!mainViewModel.isLoggedIn()) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        mainViewModel.stories.observe(this) {
            if (it.isEmpty()) {
                binding.tvNoData.visibility = View.VISIBLE
            } else {
                binding.tvNoData.visibility = View.GONE
                showRecyclerList(it)
            }
        }
        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        mainViewModel.errorResponse.observe(this) {
            binding.tvErrorMsg.text = it.message
        }

        mainViewModel.getAllStories()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_add_story -> {
                val intent = Intent(this@MainActivity, AddStoryActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.menu_logout -> {
                mainViewModel.logout()
                Log.e("LOGOUT", "sudah clear session")
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                Log.e("LOGOUT", "akan start activity")
                startActivity(intent)
                Log.e("LOGOUT", "akan finish current activity")
//                finish()
                Log.e("LOGOUT", "finished current activity")
                true
            }
            else -> true
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showRecyclerList(stories: List<Story>) {
        binding.rvStories.layoutManager = LinearLayoutManager(this)
        val listStoryAdapter = ListStoryAdapter(stories)
        binding.rvStories.adapter = listStoryAdapter

        listStoryAdapter.setOnItemClickCallback(object : ListStoryAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Story, optionsCompat: ActivityOptionsCompat) {
                showStoryDetail(data, optionsCompat)
            }
        })
    }

    private fun showStoryDetail(story: Story, optionsCompat: ActivityOptionsCompat) {
        val intent = Intent(this@MainActivity, StoryDetailActivity::class.java)
        intent.putExtra(StoryDetailActivity.EXTRA_STORY, story)
        startActivity(intent, optionsCompat.toBundle())
    }
}