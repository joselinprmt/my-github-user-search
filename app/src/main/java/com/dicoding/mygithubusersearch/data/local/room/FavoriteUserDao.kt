package com.dicoding.mygithubusersearch.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.dicoding.mygithubusersearch.data.local.entity.FavoriteUser

@Dao
interface FavoriteUserDao {
    @Query("SELECT * from favoriteuser ORDER BY username ASC")
    fun getAllFavoriteUser(): LiveData<List<FavoriteUser>>

    @Query("SELECT * FROM FavoriteUser WHERE username = :username")
    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUser>

    @Insert
    fun insert(favoriteUser: FavoriteUser)

    @Delete
    fun delete(favoriteUser: FavoriteUser)
}