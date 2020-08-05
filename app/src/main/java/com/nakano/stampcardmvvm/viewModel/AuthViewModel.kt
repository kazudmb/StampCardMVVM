package com.nakano.stampcardmvvm.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.AuthCredential
import com.nakano.stampcardmvvm.model.model.UserFirebase
import com.nakano.stampcardmvvm.model.repository.AuthRepository

class AuthViewModel(
    var repository: AuthRepository
) : ViewModel() {

    // TODO AuthViewModelをUserViewModelに統合すべき？

    var authenticatedUserLiveData: LiveData<UserFirebase>? = null
    var createdUserLiveData: LiveData<UserFirebase>? = null

    fun signInWithGoogle(googleAuthCredential: AuthCredential?) {
        authenticatedUserLiveData = repository.firebaseSignInWithGoogle(googleAuthCredential)
    }

    fun createUser(authenticatedUser: UserFirebase) {
        createdUserLiveData = repository.createUserInFirestoreIfNotExists(authenticatedUser)
    }

}