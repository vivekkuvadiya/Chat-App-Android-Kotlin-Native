package com.vivek.chatingapp.ui.chat

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.vivek.chatingapp.R
import com.vivek.chatingapp.adapter.ChatAdapter
import com.vivek.chatingapp.databinding.ChatFragmentBinding
import com.vivek.chatingapp.model.User
import com.vivek.chatingapp.utils.Constant
import com.vivek.chatingapp.utils.decodeToBitmap
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChatFragment : Fragment(R.layout.chat_fragment) {

    private lateinit var binding: ChatFragmentBinding
    private val viewModel: ChatViewModel by viewModels()
    private lateinit var user: User
    private lateinit var chatAdapter: ChatAdapter

    @Inject
    lateinit var pref: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ChatFragmentBinding.bind(view)

        getArgument()

        setClickListener()

        setRecyclerview()

        binding.tvName.text = user.name


    }

    private fun getArgument() {
        arguments?.let {
            user = ChatFragmentArgs.fromBundle(it).user
        }
    }

    private fun setClickListener() {
        binding.ivBack.setOnClickListener { findNavController().popBackStack() }
        binding.ivSend.setOnClickListener {
            if (binding.etMessage.text.isNullOrBlank() && binding.etMessage.text.toString().trim().length<0)
                return@setOnClickListener
            viewModel.sendMessage(binding.etMessage.text.trim().toString(),user.id)
            binding.etMessage.text.clear()
        }
    }

    private fun setRecyclerview() {
        chatAdapter = ChatAdapter(pref.getString(Constant.KEY_USER_ID, null).toString(),
            pref.getString(Constant.KEY_IMAGE, null).toString().decodeToBitmap(),
            emptyList())
        binding.rvChat.apply {
            adapter = chatAdapter
        }
    }
}