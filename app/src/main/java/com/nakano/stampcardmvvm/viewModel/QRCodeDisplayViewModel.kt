package com.nakano.stampcardmvvm.viewModel

import android.graphics.Bitmap
import androidx.lifecycle.*
import com.nakano.stampcardmvvm.model.repository.QRCodeDisplayRepository

class QRCodeDisplayViewModel(private val qrCodeDisplayRepository: QRCodeDisplayRepository): ViewModel() {

    private val _qrCode: MutableLiveData<Bitmap> = MutableLiveData()

    val qrCode: LiveData<Bitmap>
        get() = _qrCode

    fun generate() {
        val qrCode = qrCodeDisplayRepository.generateBitmap()
        _qrCode.value = qrCode
    }
}
