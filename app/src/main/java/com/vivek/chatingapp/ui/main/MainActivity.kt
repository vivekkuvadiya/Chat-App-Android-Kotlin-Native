package com.vivek.chatingapp.ui.main

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.vivek.chatingapp.R
import com.vivek.chatingapp.utils.Constant
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var pref: SharedPreferences
    @Inject
    lateinit var fireStore: FirebaseFirestore

    lateinit var documentReference:DocumentReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        documentReference = fireStore.collection(Constant.KEY_COLLECTION_USERS)
            .document(pref.getString(Constant.KEY_USER_ID,null).toString())

    }

    override fun onPause() {
        super.onPause()
        documentReference.update(Constant.KEY_AVAILABILITY,0)
    }

    override fun onResume() {
        super.onResume()
        documentReference.update(Constant.KEY_AVAILABILITY,1)
    }


}