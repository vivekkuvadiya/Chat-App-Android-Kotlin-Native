package com.vivek.chatingapp.ui.registration.signup

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.vivek.chatingapp.ui.main.MainActivity
import com.vivek.chatingapp.R
import com.vivek.chatingapp.databinding.SignUpFragmentBinding
import com.vivek.chatingapp.utils.*
import dagger.hilt.android.AndroidEntryPoint
import java.io.FileNotFoundException


@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.sign_up_fragment) {

    private val viewModel: SignUpViewModel by viewModels()
    lateinit var binding: SignUpFragmentBinding
    private var encodedImage: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = SignUpFragmentBinding.bind(view)

        setClickListener()

        setObserver()

    }

    private fun setObserver() {

        viewModel.signUp.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    loading(false)
                    requireContext().toast("Registered Successfully!")
                    val intent = Intent(requireActivity(), MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
                is Resource.Error -> {
                    loading(false)
                    resource.message?.let {
                        requireContext().toast(it)
                    }
                }
                is Resource.Loading -> {
                    loading(true)
                }
            }
        }

    }

    private fun setClickListener() {
        binding.tvSignIn.setOnClickListener { findNavController().popBackStack() }

        binding.btnSignUp.setOnClickListener {
            if (isValidSignUpDetails()) {
                signUp()
            }
        }

        binding.ivProfile.setOnClickListener {
            val pickImageIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
            pickImage.launch(pickImageIntent)
        }
    }

    private fun signUp() {
        viewModel.signUp(
            binding.etName.getFiledText(),
            binding.etEmail.getFiledText(),
            binding.etPassword.getFiledText(),
            encodedImage!!
        )
    }

    private val pickImage: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { it ->
        if (it.resultCode == RESULT_OK) {
            it.data?.let {
                val imageUri = it.data
                imageUri?.let {
                    try {
                        val inputStream = requireContext().contentResolver.openInputStream(imageUri)
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        binding.ivProfile.setImageBitmap(bitmap)
                        binding.tvAddImage.gone()
                        encodedImage = bitmap.encodeToBase64()
                    } catch (e: FileNotFoundException) {
                        requireContext().toast("Failed to fetch image")
                    }
                }
            }
        }
    }

    private fun isValidSignUpDetails(): Boolean {
        if (encodedImage == null) {
            requireContext().toast("Please Select Profile Picture")
            return false
        }
        if (binding.etName.isFiledIsEmpty()) {
            requireContext().toast("Please Enter Valid Name")
            return false
        }
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
        if (binding.etConfirmPassword.isFiledIsEmpty()) {
            requireContext().toast("Please Enter Valid Confirm Password")
            return false
        }
        if (!binding.etPassword.getFiledText()
                .isPasswordIsSame(binding.etConfirmPassword.getFiledText())
        ) {
            requireContext().toast("Password is not matches")
            return false
        }
        return true
    }

    private fun loading(isLoading: Boolean) {
        if (isLoading) {
            binding.btnSignUp.invisible()
            binding.pbSignUp.visible()
        } else {
            binding.btnSignUp.visible()
            binding.pbSignUp.gone()
        }
    }

}