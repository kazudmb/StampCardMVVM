package com.nakano.stampcardmvvm.viewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.firebase.auth.AuthCredential;
import com.nakano.stampcardmvvm.model.model.UserFirebase;
import com.nakano.stampcardmvvm.model.repository.AuthRepository;

public class AuthViewModel extends AndroidViewModel {
    private AuthRepository authRepository;
    public LiveData<UserFirebase> authenticatedUserLiveData;
    public LiveData<UserFirebase> createdUserLiveData;

    public AuthViewModel(Application application) {
        super(application);
        authRepository = new AuthRepository();
    }

    public void signInWithGoogle(AuthCredential googleAuthCredential) {
        authenticatedUserLiveData = authRepository.firebaseSignInWithGoogle(googleAuthCredential);
    }

    public void createUser(UserFirebase authenticatedUser) {
        createdUserLiveData = authRepository.createUserInFirestoreIfNotExists(authenticatedUser);
    }

//    public MutableLiveData<String> liveDataText = new MutableLiveData<String>();
//
//    public void getUser() {
//        liveDataText.setValue(authRepository.getUser());
//    }
}