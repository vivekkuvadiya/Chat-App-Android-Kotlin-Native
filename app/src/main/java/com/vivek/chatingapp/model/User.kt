package com.vivek.chatingapp.model

import java.io.Serializable

data class User(
    val name:String,
    val image:String,
    var email:String? = null,
    var token:String? = null,
    val id:String
):Serializable
