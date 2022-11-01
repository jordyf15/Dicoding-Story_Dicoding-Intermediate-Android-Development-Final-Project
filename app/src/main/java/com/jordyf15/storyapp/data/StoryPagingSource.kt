package com.jordyf15.storyapp.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.jordyf15.storyapp.data.remote.response.Story
import com.jordyf15.storyapp.data.remote.retrofit.ApiService

class StoryPagingSource(private val apiService: ApiService, private val accessToken: String) :
    PagingSource<Int, Story>() {
    override fun getRefreshKey(state: PagingState<Int, Story>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getStories(accessToken, 0, position, params.loadSize)
            Log.e("StoryPagingSource", "load runned")


            LoadResult.Page(
                data = responseData.listStory,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData.listStory.isEmpty()) null else position + 1,
            )
        } catch (exception: Exception) {
            Log.e("StoryPagingSource", exception.toString())
            Log.e("StoryPagingSource", exception.message.toString())
            return LoadResult.Error(exception)
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}