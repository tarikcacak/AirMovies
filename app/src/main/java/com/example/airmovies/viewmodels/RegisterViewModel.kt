package com.example.airmovies.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.airmovies.data.User
import com.example.airmovies.util.RegisterFieldState
import com.example.airmovies.util.RegisterValidation
import com.example.airmovies.util.Resource
import com.example.airmovies.util.validateEmail
import com.example.airmovies.util.validatePassword
import com.example.airmovies.util.validateUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModel() {
    private val _register = MutableStateFlow<Resource<User>>(Resource.Unspecified())
    val register: Flow<Resource<User>> = _register

    private val _validation  = Channel<RegisterFieldState>()
    val validation = _validation.receiveAsFlow()

    fun createAccountWithEmailAndPassword(user: User, password: String) = viewModelScope.launch {
        if (checkValidation(user.email, password, user.username)) {
            _register.emit(Resource.Loading())

            firebaseAuth.createUserWithEmailAndPassword(user.email, password)
                .addOnSuccessListener {
                    it.user?.let {
                        saveUserInfo(it.uid, user)
                    }
                }
                .addOnFailureListener { exception ->
                    _register.value = Resource.Error(exception.message.toString())
                }
        } else {
            val registerFieldState = RegisterFieldState(
                validateEmail(user.email), validatePassword(password), validateUser(user.username)
            )
            _validation.send(registerFieldState)
        }
    }

    private fun saveUserInfo(userUid: String, user: User) {
        db.collection("USER_INFO_COLLECTION")
            .document(userUid)
            .set(user)
            .addOnSuccessListener {
                _register.value = Resource.Success(user)
            }
            .addOnFailureListener {
                _register.value = Resource.Error(it.message.toString())
            }
    }

    private fun checkValidation(email: String, password: String, username: String): Boolean {
        val emailValidation = validateEmail(email)
        val passwordValidation = validatePassword(password)
        val userValidation = validateUser(username)
        val shouldRegister = emailValidation is RegisterValidation.Success &&
                passwordValidation is RegisterValidation.Success && userValidation is RegisterValidation.Success

        return shouldRegister
    }
}