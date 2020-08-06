package com.nakano.stampcardmvvm.viewModel

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel
import com.nakano.stampcardmvvm.model.repository.UserRepository

class UserViewModel(
    repository: UserRepository
) : ViewModel() {
    val user = repository.getUser()
    val qrCode = repository.getQRCode()
}

// TODO BindingAdapter用のファイルに移すべき？
@BindingAdapter("imageBitmap")
fun imageBitmap(imageView: ImageView, bitmap: Bitmap) {
    imageView.setImageBitmap(bitmap)
}
