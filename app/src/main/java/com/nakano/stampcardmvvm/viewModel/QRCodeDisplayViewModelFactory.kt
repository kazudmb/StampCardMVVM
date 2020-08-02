package com.nakano.stampcardmvvm.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nakano.stampcardmvvm.model.repository.QRCodeDisplayRepository

@Suppress("UNCHECKED_CAST")
class QRCodeDisplayViewModelFactory(
    private val repository: QRCodeDisplayRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return QRCodeDisplayViewModel(repository) as T
    }
}