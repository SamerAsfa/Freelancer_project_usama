package com.example.myapplication.myapplication.data

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*


class UserDataProvider(private val db : FirebaseFirestore , private val firebaseStorage : FirebaseStorage) {


    fun getUserData(): Task<QuerySnapshot> {
        return db.collection("UserObject").get()
    }

    fun uploadUserImageData(): StorageReference {
        return firebaseStorage.reference.child("images/${UUID.randomUUID()}")
    }

    fun addUserData(name: String, url: String,deviceId : String): Task<DocumentReference> {
        val user = hashMapOf(
            "id" to deviceId,
            "name" to name,
            "profile_pic_url" to url
        )
        return db.collection("UserObject").add(user)
    }

}