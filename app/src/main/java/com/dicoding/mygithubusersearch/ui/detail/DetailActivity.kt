package com.dicoding.mygithubusersearch.ui.detail

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.mygithubusersearch.R
import com.dicoding.mygithubusersearch.data.local.entity.FavoriteUser
import com.dicoding.mygithubusersearch.data.remote.response.DetailUserResponse
import com.dicoding.mygithubusersearch.databinding.ActivityDetailBinding
import com.dicoding.mygithubusersearch.ui.ViewModelFactory
import com.dicoding.mygithubusersearch.ui.follow.FollowPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val detailViewModel = obtainViewModel(this@DetailActivity)
        val username = intent.getStringExtra("username") ?: ""
        detailViewModel.getDetailUser(username)
        initTabLayout(username)

        detailViewModel.user.observe(this) { user ->
            setUserData(user, detailViewModel)
        }
        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun initTabLayout(username: String) {
        val sectionsPagerAdapter = FollowPagerAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.viewPager)
        sectionsPagerAdapter.username = username
        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = findViewById(R.id.tabLayout)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f
    }

    private fun setUserData(user: DetailUserResponse, detailViewModel: DetailViewModel) {
        binding.tvName.text = user.name
        binding.tvLogin.text = user.login
        binding.tvFollow.text = binding.root.context.getString(
            R.string.follow,
            user.followers, user.following
        )

        Glide.with(binding.root.context)
            .load(user.avatarUrl)
            .into(binding.avatar)

        observeFavorite(user, detailViewModel)
    }

    private fun observeFavorite(user: DetailUserResponse, detailViewModel: DetailViewModel) {
        detailViewModel.getFavoriteUser(user.login!!).observe(this) { favoriteUser ->
            if (favoriteUser == null) {
                binding.fab.setImageDrawable(
                    ContextCompat.getDrawable(
                        binding.fab.context,
                        R.drawable.ic_favorite_border
                    )
                )
                binding.fab.setOnClickListener {
                    detailViewModel.insertFavoriteUser(
                        FavoriteUser(
                            user.login,
                            user.avatarUrl,
                            user.type
                        )
                    )
                }
            } else {
                binding.fab.setImageDrawable(
                    ContextCompat.getDrawable(
                        binding.fab.context,
                        R.drawable.ic_favorite
                    )
                )
                binding.fab.setOnClickListener {
                    detailViewModel.deleteFavoriteUser(favoriteUser)
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun obtainViewModel(activity: AppCompatActivity): DetailViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[DetailViewModel::class.java]
    }
}