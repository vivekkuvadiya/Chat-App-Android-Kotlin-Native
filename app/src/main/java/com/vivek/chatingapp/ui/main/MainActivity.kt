package com.vivek.chatingapp.ui.main

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
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

    private lateinit var navHostFragment: NavHostFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment

        navigateToChatFragmentIfNeeded()

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

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navigateToChatFragmentIfNeeded()
    }

    private fun navigateToChatFragmentIfNeeded(){
        if (intent?.action == Constant.ACTION_SHOW_CHAT_FRAGMENT){
            val bundle = Bundle()
            bundle.putSerializable(Constant.KEY_USER, intent?.getSerializableExtra(Constant.KEY_USER))
            navHostFragment.findNavController().navigate(R.id.actionChatFragment,bundle)
        }
    }

}