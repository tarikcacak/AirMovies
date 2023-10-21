package com.example.airmovies.viewmodels

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import java.nio.channels.Channel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage
) : ViewModel() {

    private val usernameState = MutableLiveData<String>()
    private val passwordState = MutableLiveData<String>()
    private val emailState = MutableLiveData<String>()
    private val imageState = MutableLiveData<String>()
    private val loadingState = MutableLiveData<Boolean>()
    private val errorState = MutableLiveData<String>()

    private val _loadingStateEdit = MutableLiveData<Boolean>()
    val loadingStateEdit = _loadingStateEdit
    private val _errorStateEdit = MutableLiveData<String>()
    val errorStateEdit = _errorStateEdit

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
                            val password = document.get("password") as String
                            usernameState.value = user
                            imageState.value = downloadUri
                            emailState.value = email
                            passwordState.value = password
                        }
                    }
                }
            }
    }

    fun saveImage(pickedImage: Uri) {
        firebaseStorage.reference.child("imagePath").child(currentUid).delete()

        val storage = firebaseStorage.reference.child("imagePath").child(currentUid)
        storage.putFile(pickedImage)
        storage.downloadUrl.addOnSuccessListener { uri ->
            val imgUrl = uri.toString()

            firestore.collection("user").document(currentUid)
                .update(
                    mapOf(
                        "imagePath" to imgUrl
                    )
                ).addOnCompleteListener { _loadingStateEdit.value = false }
                .addOnFailureListener { error ->
                    _loadingStateEdit.value = false
                    _errorStateEdit.value = error.localizedMessage
                }
        }
    }

    fun changePassword

    fun logOut(){
        firebaseAuth.signOut()
    }
}