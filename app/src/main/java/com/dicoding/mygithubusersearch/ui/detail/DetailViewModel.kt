package com.dicoding.mygithubusersearch.ui.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.mygithubusersearch.data.FavoriteUserRepository
import com.dicoding.mygithubusersearch.data.local.entity.FavoriteUser
import com.dicoding.mygithubusersearch.data.remote.response.DetailUserResponse
import com.dicoding.mygithubusersearch.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : ViewModel() {

    val user = MutableLiveData<DetailUserResponse>()
    val isLoading = MutableLiveData<Boolean>()
    private var isUserDataFetched = false
    private val mFavoriteUserRepository: FavoriteUserRepository =
        FavoriteUserRepository(application)

    companion object {
        private const val TAG = "DetailViewModel"
    }

    fun getFavoriteUser(username: String): LiveData<FavoriteUser> =
        mFavoriteUserRepository.getFavoriteUserByUsername(username)

    fun insertFavoriteUser(favoriteUser: FavoriteUser) {
        mFavoriteUserRepository.insert(favoriteUser)
    }

    fun deleteFavoriteUser(favoriteUser: FavoriteUser) {
        mFavoriteUserRepository.delete(favoriteUser)
    }

    fun getDetailUser(username: String) {
        if (!isUserDataFetched || user.value?.login != username) {
            isLoading.value = true
            val client = ApiConfig.getApiService().getDetailUser(username)
            client.enqueue(object : Callback<DetailUserResponse> {
                override fun onResponse(
                    call: Call<DetailUserResponse>,
                    response: Response<DetailUserResponse>
                ) {
                    isLoading.value = false
                    if (response.isSuccessful) {
                        user.value = response.body()
                        isUserDataFetched = true
                    } else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                    isLoading.value = false
                    Log.e(TAG, "onFailure: ${t.message.toString()}")
                }
            })
        }
    }
}