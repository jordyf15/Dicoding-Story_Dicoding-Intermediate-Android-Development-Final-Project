package com.jordyf15.storyapp.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.jordyf15.storyapp.adapter.ListStoryAdapter
import com.jordyf15.storyapp.data.StoryRepository
import com.jordyf15.storyapp.data.UserRepository
import com.jordyf15.storyapp.data.remote.response.Story
import com.jordyf15.storyapp.utils.DataDummy
import com.jordyf15.storyapp.utils.MainDispatcherRule
import com.jordyf15.storyapp.utils.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var storyRepository: StoryRepository

    private lateinit var mainViewModel: MainViewModel

    @Before
    fun setUp() {
        mainViewModel = MainViewModel(storyRepository, userRepository)
    }

    @Test
    fun `when logout should call logout method of userRepository`() {
        mainViewModel.logout()
        Mockito.verify(userRepository).logout()
    }

    @Test
    fun `when Get Stories should not null and Return Success`() = runTest {
        val dummyStories = DataDummy.generateDummyGetAllStoryResponse().listStory
        val data: PagingData<Story> = StoryPagingSource.snapshot(dummyStories)

        val expectedStories = MutableLiveData<PagingData<Story>>()
        expectedStories.value = data
        Mockito.`when`(storyRepository.getStories()).thenReturn(expectedStories)

        val actualStories: PagingData<Story> = mainViewModel.stories.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = ListStoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )

        differ.submitData(actualStories)

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyStories, differ.snapshot())
        Assert.assertEquals(dummyStories.size, differ.snapshot().size)
        Assert.assertEquals(dummyStories[0].id, differ.snapshot()[0]?.id)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}

    override fun onRemoved(position: Int, count: Int) {}

    override fun onMoved(fromPosition: Int, toPosition: Int) {}

    override fun onChanged(position: Int, count: Int, payload: Any?) {}

}

class StoryPagingSource : PagingSource<Int, LiveData<List<Story>>>() {
    companion object {
        fun snapshot(items: List<Story>): PagingData<Story> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<Story>>>): Int? {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<Story>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}