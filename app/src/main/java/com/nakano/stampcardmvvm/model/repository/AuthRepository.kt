package com.nakano.stampcardmvvm.model.repository

import android.content.Context
import android.util.Log
import androidx.constraintlayout.widget.Constraints.TAG
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.nakano.stampcardmvvm.model.model.User
import com.nakano.stampcardmvvm.util.Utility
import com.nakano.stampcardmvvm.view.MainActivity
import kotlinx.coroutines.tasks.await

class AuthRepository(
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
                val numberOfVisits = 0.toLong()
                val user = User(
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

    suspend fun signInWithTwitter(): LiveData<Boolean> {

        val isSuccess = MutableLiveData<Boolean>()

        isSuccess.value = checkPendingTask()

        return isSuccess
    }

    private suspend fun checkPendingTask(): Boolean {
        val pendingResultTask = firebaseAuth.pendingAuthResult

        return if (pendingResultTask != null) {
            false
        } else {
            signInWithProviderTwitter()
        }
    }

    private suspend fun signInWithProviderTwitter(): Boolean {
        val oauthProvider = OAuthProvider.newBuilder("twitter.com")
        oauthProvider.addCustomParameter("lang", "jp")

        // TODO: activityの取得方法を検討すること、何も表示しない空のActivityを作る？
        try {
            firebaseAuth
                .startActivityForSignInWithProvider(MainActivity(), oauthProvider.build())
                .await()
            Log.d(TAG, "signInWithProviderTwitter: success")

            val firebaseUser = firebaseAuth.currentUser
            return if (firebaseUser != null) {
                try {
                    firebaseUser
                        .startActivityForLinkWithProvider(MainActivity(), oauthProvider.build())
                        .await()
                    Log.d(TAG, "linkWithProviderTwitter: success")
                    true
                } catch (e: Exception) {
                    Log.w(TAG, "linkWithProviderTwitter: failure", e)
                    false
                }
            } else {
                false
            }

        } catch (e: Exception) {
            Log.w(TAG, "signInWithProviderTwitter: failure", e)
            return false
        }
    }

    // TODO: High メールログイン時の新規登録処理の実装をすること

    suspend fun signInWithEmailAndPassword(email: String, password: String): LiveData<Boolean> {
        val isSuccess = MutableLiveData<Boolean>()

        return try {
            firebaseAuth
                .signInWithEmailAndPassword(email, password)
                .await()
            Log.d(TAG, "signInWithEmailAndPassword:success")
            isSuccess.value = true
            isSuccess
        } catch (e: Exception) {
            Log.w(TAG, "signInWithEmailAndPassword:failure", e)
            isSuccess.value = false
            isSuccess
        }
    }

    suspend fun signInAnonymous(): LiveData<Boolean> {
        val isSuccess = MutableLiveData<Boolean>()

        try {
            val data = firebaseAuth
                .signInAnonymously()
                .await()

            Log.d(TAG, "signInAnonymously:success")

            val isNewUser = data.additionalUserInfo?.isNewUser
            val firebaseUser = firebaseAuth.currentUser

            if (isNewUser == true && firebaseUser != null) {
                val uid = firebaseUser.uid
                val name = "anonymous"
                val email = "not set"
                val numberOfVisits = 88.toLong() // sample number
                val user = User(
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
            Log.w(TAG, "signInAnonymously:failure", e)
            isSuccess.value = false
            return isSuccess
        }
    }

    private suspend fun createUserInFirestoreIfNotExists(authenticatedUser: User): Boolean {
        try {
            val data =
                usersRef
                    .document(authenticatedUser.uid)
                    .get()
                    .await()
            if (!data.exists()) {
                return try {
                    usersRef
                        .document(authenticatedUser.uid)
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