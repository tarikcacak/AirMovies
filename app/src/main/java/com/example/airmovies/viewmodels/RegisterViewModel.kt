package com.example.airmovies.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.airmovies.data.user.User
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
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
): ViewModel() {
    private val _register = MutableStateFlow<Resource<User>>(Resource.Unspecified())
    val register : Flow<Resource<User>> = _register

    private val _validation  = Channel<RegisterFieldState>()
    val validation = _validation.receiveAsFlow()

    fun createAccountWithEmailAndPassword(user: User) = viewModelScope.launch{

        if (checkValidation(user)) {
            _register.emit(Resource.Loading())

            firebaseAuth.createUserWithEmailAndPassword(user.email, user.password)
                .addOnSuccessListener {
                    it.user?.let {
                        saveUserInfo(it.uid,user)
                    }
                }.addOnFailureListener {

                    _register.value = Resource.Error(it.message.toString())
                }
        }else{

            val registerFieldsState = RegisterFieldState(
                validateEmail(user.email),
                validatePassword(user.password),
                validateUser(user.username)
            )

            _validation.send(registerFieldsState)


        }
    }

    private fun saveUserInfo(userUid: String, user: User) {

        val hashMap = hashMapOf<String, Any>()
        hashMap["email"] = user.email
        hashMap["userName"] = user.username
        hashMap["imagePath"] = user.imgPath
        hashMap["uid"] = userUid
        hashMap["password"] = user.password

        firestore.collection("user")
            .document(userUid)
            .set(hashMap)
            .addOnSuccessListener {
                _register.value = Resource.Success(user)
            }
            .addOnFailureListener{
                _register.value = Resource.Error(it.message.toString())
            }


    }

    private fun checkValidation(user: User): Boolean {
        val emailValidaiton = validateEmail(user.email)
        val passwordValidation = validatePassword(user.password)
        val userValidation = validateUser(user.username)
        val shouldRegister = emailValidaiton is RegisterValidation.Success &&
                passwordValidation is RegisterValidation.Success && userValidation is RegisterValidation.Success

        return shouldRegister
    }

}