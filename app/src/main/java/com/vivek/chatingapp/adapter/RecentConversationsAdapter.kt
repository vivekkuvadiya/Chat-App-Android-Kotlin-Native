package com.vivek.chatingapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vivek.chatingapp.databinding.ItemUserListBinding
import com.vivek.chatingapp.databinding.ItemUserListRecentConversionBinding
import com.vivek.chatingapp.model.ChatMessage
import com.vivek.chatingapp.utils.decodeToBitmap

class RecentConversationsAdapter:
    RecyclerView.Adapter<RecentConversationsAdapter.ConversationViewHolder>() {

    private var recentConversationList = mutableListOf<ChatMessage>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ConversationViewHolder(ItemUserListRecentConversionBinding.inflate(LayoutInflater.from(parent.context)))

    override fun onBindViewHolder(holder: ConversationViewHolder, position: Int) {
        holder.setData(recentConversationList[position])
    }

    override fun getItemCount() = recentConversationList.size

    inner class ConversationViewHolder(private val binding: ItemUserListRecentConversionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(chatMessage: ChatMessage) {
            with(binding){
                ivProfile.setImageBitmap(chatMessage.conversionImage?.decodeToBitmap())
                tvName.text = chatMessage.conversionName
                tvRecentMessage.text = chatMessage.message
            }

        }

    }

}