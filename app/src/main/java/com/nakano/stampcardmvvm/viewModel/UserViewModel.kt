package com.nakano.stampcardmvvm.viewModel

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.nakano.stampcardmvvm.model.model.User
import com.nakano.stampcardmvvm.model.repository.AuthRepository
import com.nakano.stampcardmvvm.model.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(
    var userRepository: UserRepository,
    var authRepository: AuthRepository
) : ViewModel() {
    val qrCode = userRepository.getQRCode()

    private val _user = MutableLiveData<User>()
    var user: LiveData<User> = _user
    private val _stamp = MutableLiveData<List<Drawable>>()
    var stamp: LiveData<List<Drawable>> = _stamp
    lateinit var isLoginLiveData: LiveData<Boolean>
    private val _currentProviderId = MutableLiveData<String?>()
    var currentProviderId: LiveData<String?> = _currentProviderId
    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess
    private val _email = MutableLiveData<String>()
    var email: LiveData<String> = _email

    fun getUser() {
        user = userRepository.getUser()
        _user.value = user.value
    }

    fun getUserFromFirestore() {
        viewModelScope.launch(Dispatchers.Main) {
            user = userRepository.getUserFromFirestore()
            _user.value  = user.value
        }
    }

    fun setBlankStampArea() {
        stamp = userRepository.getBlankStampArea()
        _stamp.value = stamp.value
    }

    fun setStamp() {
        stamp = userRepository.getStamp()
    }

    fun isLogin() {
        isLoginLiveData = userRepository.isLogin()
    }

    fun getCurrentProviderId() {
        currentProviderId = userRepository.getCurrentProviderId()
    }

    fun logoutFromGoogle() {
        FirebaseAuth.getInstance().signOut()
        saveTmpEmail("") // reset
    }

    fun signInWithGoogle(googleAuthCredential: AuthCredential?) {
        viewModelScope.launch {
            val result = authRepository.signInWithGoogle(googleAuthCredential)
            _isSuccess.postValue(result.value)
        }
    }

    fun signInWithTwitter() {
        viewModelScope.launch {
            val result = authRepository.signInWithTwitter()
            _isSuccess.postValue(result.value)
        }
    }

    fun signInWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch {
            val result = authRepository.signInWithEmailAndPassword(email, password)
            _isSuccess.postValue(result.value)
        }
    }

    fun createInWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch {
            val result = authRepository.createUserWithEmailAndPassword(email, password)
            _isSuccess.postValue(result.value)
        }
    }

    fun signInAnonymous() {
        viewModelScope.launch {
            val result = authRepository.signInAnonymous()
            _isSuccess.postValue(result.value)
        }
    }

    fun updateEmail(currentUserEmail: String, afterChangeEmail: String, password: String) {
        viewModelScope.launch {
            val result = authRepository.updateEmail(currentUserEmail, afterChangeEmail, password)
            _isSuccess.postValue(result.value)
        }
    }

    fun sendPasswordResetEmail(email: String) {
        viewModelScope.launch {
            val result = authRepository.sendPasswordResetEmail(email)
            _isSuccess.postValue(result.value)
        }
    }

    fun saveTmpEmail(email: String) {
        val result = authRepository.saveTmpEmail(email)
        _email.postValue(result.value)
    }

    fun getTmpEmail() {
        val result = authRepository.getTmpEmail()
        _email.postValue(result.value)
    }
}

@BindingAdapter("imageBitmap")
fun imageBitmap(imageView: ImageView, bitmap: Bitmap) {
    imageView.setImageBitmap(bitmap)
}

@BindingAdapter("android:src")
fun setImageDrawable(imageView: ImageView, drawable: Drawable) {
    imageView.setImageDrawable(drawable)
}
