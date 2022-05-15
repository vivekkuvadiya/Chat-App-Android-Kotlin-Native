package com.vivek.chatingapp.repository

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.messaging.FirebaseMessaging
import com.vivek.chatingapp.utils.Constant
import com.vivek.chatingapp.utils.Resource
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MainRepository @Inject constructor(private val fireStore: FirebaseFirestore, private val fireMessage:FirebaseMessaging) {

    suspend fun updateToken(token:String,userId:String):Boolean{
        return try {
            val documentReference = fireStore.collection(Constant.KEY_COLLECTION_USERS).document(userId)
            documentReference.update(Constant.KEY_FCM_TOKEN, token).await()
            true
        }catch (e:Exception){
            false
        }
    }

    suspend fun getToken():String{
        return fireMessage.token.await()
    }

    suspend fun userSignOut(userId: String, userData:HashMap<String,Any>):Boolean{
        return try {
            val documentReference  = fireStore.collection(Constant.KEY_COLLECTION_USERS)
                .document(userId)
            documentReference.update(userData).await()
            true
        }catch (e:Exception){
            false
        }
    }

    suspend fun getAllUsers():Resource<QuerySnapshot>{
        try {
            val await = fireStore.collection(Constant.KEY_COLLECTION_USERS)
                .get()
                .await()
            if (await.isEmpty){
                return Resource.Empty("No User Available")
            }
            return Resource.Success(await)
        }catch (e:Exception){
            return Resource.Error(e.message?:"An Unknown Error Occurred")
        }
    }

    suspend fun sendMessage(message:HashMap<String,Any>):Boolean = try {
        fireStore.collection(Constant.KEY_COLLECTION_CHAT).document().set(message).await()
        true
    }catch (e:Exception){
        false
    }


    fun observeChat(userId: String, receiverId:String,listener:EventListener<QuerySnapshot>){
        fireStore.collection(Constant.KEY_COLLECTION_CHAT)
            .whereEqualTo(Constant.KEY_SENDER_ID,userId)
            .whereEqualTo(Constant.KEY_RECEIVER_ID,receiverId)
            .addSnapshotListener(listener)
        fireStore.collection(Constant.KEY_COLLECTION_CHAT)
            .whereEqualTo(Constant.KEY_SENDER_ID,receiverId)
            .whereEqualTo(Constant.KEY_RECEIVER_ID,userId)
            .addSnapshotListener(listener)
    }

}