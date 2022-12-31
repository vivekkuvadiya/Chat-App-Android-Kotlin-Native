package com.vivek.chatingapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vivek.chatingapp.databinding.ItemUserListBinding
import com.vivek.chatingapp.model.User
import com.vivek.chatingapp.utils.decodeToBitmap
import javax.inject.Inject

class UsersAdapter @Inject constructor() :ListAdapter<User,UsersAdapter.UserViewHolder>(UserDiffer()) {


    class UserViewHolder(val binding:ItemUserListBinding):RecyclerView.ViewHolder(binding.root){

        fun onUserBind(user: User) {
            binding.apply {
                tvName.text = user.name
                tvEmail.text = user.email
                ivProfile.setImageBitmap(user.image?.decodeToBitmap())
            }
        }

    }

    class UserDiffer : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.token == newItem.token
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        UserViewHolder(ItemUserListBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = currentList[position]
        holder.onUserBind(user)
        holder.binding.root.setOnClickListener {
            onUserClick?.let { it1 -> it1(user) }
        }
    }

    var onUserClick: ((User) -> Unit)? = null


}