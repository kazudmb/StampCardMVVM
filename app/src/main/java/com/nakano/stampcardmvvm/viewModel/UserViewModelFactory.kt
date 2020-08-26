package com.nakano.stampcardmvvm.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nakano.stampcardmvvm.model.repository.AuthRepository
import com.nakano.stampcardmvvm.model.repository.UserRepository

@Suppress("UNCHECKED_CAST")
class UserViewModelFactory(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return UserViewModel(userRepository, authRepository) as T
    }
}