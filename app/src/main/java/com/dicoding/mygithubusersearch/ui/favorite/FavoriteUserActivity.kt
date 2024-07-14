package com.dicoding.mygithubusersearch.ui.favorite

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.mygithubusersearch.data.local.entity.FavoriteUser
import com.dicoding.mygithubusersearch.data.remote.response.UserItem
import com.dicoding.mygithubusersearch.databinding.ActivityFavoriteUserBinding
import com.dicoding.mygithubusersearch.ui.UserAdapter
import com.dicoding.mygithubusersearch.ui.ViewModelFactory

class FavoriteUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val favoriteUserViewModel = obtainViewModel(this@FavoriteUserActivity)
        favoriteUserViewModel.getAllFavoriteUser().observe(this) { users ->
            checkSize(users)
            val items = mutableListOf<UserItem>()
            users.map {
                val item = UserItem(login = it.username, avatarUrl = it.avatarUrl, type = it.type)
                items.add(item)
            }
            setUserData(items)
        }
        favoriteUserViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        initRecycleView()
    }

    private fun initRecycleView() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUser.addItemDecoration(itemDecoration)
    }

    private fun setUserData(users: List<UserItem?>?) {
        val adapter = UserAdapter()
        adapter.submitList(users)
        binding.rvUser.adapter = adapter
    }

    private fun checkSize(list: List<FavoriteUser>) {
        if (list.isEmpty()) {
            binding.tvNoUserData.visibility = View.VISIBLE
        } else {
            binding.tvNoUserData.visibility = View.GONE
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.tvNoUserData.visibility = View.GONE
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteUserViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteUserViewModel::class.java]
    }
}