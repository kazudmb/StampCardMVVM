package com.nakano.stampcardmvvm.model.repository

import android.content.Context
import android.util.Log
import androidx.constraintlayout.widget.Constraints.TAG
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.nakano.stampcardmvvm.R
import com.nakano.stampcardmvvm.model.model.User
import com.nakano.stampcardmvvm.util.Constant
import com.nakano.stampcardmvvm.util.Utility
import com.nakano.stampcardmvvm.view.MainActivity
import kotlinx.coroutines.tasks.await

class AuthRepository(
    private val context: Context
) {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val rootRef = FirebaseFirestore.getInstance()
    private val usersRef = rootRef.collection("users")

    private val tmpEmail = MutableLiveData<String>()


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

    suspend fun createUserWithEmailAndPassword(email: String, password: String): LiveData<Boolean> {
        val isSuccess = MutableLiveData<Boolean>()

        return try {
            firebaseAuth
                .createUserWithEmailAndPassword(email, password)
                .await()
            Log.d(
                TAG,
                context.applicationContext.getString(R.string.create_user_with_email_success_log)
            )

            val firebaseUser = firebaseAuth.currentUser
            return if (firebaseUser != null) {
                val uid = firebaseUser.uid
                val name = null
                val email = firebaseUser.email
                val numberOfVisits = 0.toLong()
                val user = User(
                    uid, name, email, numberOfVisits, Utility.getRank(context, numberOfVisits)
                )
                isSuccess.value = createCollection(user)
                isSuccess
            } else {
                isSuccess.value = false
                isSuccess
            }
        } catch (e: Exception) {
            Log.w(
                TAG,
                context.applicationContext.getString(R.string.create_user_with_email_failure_log),
                e
            )
            isSuccess.value = false
            isSuccess
        }
    }

    private suspend fun createCollection(user: User): Boolean {
        try {
            usersRef
                .document(firebaseAuth.currentUser?.uid.toString())
                .set(user)
                .await()
            Log.d(
                TAG,
                "DocumentSnapshot added with ID: ${firebaseAuth.currentUser?.uid.toString()}"
            )
            return sendEmailVerification()
        } catch (e: Exception) {
            Log.w(
                TAG,
                context.applicationContext.getString(R.string.adding_document_failure_log),
                e
            )
            return false
        }
    }

    private suspend fun sendEmailVerification(): Boolean {
        val user = firebaseAuth.currentUser

        if (user != null) {
            try {
                user
                    .sendEmailVerification()
                    .await()

                Log.d(
                    TAG,
                    context.applicationContext.getString(R.string.send_email_verification_of_registration_success_log)
                )
                return true
            } catch (e: Exception) {
                Log.e(
                    TAG,
                    context.applicationContext.getString(R.string.send_email_verification_of_registration_failure_log),
                    e
                )
                return false
            }
        } else {
            return false
        }
    }

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

    suspend fun updateEmail(
        currentUserEmail: String,
        afterChangeEmail: String,
        password: String
    ): LiveData<Boolean> {
        val isSuccess = MutableLiveData<Boolean>()
        val user = firebaseAuth.currentUser
        val credential = EmailAuthProvider.getCredential(currentUserEmail, password)

        // Prompt the user to re-provide their sign-in credentials
        if (user != null) {
            try {
                user
                    .reauthenticate(credential)
                    .await()
                Log.d(
                    TAG,
                    context.applicationContext.getString(R.string.user_re_authenticated_success_log)
                )
                try {
                    user
                        .updateEmail(afterChangeEmail)
                        .await()
                    Log.d(
                        TAG,
                        context.applicationContext.getString(R.string.user_email_address_updated_success_log)
                    )
                    isSuccess.value =
                        updateEmailInFirestore(uid = user.uid, email = afterChangeEmail)
                } catch (e: Exception) {
                    Log.w(
                        TAG,
                        context.applicationContext.getString(R.string.user_email_address_updated_failure_log)
                    )
                    isSuccess.value = false
                }
            } catch (e: Exception) {
                Log.w(
                    TAG,
                    context.applicationContext.getString(R.string.user_re_authenticated_failure_log)
                )
                isSuccess.value = false
            }
        } else {
            isSuccess.value = false
        }

        return isSuccess
    }

    private suspend fun updateEmailInFirestore(uid: String, email: String): Boolean {
        return try {
            usersRef
                .document(uid)
                .update(Constant.FIELD_NAME_EMAIL, email)
                .await()
            Log.d(TAG, "updateEmailInFirestore: success")
            sendEmailVerification()
            true
        } catch (e: Exception) {
            Log.w(TAG, "updateEmailInFirestore: failure", e)
            false
        }
    }

    suspend fun sendPasswordResetEmail(email: String): LiveData<Boolean> {
        val isSuccess = MutableLiveData<Boolean>()
        try {
            firebaseAuth
                .sendPasswordResetEmail(email)
                .await()
            Log.d(TAG, context.applicationContext.getString(R.string.email_sent_success_log))
            isSuccess.value = true
        } catch (e: Exception) {
            Log.w(TAG, context.applicationContext.getString(R.string.email_sent_failure_log), e)
            isSuccess.value = false
        }
        return isSuccess
    }

    fun saveTmpEmail(email: String): LiveData<String> {
        tmpEmail.value = email
        return tmpEmail
    }

    fun getTmpEmail(): LiveData<String> {
        val currentEmail = firebaseAuth.currentUser?.email
        return if (currentEmail != null) {
            tmpEmail.value = currentEmail
            tmpEmail
        } else {
            tmpEmail
        }
    }
}