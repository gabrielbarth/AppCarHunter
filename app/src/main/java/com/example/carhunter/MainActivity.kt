package com.example.carhunter

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.carhunter.pages.login.LoginScreen
import com.example.carhunter.pages.login.LoginViewModel

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val viewModel: LoginViewModel = viewModel()
                LoginScreen(
                    viewModel,
                    activity = this,
                    onSignInSuccess = { println("login sucess!") })
            }
        }
    }

    override fun onResume() {
        super.onResume()
        fetchItems()
    }

    private fun setupView() {
        // TODO
    }

    private fun requestLocationPermission() {
        // TODO
    }

    private fun fetchItems() {
        // TODO
    }
}
