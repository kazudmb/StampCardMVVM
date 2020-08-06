package com.nakano.stampcardmvvm.model.repository

import android.content.Context
import android.util.Log
import androidx.constraintlayout.widget.Constraints.TAG
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.nakano.stampcardmvvm.model.model.AppDatabase
import com.nakano.stampcardmvvm.model.model.UserFirebase
import com.nakano.stampcardmvvm.util.Utility

class UserRepository(
    private val db: AppDatabase,
    private val context: Context
) {
    private val rootRef = FirebaseFirestore.getInstance()
    private val usersRef = rootRef.collection("users")

    fun getUser(): LiveData<UserFirebase> {

        val userMutableLiveData = MutableLiveData<UserFirebase>()

        usersRef
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    // TODO: ハードコードしないように対応すること
                    val uid = document.data["uid"] as String
                    val name = document.data["name"] as String
                    val email = document.data["email"] as String
                    val numberOfVisits = document.data["numberOfVisits"] as String
                    val rank = Utility.getRank(context, numberOfVisits)
                    val user = UserFirebase(uid, name, email, numberOfVisits, rank)
                    user.isNew = true
                    userMutableLiveData.value = user
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }

        return userMutableLiveData
    }
}
