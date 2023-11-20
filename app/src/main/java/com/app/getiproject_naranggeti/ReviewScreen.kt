//package com.app.getiproject_naranggeti
//
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.width
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavController
//import androidx.navigation.compose.currentBackStackEntryAsState
//
//@Composable
//fun ReviewScreen(navController: NavController, prediction1: String, prediction2: String) {
//    var classification by remember { mutableStateOf("") }
//    val prediction = prediction1 + prediction2
//
//    classification = when (prediction) {
//        "SS" -> "S"
//        "SA" -> "S"
//        "SB" -> "S"
//        "AS" -> "S"
//        "SF" -> "A"
//        "AA" -> "A"
//        "AB" -> "A"
//        "BS" -> "A"
//        "AF" -> "B"
//        "BA" -> "B"
//        "BB" -> "B"
//        "FS" -> "B"
//        "BF" -> "F"
//        "FA" -> "F"
//        "FB" -> "F"
//        "FF" -> "F"
//        else -> "Invalid value" // 예외 처리
//    }
//
//    Column {
//        Text(text = "앞면 추론 값 : $prediction1")
//
//        Spacer(modifier = Modifier.width(30.dp))
//
//        Text(text = "후면 추론 값: $prediction2")
//
//        Text(text = "앞뒤 더한 최종 추론 값 : $classification")
//
//    }
//}
