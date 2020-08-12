package com.nakano.stampcardmvvm.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.nakano.stampcardmvvm.model.model.UserFirebase
import com.nakano.stampcardmvvm.model.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthViewModel(
    var repository: AuthRepository
) : ViewModel() {

    var createdUserLiveData: LiveData<UserFirebase>? = null

    fun signInWithGoogle(googleAuthCredential: AuthCredential?) {
        viewModelScope.launch(Dispatchers.Main) {
            repository.signInWithGoogle(googleAuthCredential)
        }
    }

    fun createUser(authenticatedUser: UserFirebase) {
        createdUserLiveData = repository.createUserInFirestoreIfNotExists(authenticatedUser)
    }

}