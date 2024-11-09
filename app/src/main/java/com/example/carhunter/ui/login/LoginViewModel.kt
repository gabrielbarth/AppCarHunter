package com.example.carhunter.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.concurrent.TimeUnit

class LoginViewModel() : ViewModel() {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    private val _isCodeSent = MutableStateFlow(false)
    val isCodeSent: StateFlow<Boolean> = _isCodeSent

    fun onPhoneNumberChange(newPhoneNumber: String) {
        _uiState.value = _uiState.value.copy(phoneNumber = newPhoneNumber)
    }

    fun onChangeConfirmationCode(confirmationCode: String) {
        _uiState.value = _uiState.value.copy(confirmationCode = confirmationCode)
    }

    fun resetCodeSentFlag() {
        _uiState.value = _uiState.value.copy(hasVerificationCodeSent = false)
    }

    fun onSendVerificationCode() {
        _uiState.value = _uiState.value.copy(isLoading = true)
        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(_uiState.value.phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    _uiState.value =
                        _uiState.value.copy(isLoading = false, hasVerificationCodeSent = true)
                    println("onVerificationCompleted")
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    _uiState.value = _uiState.value.copy(hasError = true, isLoading = false)
                    println("onVerificationFailed")
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    _uiState.value = _uiState.value.copy(
                        hasVerificationCodeSent = true,
                        isLoading = false,
                        verificationId = verificationId
                    )
                    _isCodeSent.value = true
                    println("onCodeSent")
                }
            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun onVerifyCode() {
        println("onVerifyCode ${_uiState.value.verificationId} & ${_uiState.value.confirmationCode}")
        _uiState.value = _uiState.value.copy(isLoading = true)
        val credential =
            PhoneAuthProvider.getCredential(
                _uiState.value.verificationId,
                _uiState.value.confirmationCode,
            )
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    println("task Successful!")
                    val user = firebaseAuth.currentUser
                    Log.d("LoginActivity", "User logged: ${user?.uid}")
                    _uiState.value =
                        _uiState.value.copy(hasSuccessfullyLogged = true, isLoading = false)
                } else {
                    println("task failed ${task.exception?.localizedMessage}")
                    _uiState.value = _uiState.value.copy(hasError = true, isLoading = false)
                }
            }
    }


}