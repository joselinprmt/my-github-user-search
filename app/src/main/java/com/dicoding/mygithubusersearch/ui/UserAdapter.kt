package com.dicoding.mygithubusersearch.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.mygithubusersearch.R
import com.dicoding.mygithubusersearch.data.remote.response.UserItem
import com.dicoding.mygithubusersearch.databinding.ItemUserBinding
import com.dicoding.mygithubusersearch.ui.detail.DetailActivity

class UserAdapter : ListAdapter<UserItem, UserAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)

        holder.itemView.setOnClickListener {
            val intentDetail = Intent(holder.itemView.context, DetailActivity::class.java)
            intentDetail.putExtra("username", user.login)
            holder.itemView.context.startActivity(intentDetail)
        }
    }

    class MyViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserItem){
            binding.tvItem.text = "${user.login}"
            binding.tvType.text = binding.root.context.getString(R.string.type,
                user.type?.lowercase() ?: ""
            )

            Glide.with(binding.root.context)
                .load(user.avatarUrl)
                .into(binding.avatar)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<UserItem>() {
            override fun areItemsTheSame(oldItem: UserItem, newItem: UserItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: UserItem, newItem: UserItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}