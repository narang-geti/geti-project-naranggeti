//package com.app.getiproject_naranggeti
//
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Star
//import androidx.compose.material.icons.outlined.Star
//import androidx.compose.material3.Icon
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavController
//
//
//@Composable
//fun CustomerEvaluation(navController: NavController) {
//
//    @Composable
//    fun StarReview(rating: Float) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp),
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            // 별점
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .weight(1f)
//            ) {
//                for (i in 1..5) {
//                    val star = if (i <= rating) {
//                        Icon(
//                            imageVector = Icons.Filled.Star,
//                            contentDescription = "Star",
//                        )
//                    } else {
//                        Icon(
//                            imageVector = Icons.Outlined.Star,
//                            contentDescription = "Star",
//                        )
//                    }
//
//                }
//            }
//
//            // 별점 수
//            Text(
//                text = "$rating",
//            )
//        }
//    }
//}