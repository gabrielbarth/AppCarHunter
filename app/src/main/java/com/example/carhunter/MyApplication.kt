package com.example.carhunter

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.carhunter.ui.carForm.CarFormScreen
import com.example.carhunter.ui.carList.CarListScreen
import com.example.carhunter.ui.login.LoginScreen

private object Screens {
    const val LOGIN = "login"
    const val CAR_LIST = "carList"
    const val CAR_DETAILS = "carDetails"
    const val CAR_FORM = "carForm"
}

object Arguments {
    const val CAR_ID = "carId"
}

private object Routes {
    const val LOGIN = Screens.LOGIN
    const val CAR_LIST = Screens.CAR_LIST
    const val CAR_DETAILS = "${Screens.CAR_DETAILS}/{${Arguments.CAR_ID}}"
    const val CAR_FORM =
        "${Screens.CAR_FORM}?${Arguments.CAR_ID}={${Arguments.CAR_ID}}"
}

@Composable
fun MyApplication(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screens.LOGIN
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = Routes.LOGIN) {
            LoginScreen(
                onSignInSuccess = {
                    println("NavHost -> onSignInSuccess")
                    navController.navigate(Screens.CAR_LIST) {
                        popUpTo(Screens.LOGIN) { inclusive = true }
                    }
                },
            )
        }
        composable(route = Routes.CAR_LIST) {
            CarListScreen(
                onGoToCarForm = {
                    println("NavHost -> onGoToCarForm")
                    navController.navigate(Screens.CAR_FORM)
                },
                onLogout = {
                    println("NavHost -> onLogout")
                    navController.navigate(Screens.LOGIN)
                }
            )
        }
        composable(route = Routes.CAR_FORM) {
            CarFormScreen()
        }

    }

}