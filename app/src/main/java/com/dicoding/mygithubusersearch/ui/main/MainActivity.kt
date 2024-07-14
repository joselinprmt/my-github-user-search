package com.dicoding.mygithubusersearch.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.mygithubusersearch.R
import com.dicoding.mygithubusersearch.data.remote.response.UserItem
import com.dicoding.mygithubusersearch.databinding.ActivityMainBinding
import com.dicoding.mygithubusersearch.ui.UserAdapter
import com.dicoding.mygithubusersearch.ui.favorite.FavoriteUserActivity
import com.dicoding.mygithubusersearch.ui.setting.SettingActivity
import com.dicoding.mygithubusersearch.ui.setting.SettingPreferences
import com.dicoding.mygithubusersearch.ui.setting.SettingViewModel
import com.dicoding.mygithubusersearch.ui.setting.SettingViewModelFactory
import com.dicoding.mygithubusersearch.ui.setting.dataStore

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mainViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[MainViewModel::class.java]

        mainViewModel.listUser.observe(this) { users ->
            checkSize(users)
            setUserData(users)
        }
        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        observeTheme()
        initSearchBar(mainViewModel)
        initPopupMenu()
        initRecycleView()
    }

    private fun observeTheme() {
        val pref = SettingPreferences.getInstance(dataStore)
        val settingViewModel = ViewModelProvider(this,
            SettingViewModelFactory(pref))[SettingViewModel::class.java]
        settingViewModel.getThemeSettings().observe(this
        ) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun initSearchBar(mainViewModel: MainViewModel) {
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { _, _, _ ->
                    val searchText = searchView.text.toString()
                    mainViewModel.postUser(searchText)
                    searchView.hide()
                    true
                }
        }
    }

    private fun initPopupMenu() {
        binding.ibWidget.setOnClickListener {
            val popupMenu = PopupMenu(this, binding.ibWidget)
            popupMenu.menuInflater.inflate(R.menu.widget_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.favorite -> {
                        val intent = Intent(this@MainActivity, FavoriteUserActivity::class.java)
                        startActivity(intent)
                        true
                    }

                    R.id.settings -> {
                        val intent = Intent(this@MainActivity, SettingActivity::class.java)
                        startActivity(intent)
                        true
                    }

                    else -> false
                }
            }
            popupMenu.show()
        }
    }

    fun initRecycleView() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUser.addItemDecoration(itemDecoration)
    }

    fun setUserData(users: List<UserItem?>?) {
        val adapter = UserAdapter()
        adapter.submitList(users)
        binding.rvUser.adapter = adapter
    }

    fun checkSize(list: List<UserItem?>?) {
        if (list.isNullOrEmpty()) {
            binding.tvNoUserData.visibility = View.VISIBLE
        } else {
            binding.tvNoUserData.visibility = View.GONE
        }
    }

    fun showLoading(isLoading: Boolean) {
        binding.tvNoUserData.visibility = View.GONE
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}