package com.vivek.chatingapp.ui.chat

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vivek.chatingapp.repository.MainRepository
import com.vivek.chatingapp.utils.Constant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap

@HiltViewModel
class ChatViewModel @Inject constructor(private val repository:MainRepository, private val pref: SharedPreferences) : ViewModel() {


    fun sendMessage(message: String, receiverId:String){

        viewModelScope.launch {

            val messageMap = HashMap<String,Any>()
            messageMap[Constant.KEY_SENDER_ID] = pref.getString(Constant.KEY_USER_ID,null).toString()
            messageMap[Constant.KEY_RECEIVER_ID] = receiverId
            messageMap[Constant.KEY_MESSAGE] = message
            messageMap[Constant.KEY_TIMESTAMP] = Date()

            repository.sendMessage(messageMap)

        }

    }

}