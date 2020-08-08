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

class UserRepository(
    private val db: AppDatabase,
    private val context: Context
) {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val rootRef = FirebaseFirestore.getInstance()
    private val usersRef = rootRef.collection("users")

    fun getUser(): LiveData<UserFirebase> {

        val userMutableLiveData = MutableLiveData<UserFirebase>()

        if (firebaseAuth.currentUser != null) {

            val uid = firebaseAuth.currentUser!!.uid

            usersRef
                .document(uid)
                .get()
                .addOnSuccessListener {
                    if (it.exists()) {
                        // TODO: ハードコードしないように対応すること
                        val uid = it["uid"] as String?
                        val name = it["name"] as String?
                        val email = it["email"] as String?
                        val numberOfVisits = it["numberOfVisits"] as String? ?: "0"
                        val rank = Utility.getRank(context, numberOfVisits)
                        val user = UserFirebase(uid, name, email, numberOfVisits, rank)
                        userMutableLiveData.value = user
                        Log.d(TAG, "${it.id} => ${it.data}")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents.", exception)
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

        val mutableLiveData = MutableLiveData<Bitmap>()

        val firebaseAuth = FirebaseAuth.getInstance()
        val firebaseUser = firebaseAuth.currentUser

        mutableLiveData.value = Utility.createQRCode(context, firebaseUser?.uid)

        return mutableLiveData
    }

    fun getStamp(): LiveData<List<Drawable>> {

        val stamp = listOf(
            ResourcesCompat.getDrawable(context.resources, R.drawable.logo_stamp_area_icon1, null),
            ResourcesCompat.getDrawable(context.resources, R.drawable.logo_stamp_area_icon2, null),
            ResourcesCompat.getDrawable(context.resources, R.drawable.logo_stamp_area_icon3, null),
            ResourcesCompat.getDrawable(context.resources, R.drawable.logo_stamp_area_icon4, null),
            ResourcesCompat.getDrawable(context.resources, R.drawable.logo_stamp_area_icon5, null),
            ResourcesCompat.getDrawable(context.resources, R.drawable.logo_stamp_area_icon6, null),
            ResourcesCompat.getDrawable(context.resources, R.drawable.logo_stamp_area_icon7, null),
            ResourcesCompat.getDrawable(context.resources, R.drawable.logo_stamp_area_icon8, null),
            ResourcesCompat.getDrawable(context.resources, R.drawable.logo_stamp_area_icon9, null),
            ResourcesCompat.getDrawable(context.resources, R.drawable.logo_stamp_area_icon10, null)
        )

        val loopCount: Int
        val numberOfVisits = "9" // TODO numberOfVisitsの取得方法を検討、現状SPやSQLiteに持っていない
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
                val layers = arrayOf(stamp[i - 1], approved)
                layerDrawable.add(LayerDrawable(layers))
            } else {
                val layers = arrayOf(stamp[i - 1])
                layerDrawable.add(LayerDrawable(layers))
            }
        }

        val mutableLiveData = MutableLiveData<List<Drawable>>()
        mutableLiveData.value = layerDrawable

        return mutableLiveData
    }

    fun isLogin(): LiveData<Boolean> {

        val mutableLiveData = MutableLiveData<Boolean>()

        val firebaseAuth = FirebaseAuth.getInstance()

        if (firebaseAuth.currentUser == null) {
            mutableLiveData.value = false
        } else {
            mutableLiveData.value = true
        }

        return mutableLiveData
    }
}
