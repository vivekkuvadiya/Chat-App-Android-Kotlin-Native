package com.vivek.chatingapp.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.vivek.chatingapp.utils.Constant
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

}