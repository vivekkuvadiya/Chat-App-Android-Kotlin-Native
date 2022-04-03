package com.vivek.chatingapp.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vivek.chatingapp.databinding.ItemReceivedMessageBinding
import com.vivek.chatingapp.databinding.ItemSendMessageBinding
import com.vivek.chatingapp.model.ChatMessage
import com.vivek.chatingapp.utils.Constant.VIEW_TYPE_RECEIVED
import com.vivek.chatingapp.utils.Constant.VIEW_TYPE_SEND

class ChatAdapter(
    private val senderId: String,
    private val profileImage: Bitmap,
    private val chatMessages: List<ChatMessage>,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var chatMessagesList = mutableListOf<ChatMessage>()

    init {
        chatMessagesList.addAll(chatMessages)
    }


    class SendMessageViewHolder(val binding: ItemSendMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(message: ChatMessage) {
            binding.apply {
                tvMessage.text = message.message
                tvDateTime.text = message.dateTime
            }
        }

    }

    class ReceivedMessageViewHolder(val binding: ItemReceivedMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(message: ChatMessage, profileImage: Bitmap) {
            binding.apply {
                tvMessage.text = message.message
                tvDateTime.text = message.dateTime
                ivProfile.setImageBitmap(profileImage)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_SEND) {
            SendMessageViewHolder(ItemSendMessageBinding.inflate(LayoutInflater.from(parent.context),
                parent,
                false))
        } else {
            ReceivedMessageViewHolder(ItemReceivedMessageBinding.inflate(LayoutInflater.from(parent.context),
                parent,
                false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == VIEW_TYPE_SEND) {
            val sendHolder = holder as SendMessageViewHolder
            sendHolder.setData(chatMessagesList[position])
        } else {
            val receivedHolder = holder as ReceivedMessageViewHolder
            receivedHolder.setData(chatMessagesList[position], profileImage)
        }
    }

    override fun getItemCount() = chatMessagesList.size

    override fun getItemViewType(position: Int): Int {
        return if (chatMessagesList[position].senderId == senderId)
            VIEW_TYPE_SEND
        else
            VIEW_TYPE_RECEIVED
    }

}