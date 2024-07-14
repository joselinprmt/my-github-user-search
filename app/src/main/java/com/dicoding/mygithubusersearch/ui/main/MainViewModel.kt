package com.dicoding.mygithubusersearch.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.mygithubusersearch.data.remote.response.GithubResponse
import com.dicoding.mygithubusersearch.data.remote.response.UserItem
import com.dicoding.mygithubusersearch.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    var listUser = MutableLiveData<List<UserItem?>?>()
    val isLoading = MutableLiveData<Boolean>()

    companion object {
        private const val TAG = "MainViewModel"
    }

    init {
        postUser("user")
    }

    fun postUser(user: String) {
        isLoading.value = true
        val client = ApiConfig.getApiService().searchUsers(user)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>, response: Response<GithubResponse>
            ) {
                isLoading.value = false
                if (response.isSuccessful) {
                    listUser.value = response.body()!!.items
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}