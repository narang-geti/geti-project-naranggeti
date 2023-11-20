package com.app.getiproject_naranggeti

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.app.getiproject_naranggeti.ui.theme.elice
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class Review(
    val userId: String = "",
    val userUid: String = "",
    val textReview: String = "",
    val starRating: Float = 0.0f
)

@Composable
fun CustomerReviewScreen(navController: NavHostController) {
    val reviews by loadReviews().collectAsState(initial = emptyList())

    LazyColumn {
        items(reviews) { review ->
            ReviewItem(review)
        }
    }
}

@Composable
fun ReviewItem(review: Review) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),

            ) {
                Text(
                    "고객명: ${review.userId}",
                    fontFamily = elice,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "별점: ${review.starRating}",
                    fontFamily = elice,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "리뷰: ${review.textReview}",
                    fontFamily = elice,
                    fontSize = 20.sp
                )
            }
        }
    }
}

fun loadReviews(): StateFlow<List<Review>> {
    val reviewsFlow = MutableStateFlow<List<Review>>(emptyList())
    val database = FirebaseDatabase.getInstance()
    val reviewsRef = database.getReference("reviews")

    CoroutineScope(Dispatchers.IO).launch {
        val snapshot = reviewsRef.get().await()
        val reviews = snapshot.children.mapNotNull { it.getValue<Review>() }
        reviewsFlow.value = reviews
    }

    return reviewsFlow
}
