package com.vivek.chatingapp.ui.chat

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import com.vivek.chatingapp.model.ChatMessage
import com.vivek.chatingapp.repository.MainRepository
import com.vivek.chatingapp.utils.Constant
import com.vivek.chatingapp.utils.getReadableDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: MainRepository,
    private val pref: SharedPreferences,
) : ViewModel() {


    fun sendMessage(message: String, receiverId: String) {

        viewModelScope.launch {

            val messageMap = HashMap<String, Any>()
            messageMap[Constant.KEY_SENDER_ID] =
                pref.getString(Constant.KEY_USER_ID, null).toString()
            messageMap[Constant.KEY_RECEIVER_ID] = receiverId
            messageMap[Constant.KEY_MESSAGE] = message
            messageMap[Constant.KEY_TIMESTAMP] = Date()

            repository.sendMessage(messageMap)

        }

    }


    fun eventListener(receiverId: String,chatObserver: ChatFragment.ChatObserver) {
        val newMessageList = mutableListOf<ChatMessage>()
        val eventListener = EventListener<QuerySnapshot> { value, error ->
            if (error != null) {
                return@EventListener
            }
            if (value != null) {
                for (documentChange in value.documentChanges) {
                    if (documentChange.type == DocumentChange.Type.ADDED) {
                        val chatMessage = ChatMessage(
                            documentChange.document[Constant.KEY_SENDER_ID].toString(),
                            documentChange.document[Constant.KEY_RECEIVER_ID].toString(),
                            documentChange.document[Constant.KEY_MESSAGE].toString(),
                            documentChange.document.getDate(Constant.KEY_TIMESTAMP)!!
                                .getReadableDate(),
                            documentChange.document.getDate(Constant.KEY_TIMESTAMP)!!
                        )
                        newMessageList.add(chatMessage)
                    }
                }
                chatObserver.observeChat(newMessageList)
                newMessageList.clear()
            }
        }

        repository.observeChat(pref.getString(Constant.KEY_USER_ID, null).toString(), receiverId, eventListener)
    }

}