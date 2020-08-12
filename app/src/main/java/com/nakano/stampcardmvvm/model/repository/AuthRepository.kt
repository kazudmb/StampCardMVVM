package com.nakano.stampcardmvvm.model.repository

import android.content.Context
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

    // TODO: ReturnをResponse?result?にして、suceessの場合はview側でnavigationする、failedの場合はtoastの表示
    suspend fun signInWithGoogle(googleAuthCredential: AuthCredential?): Boolean {
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
                return createUserInFirestoreIfNotExists2(user)
            } else {
                return true
            }
        } catch (e: Exception) {
            return false
        }
    }

    private suspend fun createUserInFirestoreIfNotExists2(authenticatedUser: UserFirebase): Boolean {
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