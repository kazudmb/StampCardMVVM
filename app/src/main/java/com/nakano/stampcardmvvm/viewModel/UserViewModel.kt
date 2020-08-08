package com.nakano.stampcardmvvm.viewModel

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.nakano.stampcardmvvm.model.repository.UserRepository

class UserViewModel(
    var repository: UserRepository
) : ViewModel() {
    val user = repository.getUser()
    val qrCode = repository.getQRCode()
    val stamp = repository.getStamp()

    var isLoginLiveData: LiveData<Boolean>? = null
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
