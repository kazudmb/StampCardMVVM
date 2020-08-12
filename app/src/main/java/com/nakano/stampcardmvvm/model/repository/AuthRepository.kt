package com.nakano.stampcardmvvm.model.repository

import android.content.Context
import android.util.Log
import androidx.constraintlayout.widget.Constraints.TAG
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.nakano.stampcardmvvm.model.model.AppDatabase
import com.nakano.stampcardmvvm.model.model.UserFirebase
import com.nakano.stampcardmvvm.util.HelperClass
import com.nakano.stampcardmvvm.util.Utility
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AuthRepository(
    private val db: AppDatabase,
    private val context: Context
) {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val rootRef = FirebaseFirestore.getInstance()
    private val usersRef = rootRef.collection("users")

    // TODO: ReturnをResponse?result?にして、suceessの場合はview側でnavigationする、failedの場合はtoastの表示
    suspend fun signInWithGoogle(googleAuthCredential: AuthCredential?) {
        // TODO: withContextは、Dispatchersの切り替えのため、Main->Mainなので、ここでは使う意味ないが、createUserInFirestoreIfNotExist()では使う
//        withContext(Dispatchers.Main) {
            try{
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
                    // TODO: High userのcreateが出来ていないのでここから調査を進めること
                    createUserInFirestoreIfNotExists(user)
                } else {
                    // TODO: StampCardFragmentにnavigationさせる(repositoryでは実行しない)
                }
            }catch (e : Exception){
                Log.e(TAG, "signInWithGoogle: ", e)
            }
//        }
    }

    // TODO: コールバックを使用するのではなく、await()で処理を換装すること
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

    // TODO: sampleなので、いらなくなったら削除すること
//    suspend fun saveDataInFireStore(
//        childName : String,
//        hashMap: HashMap<String,Any>
//    ) : Boolean{
//        return try{
//            val data = rootRef
//                .collection("users")
//                .document(childName)
//                .set(hashMap)
//                .await()
//            return true
//        }catch (e : Exception){
//            return false
//        }
//    }
//
//    suspend fun getDataFromFireStore(childName : String): DocumentSnapshot?{
//        return try{
//            val data = rootRef
//                .collection("users")
//                .document(childName)
//                .get()
//                .await()
//            data
//        }catch (e : Exception){
//            null
//        }
//    }
}