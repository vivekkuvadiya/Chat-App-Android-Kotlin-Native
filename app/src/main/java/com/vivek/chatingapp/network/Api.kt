package com.vivek.chatingapp.network

import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.POST

interface Api {

    @POST("send")
    fun sendMessage(
        @HeaderMap header:HashMap<String,String>,
        @Body messageBody:String
    )

}