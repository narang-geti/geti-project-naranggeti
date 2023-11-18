package com.app.getiproject_naranggeti

import CustomerEvaluation
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.getiproject_naranggeti.ui.theme.GetiProject_naranggetiTheme
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {

    private val auth: FirebaseAuth =FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Navi()

        }
    }
}

@Composable
fun Navi() {

    GetiProject_naranggetiTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {

            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "detect") {
                composable("start") {
                    StartScreen(navController)
                }
                composable("detect") {
                    DetectScreen(navController)
                }
                composable("login"){
                    LoginScreen(LoginViewModel(), {}, navController)
                }
                composable("Sign Up"){
                    SignupScreen(navController)
                }
                composable("customer"){
                    CustomerEvaluation(navController)
                }

            }

        }
    }
}

