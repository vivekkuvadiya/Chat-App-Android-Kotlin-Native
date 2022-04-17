package com.vivek.chatingapp.model

import java.util.*

data class ChatMessage(
    val senderId:String,
    val receiverId:String,
    val message:String,
    val dateTime:String,
    val date:Date
)
