package com.jordyf15.storyapp.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.jordyf15.storyapp.R
import com.jordyf15.storyapp.adapter.ListStoryAdapter
import com.jordyf15.storyapp.adapter.LoadingStateAdapter
import com.jordyf15.storyapp.data.remote.response.Story
import com.jordyf15.storyapp.ui.detail.StoryDetailActivity
import com.jordyf15.storyapp.databinding.ActivityMainBinding
import com.jordyf15.storyapp.ui.add.AddStoryActivity
import com.jordyf15.storyapp.ui.login.LoginActivity
import com.jordyf15.storyapp.ui.map.MapsActivity
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

        mainViewModel.isLoggedIn.observe(this) {
            if (!it) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        binding.rvStories.layoutManager = LinearLayoutManager(this)

        getData()
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
                true
            }
            R.id.menu_map -> {
                val intent = Intent(this@MainActivity, MapsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> true
        }
    }

    private fun getData() {
        val listStoryAdapter = ListStoryAdapter()
        binding.rvStories.adapter = listStoryAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                listStoryAdapter.retry()
            }
        )
        mainViewModel.stories.observe(this) {
            listStoryAdapter.submitData(lifecycle, it)
        }

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