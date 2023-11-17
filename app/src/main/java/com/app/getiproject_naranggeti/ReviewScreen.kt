package com.app.getiproject_naranggeti

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun ReviewScreen(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val arguments = navBackStackEntry?.arguments

    val prediction1 = arguments?.getString("arg1") ?: ""
    val prediction2 = arguments?.getString("arg2") ?: ""

    Text(text = prediction1)
    Text(text = prediction2)
}
