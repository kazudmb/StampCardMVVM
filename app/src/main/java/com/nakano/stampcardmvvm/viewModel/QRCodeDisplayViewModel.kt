package com.nakano.stampcardmvvm.viewModel

import androidx.lifecycle.*
import com.nakano.stampcardmvvm.model.repository.QRCodeDisplayRepository

class QRCodeDisplayViewModel(
    private val repository: QRCodeDisplayRepository
): ViewModel() {

    val stampCard = repository.getStampCard()

}
