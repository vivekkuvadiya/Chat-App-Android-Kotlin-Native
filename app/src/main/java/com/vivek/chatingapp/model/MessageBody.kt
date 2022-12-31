package com.vivek.chatingapp.model

import com.google.gson.annotations.SerializedName

data class MessageBody(
    @SerializedName("data")
    val data: Data,
    @SerializedName("registration_ids")
    val regIs:List<String>
)

data class Data(
    @SerializedName("userId")
    val userId:String,
    @SerializedName("name")
    val name:String,
    @SerializedName("fcmToken")
    val fcmToken:String,
    @SerializedName("message")
    val message:String
)