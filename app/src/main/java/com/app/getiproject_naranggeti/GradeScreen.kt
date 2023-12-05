package com.app.getiproject_naranggeti

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.app.getiproject_naranggeti.ui.theme.elice
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun GradeScreen(navController: NavController) {
    val CustomColor = Color(0xFF608EBD)
    var ipFront by remember { mutableStateOf<String?>(null) }
    var ipBack by remember { mutableStateOf<String?>(null) }
    var ipLateral by remember { mutableStateOf<String?>(null) }
    var ipDown by remember { mutableStateOf<String?>(null) }

    val db = Firebase.firestore

    val userUID = Firebase.auth.currentUser?.uid


    userUID?.let { uid ->
        db.collection("userdata").document(uid).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    ipFront = document.getString("front")
                    ipBack = document.getString("back")
                    ipLateral = document.getString("lateral")
                    ipDown = document.getString("down")
                } else {
                    Log.d("Firestore", "문서 없음")
                }
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "에러", e)
            }
    }


    val gradeScores = mapOf("S" to 100, "A" to 80, "B" to 60, "F" to 30)

    val weightedScore = (gradeScores[ipFront] ?: 0) * 0.4 + (gradeScores[ipBack]
        ?: 0) * 0.3 + (gradeScores[ipLateral] ?: 0) * 0.2 + (gradeScores[ipDown] ?: 0) * 0.1

    val imageResource = when {
        weightedScore >= 80 -> R.drawable.s_grade
        weightedScore >= 60 -> R.drawable.a_grade
        weightedScore >= 40 -> R.drawable.b_grade
//        weightedScore >= 0 -> R.drawable.f_grade
        else -> R.drawable.f_grade
    }

    val scrollState = rememberScrollState()


    Column(
        modifier = Modifier
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        Card() {

            Text(
                text = "somac 인증서를 통해 소형 전자기기\n 중고거래 신뢰도를 높일 수 있습니다.",
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                fontFamily = elice
            )

        }

        Spacer(modifier = Modifier.height(50.dp))

        Image(
            painter = painterResource(id = imageResource),
            contentDescription = "총점 이미지",
            modifier = Modifier.fillMaxSize()
        )

        Spacer(modifier = Modifier.height(50.dp))

        Card(
            modifier = Modifier
                .width(300.dp)
                .padding(8.dp)
                .border(1.dp, Color.White, RoundedCornerShape(4.dp)),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF608DBC))

        ) {
            Button(
                onClick = { navController.navigate("product/${imageResource.toString()}")},
                colors = ButtonDefaults.buttonColors(CustomColor, contentColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .width(100.dp)
                    .height(50.dp)
            ) {
                Text(
                    text = "Click",
                    fontFamily = elice,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}



