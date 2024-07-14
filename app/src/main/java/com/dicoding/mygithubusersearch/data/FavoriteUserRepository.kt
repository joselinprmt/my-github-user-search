package com.dicoding.mygithubusersearch.data

import android.app.Application
import androidx.lifecycle.LiveData
import com.dicoding.mygithubusersearch.data.local.entity.FavoriteUser
import com.dicoding.mygithubusersearch.data.local.room.FavoriteUserDao
import com.dicoding.mygithubusersearch.data.local.room.FavoriteUserDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteUserRepository(application: Application) {
    private val mFavoriteUserDao: FavoriteUserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteUserDatabase.getDatabase(application)
        mFavoriteUserDao = db.FavoriteUserDao()
    }

    fun getAllFavoriteUser(): LiveData<List<FavoriteUser>> = mFavoriteUserDao.getAllFavoriteUser()

    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUser> =
        mFavoriteUserDao.getFavoriteUserByUsername(username)

    fun insert(favoriteUser: FavoriteUser) {
        executorService.execute { mFavoriteUserDao.insert(favoriteUser) }
    }

    fun delete(favoriteUser: FavoriteUser) {
        executorService.execute { mFavoriteUserDao.delete(favoriteUser) }
    }
}