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
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import com.nakano.stampcardmvvm.R
import com.nakano.stampcardmvvm.model.model.User
import com.nakano.stampcardmvvm.util.Constant
import com.nakano.stampcardmvvm.util.Utility
import kotlinx.coroutines.tasks.await

class UserRepository(
    private val context: Context
) {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val rootRef = FirebaseFirestore.getInstance()
    private val usersRef = rootRef.collection(Constant.COLLECTION_PATH)

    private val userMutableLiveData = MutableLiveData<User>()
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

    fun getUser(): LiveData<User> {
        return userMutableLiveData
    }

    suspend fun getUserFromFirestore(): LiveData<User> {
        val uid = firebaseAuth.currentUser?.uid

        if (uid != null) {
            val data = usersRef
                .document(uid)
                .get()
                .await()

            try {
                if (data.exists()) {
                    val uid = data[Constant.FIELD_NAME_UID] as String
                    val name = data[Constant.FIELD_NAME_NAME] as String?
                    val email = data[Constant.FIELD_NAME_EMAIL] as String?
                    val numberOfVisits = data[Constant.FIELD_NAME_NUMBER_OF_VISITS] as Long
                    val rank = Utility.getRank(context, numberOfVisits)
                    val user = User(uid, name, email, numberOfVisits, rank)
                    userMutableLiveData.value = user
                    Log.d(TAG, "${data.id} => ${data.data}")
                }
            } catch (e: Exception) {
                Log.w(TAG, "Error getting documents.", e)
            }

        } else {
            val uid = ""
            val name = null as String?
            val email = null as String?
            val numberOfVisits = 0.toLong()
            val rank = Utility.getRank(context, numberOfVisits)
            val user = User(uid, name, email, numberOfVisits, rank)
            userMutableLiveData.value = user
        }
        return userMutableLiveData
    }

    fun getQRCode(): LiveData<Bitmap> {
        val qrCodeMutableLiveData = MutableLiveData<Bitmap>()

        val firebaseAuth = FirebaseAuth.getInstance()
        val uid = firebaseAuth.currentUser?.uid

        if (uid != null) {
            qrCodeMutableLiveData.value = Utility.createQRCode(context, uid)
        }

        return qrCodeMutableLiveData
    }

    fun getBlankStampArea(): LiveData<List<Drawable>> {
        drawableMutableLiveData.value = stampArea
        return drawableMutableLiveData
    }

    fun getStamp(): LiveData<List<Drawable>> {
        val loopCount: Int
        val numberOfVisits =
            userMutableLiveData.value?.numberOfVisits ?: Constant.DEFAULT_NUMBER_OF_VISITS
        val numberOfCutOut =
            numberOfVisits.toString().substring(numberOfVisits.toString().length - 1).toInt()

        loopCount = if (numberOfCutOut == 0 && numberOfVisits < 10) {
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

    fun getCurrentProviderId(): LiveData<String?> {
        val currentProviderIdMutableLiveData = MutableLiveData<String?>()

        when (firebaseAuth.currentUser?.providerId) {
            EmailAuthProvider.PROVIDER_ID -> currentProviderIdMutableLiveData.value =
                EmailAuthProvider.PROVIDER_ID
            FacebookAuthProvider.PROVIDER_ID -> currentProviderIdMutableLiveData.value =
                FacebookAuthProvider.PROVIDER_ID
            FirebaseAuthProvider.PROVIDER_ID -> currentProviderIdMutableLiveData.value =
                FirebaseAuthProvider.PROVIDER_ID
            GithubAuthProvider.PROVIDER_ID -> currentProviderIdMutableLiveData.value =
                GithubAuthProvider.PROVIDER_ID
            GoogleAuthProvider.PROVIDER_ID -> currentProviderIdMutableLiveData.value =
                GoogleAuthProvider.PROVIDER_ID
            PhoneAuthProvider.PROVIDER_ID -> currentProviderIdMutableLiveData.value =
                PhoneAuthProvider.PROVIDER_ID
            PlayGamesAuthProvider.PROVIDER_ID -> currentProviderIdMutableLiveData.value =
                PlayGamesAuthProvider.PROVIDER_ID
            TwitterAuthProvider.PROVIDER_ID -> currentProviderIdMutableLiveData.value =
                TwitterAuthProvider.PROVIDER_ID
            else -> currentProviderIdMutableLiveData.value = null
        }
        return currentProviderIdMutableLiveData
    }
}
