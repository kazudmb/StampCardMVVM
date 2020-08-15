package com.nakano.stampcardmvvm.model.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.util.Log
import androidx.constraintlayout.widget.Constraints.TAG
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nakano.stampcardmvvm.R
import com.nakano.stampcardmvvm.model.model.AppDatabase
import com.nakano.stampcardmvvm.model.model.UserFirebase
import com.nakano.stampcardmvvm.util.Utility
import kotlinx.coroutines.tasks.await

class UserRepository(
    private val db: AppDatabase,
    private val context: Context
) {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val rootRef = FirebaseFirestore.getInstance()
    private val usersRef = rootRef.collection("users")

    private val userMutableLiveData = MutableLiveData<UserFirebase>()
    private val drawableMutableLiveData = MutableLiveData<List<Drawable>>()

    private val stampArea = mutableListOf(
        ResourcesCompat.getDrawable(context.resources, R.drawable.logo_stamp_area_icon1, null)!!,
        ResourcesCompat.getDrawable(context.resources, R.drawable.logo_stamp_area_icon2, null)!!,
        ResourcesCompat.getDrawable(context.resources, R.drawable.logo_stamp_area_icon3, null)!!,
        ResourcesCompat.getDrawable(context.resources, R.drawable.logo_stamp_area_icon4, null)!!,
        ResourcesCompat.getDrawable(context.resources, R.drawable.logo_stamp_area_icon5, null)!!,
        ResourcesCompat.getDrawable(context.resources, R.drawable.logo_stamp_area_icon6, null)!!,
        ResourcesCompat.getDrawable(context.resources, R.drawable.logo_stamp_area_icon7, null)!!,
        ResourcesCompat.getDrawable(context.resources, R.drawable.logo_stamp_area_icon8, null)!!,
        ResourcesCompat.getDrawable(context.resources, R.drawable.logo_stamp_area_icon9, null)!!,
        ResourcesCompat.getDrawable(context.resources, R.drawable.logo_stamp_area_icon10, null)!!
    )

    fun getUser(): LiveData<UserFirebase> {
        return userMutableLiveData
    }

    suspend fun getUserFromGoogle(): LiveData<UserFirebase> {
            val uid = firebaseAuth.currentUser?.uid

            if (uid != null) {
                val data = usersRef
                    .document(uid)
                    .get()
                    .await()

                try {
                    if(data.exists()) {
                        // TODO: ハードコードしないように対応すること
                        val uid = data["uid"] as String?
                        val name = data["name"] as String?
                        val email = data["email"] as String?
                        val numberOfVisits = data["numberOfVisits"] as String? ?: "0"
                        val rank = Utility.getRank(context, numberOfVisits)
                        val user = UserFirebase(uid, name, email, numberOfVisits, rank)
                        userMutableLiveData.value = user
                        Log.d(TAG, "${data.id} => ${data.data}")
                    }
                } catch (e: Exception) {
                    Log.w(TAG, "Error getting documents.", e)
                }

            } else {
                val uid = null as String?
                val name = null as String?
                val email = null as String?
                val numberOfVisits = "0"
                val rank = Utility.getRank(context, numberOfVisits)
                val user = UserFirebase(uid, name, email, numberOfVisits, rank)
                userMutableLiveData.value = user
            }
        return userMutableLiveData
    }

    fun getQRCode(): LiveData<Bitmap> {
        val qrCodeMutableLiveData = MutableLiveData<Bitmap>()

        val firebaseAuth = FirebaseAuth.getInstance()
        val firebaseUser = firebaseAuth.currentUser

        qrCodeMutableLiveData.value = Utility.createQRCode(context, firebaseUser?.uid)

        return qrCodeMutableLiveData
    }

    fun getBlankStampArea(): LiveData<List<Drawable>> {
        drawableMutableLiveData.value = stampArea
        return drawableMutableLiveData
    }

    fun getStamp(): LiveData<List<Drawable>> {
        val loopCount: Int
        val numberOfVisits = userMutableLiveData.value?.numberOfVisits ?: "0"
        val numberOfCutOut = numberOfVisits.substring(numberOfVisits.length - 1).toInt()

        loopCount = if (numberOfCutOut == 0 && numberOfVisits.toInt() < 10) {
            0
        } else {
            if (numberOfCutOut == 0) {
                10
            } else {
                numberOfCutOut
            }
        }

        val layerDrawable = mutableListOf<Drawable>()
        for (i in 1..10) {
            if (i <= loopCount) {
                val approved = ResourcesCompat.getDrawable(
                    context.resources,
                    R.drawable.logo_stamp_icon1,
                    null
                )
                val layers = arrayOf(stampArea[i - 1], approved)
                layerDrawable.add(LayerDrawable(layers))
            } else {
                val layers = arrayOf(stampArea[i - 1])
                layerDrawable.add(LayerDrawable(layers))
            }
        }

        drawableMutableLiveData.value = layerDrawable

        return drawableMutableLiveData
    }

    fun isLogin(): LiveData<Boolean> {
        val isLoginMutableLiveData = MutableLiveData<Boolean>()

        val firebaseAuth = FirebaseAuth.getInstance()

        if (firebaseAuth.currentUser == null) {
            isLoginMutableLiveData.value = false
        } else {
            isLoginMutableLiveData.value = true
        }

        return isLoginMutableLiveData
    }
}
