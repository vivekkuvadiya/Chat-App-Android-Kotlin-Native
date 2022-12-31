package com.vivek.chatingapp.network

import com.google.gson.JsonObject
import com.vivek.chatingapp.model.MessageBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.*

interface Api {

//    @Headers("Authorization: key=AAAAYOAUpdc:APA91bE6BlgQHoXxa_olgtFrxxcN5kuvgReAl38kB4ZcmosSGfcM9qsAdo44S80JsJke7DXFxMtQePFJU_DLUgX1S8rE3MX6Jhzvje2uYgavngLPY9m5IJS3WwBdzkE9ouTPXrvLcr_i",
//        "Content-Type: application/json")
    @POST("send")
    suspend fun sendMessage(
        @HeaderMap header:HashMap<String,String>,
//        @Header("Authorization") auth:String = "key=AAAAYOAUpdc:APA91bE6BlgQHoXxa_olgtFrxxcN5kuvgReAl38kB4ZcmosSGfcM9qsAdo44S80JsJke7DXFxMtQePFJU_DLUgX1S8rE3MX6Jhzvje2uYgavngLPY9m5IJS3WwBdzkE9ouTPXrvLcr_i",
//        @Header("Content-Type") contentType:String = "application/json",
        @Body messageBody:MessageBody
    ):Response<JsonObject>

}