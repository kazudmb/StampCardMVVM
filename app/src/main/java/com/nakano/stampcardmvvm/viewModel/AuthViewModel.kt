package com.nakano.stampcardmvvm.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.nakano.stampcardmvvm.model.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthViewModel(
    var repository: AuthRepository
) : ViewModel() {
    private var isSuccess: Boolean = false
    fun signInWithGoogle(googleAuthCredential: AuthCredential?): Boolean {
        // TODO: High Coroutineを使用しているため、値が入ってくるのを待ってからretuneしたい
        viewModelScope.launch(Dispatchers.Main) {
            isSuccess = repository.signInWithGoogle(googleAuthCredential)
        }
        return isSuccess
    }
}