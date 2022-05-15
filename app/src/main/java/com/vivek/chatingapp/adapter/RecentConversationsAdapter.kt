package com.vivek.chatingapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vivek.chatingapp.databinding.ItemUserListBinding
import com.vivek.chatingapp.model.ChatMessage
import com.vivek.chatingapp.utils.decodeToBitmap

class RecentConversationsAdapter:
    RecyclerView.Adapter<RecentConversationsAdapter.ConversationViewHolder>() {

    private var recentConversationList = mutableListOf<ChatMessage>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ConversationViewHolder(ItemUserListBinding.inflate(LayoutInflater.from(parent.context)))

    override fun onBindViewHolder(holder: ConversationViewHolder, position: Int) {
        holder.setData(recentConversationList[position])
    }

    override fun getItemCount() = recentConversationList.size

    inner class ConversationViewHolder(val binding: ItemUserListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(chatMessage: ChatMessage) {
            binding.ivProfile.setImageBitmap(chatMessage.conversionImage.decodeToBitmap())
            binding.tvName.text = chatMessage.conversionName
            binding.tvEmail.text = chatMessage.message
        }

    }

}