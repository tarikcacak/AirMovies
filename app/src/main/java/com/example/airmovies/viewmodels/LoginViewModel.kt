package com.example.airmovies.viewmodels

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
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {
    private val _login = MutableStateFlow<Resource<FirebaseUser>>(Resource.Unspecified())
    val login = _login.asSharedFlow()

    private val _validation  = Channel<RegisterFieldState>()
    val validation = _validation.receiveAsFlow()

    fun login(user: User) {
        if (checkValidation(user)) {
            firebaseAuth.signInWithEmailAndPassword(user.email, user.password)
                .addOnSuccessListener {
                    viewModelScope.launch {
                        it.user?.let {
                            _login.emit(Resource.Success(it))
                        }
                    }
                }
                .addOnFailureListener {
                    viewModelScope.launch {
                        _login.emit(Resource.Error(it.message.toString()))
                    }
                }

        } else {
            val registerFieldState = RegisterFieldState(
                validateEmail(user.email),
                validatePassword(user.password),
                validateUser(user.username)
            )

            _validation.send(registerFieldState)
        }
    }

    private fun checkValidation(user: User): Boolean {
        val emailValidaiton = validateEmail(user.email)
        val passwordValidation = validatePassword(user.password)
        val shouldRegister = emailValidaiton is RegisterValidation.Success &&
                passwordValidation is RegisterValidation.Success

        return shouldRegister
    }
}