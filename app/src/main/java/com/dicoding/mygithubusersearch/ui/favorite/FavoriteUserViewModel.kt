package com.dicoding.mygithubusersearch.ui.favorite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.mygithubusersearch.data.FavoriteUserRepository
import com.dicoding.mygithubusersearch.data.local.entity.FavoriteUser

class FavoriteUserViewModel(application: Application) : ViewModel() {

    val isLoading = MutableLiveData<Boolean>()
    private val mFavoriteUserRepository : FavoriteUserRepository = FavoriteUserRepository(application)

    fun getAllFavoriteUser() : LiveData<List<FavoriteUser>> {
        isLoading.value = false
        return mFavoriteUserRepository.getAllFavoriteUser()
    }
}