package com.vivek.chatingapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.vivek.chatingapp.R
import com.vivek.chatingapp.databinding.MainFragmentBinding
import com.vivek.chatingapp.ui.registration.RegistrationActivity
import com.vivek.chatingapp.utils.decodeToBitmap
import com.vivek.chatingapp.utils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.main_fragment) {

    private lateinit var binding:MainFragmentBinding
    private val viewModel:MainViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = MainFragmentBinding.bind(view)

        clickListener()
        updateDetails()


    }

    private fun updateDetails(){
        binding.ivProfile.setImageBitmap(viewModel.loadUserDetails().decodeToBitmap())
        binding.tvName.text = viewModel.getName()
    }

    private fun clickListener(){
        binding.ivSignOut.setOnClickListener { signOut() }
        binding.fabNewChat.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_usersFragment)
        }
    }

    private fun signOut(){

        viewModel.signOut().observe(viewLifecycleOwner){
            if (it){
                requireContext().toast("SignOut")
                val intent = Intent(requireActivity(), RegistrationActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)

            }else{
                requireContext().toast("Unable to signOut")
            }
        }



    }

}