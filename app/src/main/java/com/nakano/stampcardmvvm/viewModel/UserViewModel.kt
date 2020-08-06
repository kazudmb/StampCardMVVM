package com.nakano.stampcardmvvm.viewModel

import androidx.lifecycle.ViewModel
import com.nakano.stampcardmvvm.model.repository.UserRepository

class UserViewModel(
    repository: UserRepository
) : ViewModel() {
    val user = repository.getUser()
    val qrCode = repository.getQRCode()
}
