package com.app.getiproject_naranggeti

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
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

    var ipFront by remember { mutableStateOf<String?>(null) }
    var ipBack by remember { mutableStateOf<String?>(null) }
    var ipLateral by remember { mutableStateOf<String?>(null) }
    var ipDown by remember { mutableStateOf<String?>(null) }

    val db = Firebase.firestore

    val userUID = Firebase.auth.currentUser?.uid


    userUID?.let { uid ->
        db.collection("userClassification").document(uid).get()
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

//    fun getImageResource(grade: String?): Int {
//        return when (grade) {
//            "S" -> R.drawable.s_grade
//            "A" -> R.drawable.a_grade
//            "B" -> R.drawable.b_grade
//            "F" -> R.drawable.f_grade
//            else -> R.drawable.somac_logo
//        }
//    }

//    val imageResource = when {
//        ipFront == "S" || ipBack == "S" -> R.drawable.s_grade
//        ipFront == "S" || ipBack == "A" -> R.drawable.s_grade
//        ipFront == "S" || ipBack == "B" -> R.drawable.s_grade
//        ipFront == "A" || ipBack == "S" -> R.drawable.s_grade
//        ipFront == "S" || ipBack == "F" -> R.drawable.a_grade
//        ipFront == "A" || ipBack == "A" -> R.drawable.a_grade
//        ipFront == "A" || ipBack == "B" -> R.drawable.a_grade
//        ipFront == "B" || ipBack == "S" -> R.drawable.a_grade
//        ipFront == "A" || ipBack == "F" -> R.drawable.b_grade
//        ipFront == "B" || ipBack == "A" -> R.drawable.b_grade
//        ipFront == "B" || ipBack == "B" -> R.drawable.b_grade
//        ipFront == "F" || ipBack == "S" -> R.drawable.b_grade
//        ipFront == "B" || ipBack == "F" -> R.drawable.f_grade
//        ipFront == "F" || ipBack == "A" -> R.drawable.f_grade
//        ipFront == "F" || ipBack == "B" -> R.drawable.f_grade
//        ipFront == "F" || ipBack == "F" -> R.drawable.f_grade
//        else -> R.drawable.somac_logo
//    }


    val gradeScores = mapOf("S" to 100, "A" to 80, "B" to 60, "F" to 40)

    val weightedScore = (gradeScores[ipFront] ?: 0) * 0.4 + (gradeScores[ipBack] ?: 0) * 0.3 + (gradeScores[ipLateral] ?: 0) * 0.2 + (gradeScores[ipDown] ?: 0) * 0.1

    val imageResource = when {
        weightedScore >= 90 -> R.drawable.s_grade
        weightedScore >= 70 -> R.drawable.a_grade
        weightedScore >= 50 -> R.drawable.b_grade
        else -> R.drawable.f_grade
    }



        Box(
            modifier = Modifier.padding(16.dp),
        ) {
            Card() {

                Text(
                    text = "somac 인증서를 통해 소형 전자기기\n 중고거래 신뢰도를 높일 수 있습니다.",
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    fontFamily = elice
                )

            }




            Image(
                painter = painterResource(id = imageResource),
                contentDescription = "총점 이미지",
                modifier = Modifier.fillMaxSize()
            )
        }
    }

//            Text(text = "전면 등급: ${ipFront ?: ""}")
//
//            ipFront?.let {
//                Image(
//                    painter = painterResource(id = getImageResource(it)),
//                    contentDescription = "전면 등급 이미지"
//                )
//            }
//
//            Text(text = "후면 등급: ${ipBack?: ""}")
//
//            ipBack?.let {
//                Image(
//                    painter = painterResource(id = getImageResource(it)),
//                    contentDescription = "후면 등급 이미지"
//                )
//            }
//            Text(text = "총점 등급: ${ipFront}${ipBack}")
