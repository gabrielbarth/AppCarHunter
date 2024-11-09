package com.example.carhunter.ui.login

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    onSignInSuccess: () -> Unit,
) {
    val uiState = viewModel.uiState.collectAsState().value
    val isCodeSent = viewModel.isCodeSent.collectAsState().value
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Enter your Phone Number", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = uiState.phoneNumber,
            onValueChange = { viewModel.onPhoneNumberChange(it) },
            label = { Text("Phone Number") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            enabled = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        if (isCodeSent) {
            Text(text = "Enter Confirmation Code", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = viewModel.uiState.value.confirmationCode,
                onValueChange = { viewModel.onChangeConfirmationCode(it) },
                label = { Text("Confirmation code") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (isCodeSent) {
                    println("code confirmation")
                    viewModel.onVerifyCode()
                } else {
                    println("else.. onSendVerificationCode")
                    viewModel.onSendVerificationCode()
                }
            },
            enabled = !uiState.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (viewModel.uiState.value.isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Text(if (isCodeSent) "Verificar código" else "Enviar código")
            }
        }

    }
    LaunchedEffect(uiState.hasVerificationCodeSent) {
        if (uiState.hasVerificationCodeSent) {
            Toast.makeText(context, "Código de verificação enviado!", Toast.LENGTH_SHORT).show()
            viewModel.resetCodeSentFlag()
        }
    }

    LaunchedEffect(uiState.hasSuccessfullyLogged) {
        if (uiState.hasSuccessfullyLogged) {
            onSignInSuccess()
        }
    }
}