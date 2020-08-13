package com.nakano.stampcardmvvm.model.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nakano.stampcardmvvm.model.model.AppDatabase
import com.nakano.stampcardmvvm.model.model.UserFirebase
import com.nakano.stampcardmvvm.util.Utility
import kotlinx.coroutines.tasks.await

class AuthRepository(
    private val db: AppDatabase,
    private val context: Context
) {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val rootRef = FirebaseFirestore.getInstance()
    private val usersRef = rootRef.collection("users")

    suspend fun signInWithGoogle(googleAuthCredential: AuthCredential?): LiveData<Boolean> {

        val isSuccess = MutableLiveData<Boolean>()

        try {
            val data = firebaseAuth
                .signInWithCredential(googleAuthCredential!!)
                .await()

            val isNewUser = data.additionalUserInfo?.isNewUser
            val firebaseUser = firebaseAuth.currentUser

            if (isNewUser == true && firebaseUser != null) {
                val uid = firebaseUser.uid
                val name = firebaseUser.displayName
                val email = firebaseUser.email
                val numberOfVisits = "0"
                val user = UserFirebase(
                    uid,
                    name,
                    email,
                    numberOfVisits,
                    Utility.getRank(context, numberOfVisits)
                )
                isSuccess.value = createUserInFirestoreIfNotExists(user)
                return isSuccess
            } else {
                isSuccess.value = true
                return isSuccess
            }
        } catch (e: Exception) {
            isSuccess.value = false
            return isSuccess
        }
    }

    private suspend fun createUserInFirestoreIfNotExists(authenticatedUser: UserFirebase): Boolean {
        try {
            val data =
                usersRef
                    .document(authenticatedUser.uid!!)
                    .get()
                    .await()
            if (!data.exists()) {
                return try {
                    usersRef
                        .document(authenticatedUser.uid!!)
                        .set(authenticatedUser)
                        .await()
                    true
                } catch (e: Exception) {
                    false
                }
            }
            return false
        } catch (e: Exception) {
            return false
        }
    }
}