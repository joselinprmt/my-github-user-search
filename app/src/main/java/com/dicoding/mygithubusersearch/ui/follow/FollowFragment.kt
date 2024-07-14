package com.dicoding.mygithubusersearch.ui.follow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.mygithubusersearch.data.remote.response.UserItem
import com.dicoding.mygithubusersearch.databinding.FragmentFollowsBinding
import com.dicoding.mygithubusersearch.ui.UserAdapter

class FollowFragment : Fragment() {

    private var _binding: FragmentFollowsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FollowViewModel

    companion object {
        const val ARG_USERNAME = "user"
        const val ARG_POSITION = "number"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val position = it.getInt(ARG_POSITION)
            val username = it.getString(ARG_USERNAME)

            viewModel = ViewModelProvider(
                this,
                ViewModelProvider.NewInstanceFactory()
            )[FollowViewModel::class.java]
            viewModel.setFollows(username!!, position)
        }
        initRecycleView()
        viewModel.listFollow.observe(viewLifecycleOwner) {
            setFollowData(it)
            checkSize(it)
        }
        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
    }

    private fun initRecycleView() {
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFollows.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvFollows.addItemDecoration(itemDecoration)
    }

    private fun setFollowData(users: List<UserItem?>?) {
        val adapter = UserAdapter()
        adapter.submitList(users)
        binding.rvFollows.adapter = adapter
    }

    private fun checkSize(list: List<UserItem>) {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}