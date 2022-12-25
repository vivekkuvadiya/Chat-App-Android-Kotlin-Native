package com.vivek.chatingapp.model

import java.util.*

data class ChatMessage(
    val senderId:String,
    val receiverId:String,
    var message:String,
    val dateTime:String,
    var date:Date,
    val conversionId:String? = null,
    val conversionName:String? = null,
    val conversionImage:String? = null
)
