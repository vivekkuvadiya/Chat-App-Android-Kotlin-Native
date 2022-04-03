package com.vivek.chatingapp.repository

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.vivek.chatingapp.utils.Constant
import com.vivek.chatingapp.utils.Resource
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.Exception

class RegistrationRepository @Inject constructor(private val fireStore: FirebaseFirestore) {


    suspend fun userSignUp(userData: HashMap<String, Any>):Resource<DocumentReference> {
        return try {
            val await = fireStore.collection(Constant.KEY_COLLECTION_USERS)
                .add(userData)
                .await()
            Resource.Success(await)
        }catch (e:Exception){
            Resource.Error(e.message?:"An Unknown Error Occurred")
        }
    }


    suspend fun userSignIn(email:String,password:String):Resource<QuerySnapshot>{
        return try {
            val await = fireStore.collection(Constant.KEY_COLLECTION_USERS)
                .whereEqualTo(Constant.KEY_EMAIL, email)
                .whereEqualTo(Constant.KEY_PASSWORD, password)
                .get()
                .await()
            if (await.isEmpty){
                Resource.Error("User Not Found")
            }else{
                Resource.Success(await)
            }

        }catch (e:Exception){
            Resource.Error(e.message?:"An Unknown Error Occurred")
        }
    }


}