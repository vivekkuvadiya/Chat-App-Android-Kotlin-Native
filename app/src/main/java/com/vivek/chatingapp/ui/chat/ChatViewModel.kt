package com.vivek.chatingapp.ui.chat

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import com.vivek.chatingapp.model.ChatMessage
import com.vivek.chatingapp.model.Data
import com.vivek.chatingapp.model.MessageBody
import com.vivek.chatingapp.model.User
import com.vivek.chatingapp.repository.MainRepository
import com.vivek.chatingapp.utils.Constant
import com.vivek.chatingapp.utils.encodeBase64
import com.vivek.chatingapp.utils.getReadableDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: MainRepository,
    private val pref: SharedPreferences,
) : ViewModel() {

    var conversionId = ""
    private var isReceiverAvailable = false

    fun sendMessage(message: String, receiverUser: User) {

        viewModelScope.launch {

            val messageMap = HashMap<String, Any>()
            messageMap[Constant.KEY_SENDER_ID] =
                pref.getString(Constant.KEY_USER_ID, null).toString()
            messageMap[Constant.KEY_RECEIVER_ID] = receiverUser.id
            messageMap[Constant.KEY_MESSAGE] = message
            messageMap[Constant.KEY_TIMESTAMP] = Date()

            repository.sendMessage(messageMap)

            if (conversionId.isNotEmpty()){
                repository.updateConversation(message,conversionId)
            }else{
                val conversation = HashMap<String,Any>().apply {
                    put(Constant.KEY_SENDER_ID,pref.getString(Constant.KEY_USER_ID, null).toString())
                    put(Constant.KEY_SENDER_NAME,pref.getString(Constant.KEY_NAME,null).toString())
                    put(Constant.KEY_SENDER_IMAGE,pref.getString(Constant.KEY_IMAGE,null).toString())
                    put(Constant.KEY_RECEIVER_ID,receiverUser.id)
                    put(Constant.KEY_RECEIVER_NAME,receiverUser.name)
                    put(Constant.KEY_RECEIVER_IMAGE,receiverUser.image!!)
                    put(Constant.KEY_LAST_MESSAGE,message)
                    put(Constant.KEY_TIMESTAMP,Date())
                }
                repository.updateRecentConversation(conversation){
                    conversionId = it
                }
            }
            if (!isReceiverAvailable){
                try {

                    val messageBody = MessageBody(
                        data = Data(
                            userId = pref.getString(Constant.KEY_USER_ID,null).toString(),
                            name = pref.getString(Constant.KEY_NAME,null).toString(),
                            fcmToken = pref.getString(Constant.KEY_FCM_TOKEN,null).toString(),
                            message = message
                        ),
                        regIs = listOf(receiverUser.token!!)
                    )
                    sendNotification(messageBody)
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
        }

    }


    fun eventListener(receiverId: String, chatObserver: ChatFragment.ChatObserver) {
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

        repository.observeChat(pref.getString(Constant.KEY_USER_ID, null).toString(),
            receiverId,
            eventListener)
    }


    fun checkForConversation(receiverUserId: String) = viewModelScope.launch  {
        repository.checkForConversionRemotely(
            pref.getString(Constant.KEY_USER_ID, null).toString(),
            receiverUserId,
            conversionOnCompleteListener()
        )
        repository.checkForConversionRemotely(
            receiverUserId,
            pref.getString(Constant.KEY_USER_ID, null).toString(),
            conversionOnCompleteListener()
        )
    }

    private fun conversionOnCompleteListener() = OnCompleteListener<QuerySnapshot> { task ->
        if (task.isSuccessful && task.result != null && task.result?.documents?.size!! > 0) {
            val documentSnapshot = task.result?.documents?.get(0)
            conversionId = documentSnapshot?.id.toString()
        }

    }

    fun listenerAvailabilityOfReceiver(receiverId: String, availability:(Boolean,String,String)->Unit){
        repository.listenerAvailabilityOfReceiver(receiverId
        ) { value, error ->

            var fcm = ""
            var profileImage = ""
            if (error != null)
                return@listenerAvailabilityOfReceiver
            if (value != null){
                if (value.getLong(Constant.KEY_AVAILABILITY) != null){
                    val availability = Objects.requireNonNull(value.getLong(Constant.KEY_AVAILABILITY))?.toInt()
                    isReceiverAvailable = availability == 1

                }
                fcm = value.getString(Constant.KEY_FCM_TOKEN).toString()
                profileImage = value.getString(Constant.KEY_IMAGE).toString()
            }
            availability(isReceiverAvailable,fcm,profileImage)
        }
    }

    fun sendNotification(messageBody: MessageBody) = viewModelScope.launch {
        val response = repository.sendNotification(messageBody)
        if (response.isSuccessful){
        }
    }


}