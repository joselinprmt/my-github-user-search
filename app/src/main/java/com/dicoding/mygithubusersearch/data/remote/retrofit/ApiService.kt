package com.dicoding.mygithubusersearch.data.remote.retrofit

import com.dicoding.mygithubusersearch.data.remote.response.DetailUserResponse
import com.dicoding.mygithubusersearch.data.remote.response.GithubResponse
import com.dicoding.mygithubusersearch.data.remote.response.UserItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    fun searchUsers(
        @Query("q") username: String
    ): Call<GithubResponse>

    @GET("users/{username}")
    fun getDetailUser(@Path("username") username: String
    ): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    fun getFollowers(@Path("username") username: String
    ): Call<List<UserItem>>

    @GET("users/{username}/following")
    fun getFollowing(@Path("username") username: String
    ): Call<List<UserItem>>
}