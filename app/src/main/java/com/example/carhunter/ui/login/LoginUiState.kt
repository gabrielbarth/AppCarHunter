package com.example.carhunter.ui.login

data class LoginUiState(
    val isLoading: Boolean = false,
    val hasError: Boolean = false,
    var phoneNumber: String = "",
    val hasVerificationCodeSent: Boolean = false,
    val verificationId: String = "",
    var confirmationCode: String = "",
    var hasSuccessfullyLogged: Boolean = false,
)