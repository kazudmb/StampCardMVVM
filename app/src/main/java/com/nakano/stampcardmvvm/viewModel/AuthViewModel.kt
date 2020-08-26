package com.nakano.stampcardmvvm.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.nakano.stampcardmvvm.model.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(
    var repository: AuthRepository
) : ViewModel() {
    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess
    private val _email = MutableLiveData<String>()
    var email: LiveData<String> = _email

    fun signInWithGoogle(googleAuthCredential: AuthCredential?) {
        viewModelScope.launch {
            val result = repository.signInWithGoogle(googleAuthCredential)
            _isSuccess.postValue(result.value)
        }
    }

    fun signInWithTwitter() {
        viewModelScope.launch {
            val result = repository.signInWithTwitter()
            _isSuccess.postValue(result.value)
        }
    }

    fun signInWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch {
            val result = repository.signInWithEmailAndPassword(email, password)
            _isSuccess.postValue(result.value)
        }
    }

    fun createInWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch {
            val result = repository.createUserWithEmailAndPassword(email, password)
            _isSuccess.postValue(result.value)
        }
    }

    fun signInAnonymous() {
        viewModelScope.launch {
            val result = repository.signInAnonymous()
            _isSuccess.postValue(result.value)
        }
    }

    fun updateEmail(currentUserEmail: String, afterChangeEmail: String, password: String) {
        viewModelScope.launch {
            val result = repository.updateEmail(currentUserEmail, afterChangeEmail, password)
            _isSuccess.postValue(result.value)
        }
    }

    fun sendPasswordResetEmail(email: String) {
        viewModelScope.launch {
            val result = repository.sendPasswordResetEmail(email)
            _isSuccess.postValue(result.value)
        }
    }

    fun saveTmpEmail(email: String) {
        val result = repository.saveTmpEmail(email)
        _email.postValue(result.value)
    }

    fun getTmpEmail() {
        val result = repository.getTmpEmail()
        _email.postValue(result.value)
    }
}