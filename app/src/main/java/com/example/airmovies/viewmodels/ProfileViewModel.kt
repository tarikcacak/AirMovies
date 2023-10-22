package com.example.airmovies.viewmodels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.airmovies.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.nio.channels.Channel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage
) : ViewModel() {

    private val usernameState = MutableLiveData<String>()
    private val emailState = MutableLiveData<String>()
    private val imageState = MutableLiveData<String>()
    private val loadingState = MutableLiveData<Boolean>()
    private val errorState = MutableLiveData<String>()

    private val _loadingStateEdit = MutableLiveData<Boolean>()
    val loadingStateEdit = _loadingStateEdit
    private val _errorStateEdit = MutableLiveData<String>()
    val errorStateEdit = _errorStateEdit

    private val _resetPassword = MutableSharedFlow<Resource<String>>()
    val resetPassword = _resetPassword.asSharedFlow()

    val currentUid = firebaseAuth.currentUser?.uid.toString()
    val user = firebaseAuth.currentUser

    fun getData() {

        loadingState.value = true

        firestore.collection("user").whereEqualTo("uid", currentUid)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    loadingState.value = false
                    errorState.value = exception.localizedMessage
                } else {
                    if (!snapshot!!.isEmpty) {
                        val documentList = snapshot.documents

                        for (document in documentList) {
                            val email = document.get("email") as String
                            val user = document.get("userName") as String
                            val downloadUri = document.get("imagePath") as String
                            usernameState.value = user
                            imageState.value = downloadUri
                            emailState.value = email
                        }
                    }
                }
            }
    }

    fun saveImage(pickedImage: Uri) {
        firestore.collection("user").document(currentUid)
            .update(
                hashMapOf<String, Any>(
                    "imagePath" to pickedImage
                )
            )
            .addOnSuccessListener {
                Log.d("imgUpdate", "Image successfully updated")
            }
            .addOnFailureListener { e ->
                Log.e("imgUpdate", e.message.toString())
            }
    }

    fun resetPassword(email: String) {
        viewModelScope.launch {
            _resetPassword.emit(Resource.Loading())
        }
        firebaseAuth
            .sendPasswordResetEmail(email)
            .addOnSuccessListener {
                viewModelScope.launch {
                    _resetPassword.emit(Resource.Success(email))
                }
            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _resetPassword.emit(Resource.Success(it.message.toString()))
                }
            }
    }

    fun logOut(){
        firebaseAuth.signOut()
    }

    fun observeUsernameLiveData(): LiveData<String> {
        return usernameState
    }

    fun observeEmailLiveData(): LiveData<String> {
        return emailState
    }

    fun observeImageLiveData(): LiveData<String> {
        return imageState
    }

    fun loadingStateLiveData(): LiveData<Boolean> {
        return loadingState
    }

    fun errorStateLiveData(): LiveData<String> {
        return errorState
    }
}