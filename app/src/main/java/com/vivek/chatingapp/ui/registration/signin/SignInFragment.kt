package com.vivek.chatingapp.ui.registration.signin

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.vivek.chatingapp.ui.main.MainActivity
import com.vivek.chatingapp.R
import com.vivek.chatingapp.databinding.SignInFragmentBinding
import com.vivek.chatingapp.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : Fragment(R.layout.sign_in_fragment) {


    private val viewModel: SignInViewModel by viewModels()
    private lateinit var binding: SignInFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = SignInFragmentBinding.bind(view)

        setClickListener()

        setObserver()


    }

    private fun setObserver() {
        viewModel.signIn.observe(viewLifecycleOwner){ it ->
            when(it){
                is Resource.Success -> {
                    loading(false)
                    requireContext().toast("Sign In Successfully!")
                    viewModel.clearSignInData()
                    val intent = Intent(requireActivity(), MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
                is Resource.Error -> {
                    loading(false)
                    it.message?.let {
                        requireContext().toast(it)
                    }
                    viewModel.clearSignInData()
                }
                is Resource.Loading -> {
                    loading(true)
                }
                is Resource.Empty -> {

                }
            }
        }
    }


    private fun setClickListener(){

        binding.tvCreateNewAccount.setOnClickListener { findNavController().navigate(R.id.action_signInFragment_to_signUpFragment) }

        binding.btnSignIn.setOnClickListener {
            if (isValidSignInDetails()){
                signIn()
            }
        }
    }


    private fun isValidSignInDetails(): Boolean {
        if (binding.etEmail.isFiledIsEmpty()) {
            requireContext().toast("Please Enter Email")
            return false
        }
        if (!binding.etEmail.getFiledText().isValidEmail()) {
            requireContext().toast("Please Enter Valid Email")
            return false
        }
        if (binding.etPassword.isFiledIsEmpty()) {
            requireContext().toast("Please Enter Password")
            return false
        }
        return true
    }

    private fun signIn(){
        viewModel.signIn(binding.etEmail.getFiledText(),binding.etPassword.getFiledText())
    }

    private fun loading(isLoading: Boolean) {
        if (isLoading) {
            binding.btnSignIn.invisible()
            binding.pbSignIn.visible()
        } else {
            binding.btnSignIn.visible()
            binding.pbSignIn.gone()
        }
    }

}