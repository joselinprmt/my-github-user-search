package com.dicoding.mygithubusersearch.ui.follow

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.mygithubusersearch.data.remote.response.UserItem
import com.dicoding.mygithubusersearch.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowViewModel: ViewModel()  {

    val listFollow = MutableLiveData<List<UserItem>>()
    val isLoading = MutableLiveData<Boolean>()
    private lateinit var currUsername: String
    private var isUserDataFetched = false

    companion object {
        private const val TAG = "FollowViewModel"
    }

    fun setFollows(username: String, position: Int) {
        if (!isUserDataFetched  || currUsername != username) {
            currUsername = username
            isLoading.value = true
            val client = when (position) {
                1 -> ApiConfig.getApiService().getFollowers(username)
                else -> ApiConfig.getApiService().getFollowing(username)
            }
            client.enqueue(object : Callback<List<UserItem>> {
                override fun onResponse(
                    call: Call<List<UserItem>>,
                    response: Response<List<UserItem>>
                ) {
                    isLoading.value = false
                    if (response.isSuccessful) {
                        listFollow.value = response.body()
                        isUserDataFetched = true
                    } else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<List<UserItem>>, t: Throwable) {
                    isLoading.value = false
                    Log.e(TAG, "onFailure: ${t.message}")
                    t.printStackTrace()
                }
            })
        }
    }
}