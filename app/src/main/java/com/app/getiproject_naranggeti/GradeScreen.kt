package com.app.getiproject_naranggeti

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun GradeScreen(navController: NavController) {

    var ipFront by remember { mutableStateOf<String?>(null) }
    var ipBack by remember { mutableStateOf<String?>(null) }


    val db = Firebase.firestore


    val userUID = Firebase.auth.currentUser?.uid


    userUID?.let { uid ->
        db.collection("user").document(uid).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    ipFront = document.getString("front")
                    ipBack = document.getString("back")
                } else {
                    Log.d("Firestore", "문서 없음")
                }
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "에러", e)
            }
    }

    fun getImageResource(grade: String?): Int {
        return when (grade) {
            "S" -> R.drawable.s_grade
            "A" -> R.drawable.a_grade
            "B" -> R.drawable.b_grade
            "F" -> R.drawable.f_grade
            else -> R.drawable.somac_logo
        }
    }


    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "전면 등급: ${ipFront ?: "Loading..."}")

            ipFront?.let {
                Image(
                    painter = painterResource(id = getImageResource(it)),
                    contentDescription = "전면 등급 이미지"
                )
            }

            Text(text = "후면 등급: ${ipBack?: "Loading..."}")

            ipBack?.let {
                Image(
                    painter = painterResource(id = getImageResource(it)),
                    contentDescription = "후면 등급 이미지"
                )
            }
        }
    }
}
