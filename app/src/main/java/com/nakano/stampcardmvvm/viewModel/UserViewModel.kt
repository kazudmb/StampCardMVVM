package com.nakano.stampcardmvvm.viewModel

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.nakano.stampcardmvvm.model.model.UserFirebase
import com.nakano.stampcardmvvm.model.repository.UserRepository

class UserViewModel(
    var repository: UserRepository
) : ViewModel() {
    val user = repository.getUser()
    val qrCode = repository.getQRCode()

    lateinit var userLiveData: LiveData<UserFirebase>
    lateinit var stampLiveData: LiveData<List<Drawable>>
    lateinit var isLoginLiveData: LiveData<Boolean>

    fun getUser() {
        userLiveData = repository.getUser()
    }

    fun setStamp() {
        stampLiveData = repository.getStamp()
    }

    fun isLogin() {
        isLoginLiveData = repository.isLogin()
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
