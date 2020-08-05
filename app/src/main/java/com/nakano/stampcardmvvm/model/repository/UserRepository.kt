package com.nakano.stampcardmvvm.model.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nakano.stampcardmvvm.model.model.AppDatabase
import com.nakano.stampcardmvvm.model.model.UserFirebase

class UserRepository(
    private val db: AppDatabase
) {
    private val firebaseAuth = FirebaseAuth.getInstance()

    private val rootRef = FirebaseFirestore.getInstance()
    private val usersRef = rootRef.collection("users")

    fun getUser(): LiveData<UserFirebase> {

        val userMutableLiveData = MutableLiveData<UserFirebase>()

        val firebaseUser = firebaseAuth.currentUser

        if (firebaseUser != null) {
            val uid = firebaseUser.uid
            val name = firebaseUser.displayName
            val email = firebaseUser.email
            val user = UserFirebase(uid, name, email)
            userMutableLiveData.setValue(user)
        } else {

            // TODO: ユーザ情報を取得していく処理の実装
//            usersRef
//                .get()
//                .addOnSuccessListener { result ->
//                    for (document in result) {
//
//                        val firebaseUser = firebaseAuth.currentUser
//                        if (firebaseUser != null) {
//                            val uid = firebaseUser.uid
//                            val name = firebaseUser.displayName
//                            val email = firebaseUser.email
//                            val user = UserFirebase(uid, name, email)
//                            user.isNew = isNewUser
//                            userMutableLiveData.setValue(user)
//                        }
//
//
////                    userMutableLiveData.setValue()
////                    Log.d(TAG, "${document.id} => ${document.data}")
//                    }
//                }
//                .addOnFailureListener { exception ->
////                Log.w(TAG, "Error getting documents.", exception)
//                }
        }

        return userMutableLiveData
    }
}
