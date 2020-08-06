package com.nakano.stampcardmvvm.model.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.nakano.stampcardmvvm.model.model.AppDatabase
import com.nakano.stampcardmvvm.model.model.UserFirebase
import com.nakano.stampcardmvvm.util.HelperClass
import com.nakano.stampcardmvvm.util.Utility

class AuthRepository(
    private val db: AppDatabase,
    private val context: Context
) {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val rootRef = FirebaseFirestore.getInstance()
    private val usersRef = rootRef.collection("users")

    fun firebaseSignInWithGoogle(googleAuthCredential: AuthCredential?): MutableLiveData<UserFirebase> {
        val authenticatedUserMutableLiveData =
            MutableLiveData<UserFirebase>()
        firebaseAuth.signInWithCredential(googleAuthCredential!!)
            .addOnCompleteListener { authTask: Task<AuthResult> ->
                if (authTask.isSuccessful) {
                    val isNewUser =
                        authTask.result!!.additionalUserInfo!!.isNewUser
                    val firebaseUser = firebaseAuth.currentUser
                    if (firebaseUser != null) {
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
                        user.isNew = isNewUser
                        authenticatedUserMutableLiveData.value = user
                    }
                } else {
                    HelperClass.logErrorMessage(authTask.exception!!.message)
                }
            }
        return authenticatedUserMutableLiveData
    }

    fun createUserInFirestoreIfNotExists(authenticatedUser: UserFirebase): MutableLiveData<UserFirebase> {
        val newUserMutableLiveData = MutableLiveData<UserFirebase>()
        val uidRef =
            usersRef.document(authenticatedUser.uid!!)
        uidRef.get()
            .addOnCompleteListener { uidTask: Task<DocumentSnapshot?> ->
                if (uidTask.isSuccessful) {
                    val document = uidTask.result
                    if (!document!!.exists()) {
                        uidRef.set(authenticatedUser)
                            .addOnCompleteListener { userCreationTask: Task<Void?> ->
                                if (userCreationTask.isSuccessful) {
                                    authenticatedUser.isCreated = true
                                    newUserMutableLiveData.setValue(authenticatedUser)
                                } else {
                                    HelperClass.logErrorMessage(userCreationTask.exception!!.message)
                                }
                            }
                    } else {
                        newUserMutableLiveData.setValue(authenticatedUser)
                    }
                } else {
                    HelperClass.logErrorMessage(uidTask.exception!!.message)
                }
            }
        return newUserMutableLiveData
    }
}