package com.dicoding.mygithubusersearch

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.dicoding.mygithubusersearch.data.remote.response.GithubResponse
import com.dicoding.mygithubusersearch.data.remote.response.UserItem
import com.dicoding.mygithubusersearch.ui.main.MainViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.any
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.internal.verification.VerificationModeFactory.times
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var observerLoading: Observer<Boolean>

    @Mock
    private lateinit var observerListUser: Observer<List<UserItem?>?>

    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        viewModel = MainViewModel()
        viewModel.isLoading.observeForever(observerLoading)
        viewModel.listUser.observeForever(observerListUser)
    }

    @Test
    fun `test postUser success`() {
        val mockCall = mock(Call::class.java) as Call<GithubResponse>

        viewModel.postUser("user")
        verify(observerLoading, times(2)).onChanged(true)

        val mockResponse = mock(Response::class.java) as Response<GithubResponse>
        val mockGithubResponse = mock(GithubResponse::class.java)
        val mockUserList = listOf<UserItem?>(mock(UserItem::class.java))

        `when`(mockResponse.isSuccessful).thenReturn(true)
        `when`(mockResponse.body()).thenReturn(mockGithubResponse)
        `when`(mockGithubResponse.items).thenReturn(mockUserList)

        val mockCallback = object : Callback<GithubResponse> {
            override fun onResponse(call: Call<GithubResponse>, response: Response<GithubResponse>) {
                viewModel.isLoading.value = false
                if (response.isSuccessful) {
                    viewModel.listUser.value = response.body()?.items
                }
            }
            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
            }
        }
        mockCallback.onResponse(mockCall, mockResponse)

        verify(observerLoading).onChanged(false)
        verify(observerListUser).onChanged(mockUserList)
    }

    @Test
    fun `test postUser failure`() {
        val mockCall = mock(Call::class.java) as Call<GithubResponse>

        viewModel.postUser("user")
        verify(observerLoading, times(2)).onChanged(true)

        val mockThrowable = mock(Throwable::class.java)
        val mockCallback = object : Callback<GithubResponse> {
            override fun onResponse(call: Call<GithubResponse>, response: Response<GithubResponse>) {
                // Do nothing
            }
            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
            }
        }
        mockCallback.onFailure(mockCall, mockThrowable)
        verify(observerListUser, never()).onChanged(any())
    }
}