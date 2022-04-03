package com.vivek.chatingapp.ui.registration.signup

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vivek.chatingapp.repository.RegistrationRepository
import com.vivek.chatingapp.utils.Constant
import com.vivek.chatingapp.utils.Resource
import com.vivek.chatingapp.utils.putAny
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val registrationRepository: RegistrationRepository, private val pref: SharedPreferences) :
    ViewModel() {

    private val _signUp = MutableLiveData<Resource<Boolean>>()
    val signUp: LiveData<Resource<Boolean>> = _signUp


    fun signUp(name: String, email: String, password: String, image: String) {
        viewModelScope.launch {
            _signUp.postValue(Resource.Loading())

            val userData = HashMap<String, Any>()
            userData[Constant.KEY_NAME] = name
            userData[Constant.KEY_EMAIL] = email
            userData[Constant.KEY_PASSWORD] = password
            userData[Constant.KEY_IMAGE] = image

            when (val documentReference = registrationRepository.userSignUp(userData)) {
                is Resource.Success -> {
                    documentReference.data?.let {
                        pref.putAny(Constant.KEY_IS_SIGNED_IN, true)
                        pref.putAny(Constant.KEY_USER_ID, it.id)
                        pref.putAny(Constant.KEY_NAME, name)
                        pref.putAny(Constant.KEY_EMAIL, email)
                        pref.putAny(Constant.KEY_IMAGE, image)
                        _signUp.postValue(Resource.Success(true))
                    }
                }
                is Resource.Error -> {
                    _signUp.postValue(documentReference.message?.let { Resource.Error(it) })
                }
            }

        }
    }

}