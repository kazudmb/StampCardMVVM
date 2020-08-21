package com.nakano.stampcardmvvm.viewModel

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.nakano.stampcardmvvm.model.model.User
import com.nakano.stampcardmvvm.model.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(
    var repository: UserRepository
) : ViewModel() {
    val qrCode = repository.getQRCode()

    private val _user = MutableLiveData<User>()
    var user: LiveData<User> = _user
    private val _stamp = MutableLiveData<List<Drawable>>()
    var stamp: LiveData<List<Drawable>> = _stamp
    lateinit var isLoginLiveData: LiveData<Boolean>
    private val _currentProviderId = MutableLiveData<String?>()
    var currentProviderId: LiveData<String?> = _currentProviderId

    fun getUser() {
        user = repository.getUser()
        _user.value = user.value
    }

    fun getUserFromFirestore() {
        viewModelScope.launch(Dispatchers.Main) {
            user = repository.getUserFromFirestore()
            _user.value  = user.value
        }
    }

    fun setBlankStampArea() {
        stamp = repository.getBlankStampArea()
        _stamp.value = stamp.value
    }

    fun setStamp() {
        stamp = repository.getStamp()
    }

    fun isLogin() {
        isLoginLiveData = repository.isLogin()
    }

    fun getCurrentProviderId() {
        currentProviderId = repository.getCurrentProviderId()
    }

    fun logoutFromGoogle() {
        FirebaseAuth.getInstance().signOut()
    }
}

@BindingAdapter("imageBitmap")
fun imageBitmap(imageView: ImageView, bitmap: Bitmap) {
    imageView.setImageBitmap(bitmap)
}

@BindingAdapter("android:src")
fun setImageDrawable(imageView: ImageView, drawable: Drawable) {
    imageView.setImageDrawable(drawable)
}
