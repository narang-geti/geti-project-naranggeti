package com.app.getiproject_naranggeti

import android.util.Log
import android.widget.RatingBar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.app.getiproject_naranggeti.ui.theme.Purple40
import com.app.getiproject_naranggeti.ui.theme.elice
import com.google.android.libraries.places.api.model.Review
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerEvaluation(navController: NavController) {
    var userTextReview by remember { mutableStateOf("") }
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val database = FirebaseDatabase.getInstance()
    val reviewsRef: DatabaseReference = database.getReference("reviews")
    val rating: MutableState<Float> = remember { mutableStateOf(0.0f) }
    val CustomColor = Color(0xFF608EBD)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Purple40
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RatingBar(
                rating = rating.value,
                onRatingChanged = { newRating ->
                    rating.value = newRating
                }
            )
            OutlinedTextField(
                value = userTextReview,
                onValueChange = { userTextReview = it },
                label = { Text("소맥 등급 분류 서비스에 대한 리뷰를 남겨주세요 :) ") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(200.dp),

                )

            Button(
                onClick = {
                    val currentUser = auth.currentUser
                    if (currentUser != null) {
                        val review = MyReview(
                            userId = currentUser.displayName ?: "",
                            userUid = currentUser.uid,
                            textReview = userTextReview,
                            starRating = rating.value
                        )
                        writeReview(review)
                    }
                },
                modifier = Modifier
                    .width(150.dp)
                    .height(100.dp),
                colors = ButtonDefaults.buttonColors(CustomColor)
            ) {
                Text(
                    text = "리뷰 작성",
                    fontSize = 20.sp,
                    fontFamily = elice,
                    fontWeight = FontWeight.Bold
                )
            }

        }
    }
}

data class MyReview(
    val userId: String = "",
    val userUid: String = "",
    val textReview: String = "",
    val starRating: Float = 0.0f
)

@Composable
fun RatingBar(
    rating: Float,
    onRatingChanged: (Float) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.Center  // 가운데 정렬을 설정합니다.
    ) {
        for (i in 0 until 5) {
            val starRating = i + 1
            val isSelected = starRating <= rating
            val iconColor = if (isSelected) Color(0XFFEDD500) else Color.Gray

            IconButton(
                onClick = { onRatingChanged(starRating.toFloat()) },
                modifier = Modifier.size(60.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "별점 $starRating",
                    tint = iconColor,
                    modifier = Modifier.size(50.dp)
                )
            }
        }
    }
}

fun writeReview(review: MyReview) {
    val database = FirebaseDatabase.getInstance()
    val reviewsRef: DatabaseReference = database.getReference("reviews")
    val newReviewRef = reviewsRef.push()
    newReviewRef.setValue(review)
}
