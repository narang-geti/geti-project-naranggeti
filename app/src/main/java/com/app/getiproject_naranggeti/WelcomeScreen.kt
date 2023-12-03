package com.app.getiproject_naranggeti

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore

@Composable
fun WelcomeScreen(navController: NavController) {
    val scrollState = rememberScrollState()

    val auth = Firebase.auth
    val user = auth.currentUser

    var userName by remember { mutableStateOf("") }

    val userUid = user?.uid

    if (user != null) {
        user.displayName?.let {
            userName = it
        }
    }


    val db = com.google.firebase.ktx.Firebase.firestore
    val userUID = com.google.firebase.ktx.Firebase.auth.currentUser?.uid


    var imeiValid by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }

    userUID?.let { uid ->
        db.collection("userdata").document(uid).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    imeiValid = document.getBoolean("imeiValid") ?: false
                    isLoading = false
                } else {
                    Log.d("Firestore", "없음")
                    isLoading = false
                }
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "에러", e)
                isLoading = false
            }
    }

    var ipFront by remember { mutableStateOf<String?>(null) }
    var ipBack by remember { mutableStateOf<String?>(null) }


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

    val gradeScores = mapOf("S" to 100, "A" to 80, "B" to 60, "F" to 40)

    val weightedScore = (gradeScores[ipFront] ?: 0) * 0.7 + (gradeScores[ipBack] ?: 0) * 0.3

    val imageResource = when {
        weightedScore >= 86 -> R.drawable.s_grade
        weightedScore >= 74 -> R.drawable.a_grade
        weightedScore >= 58 -> R.drawable.b_grade
        else -> R.drawable.f_grade
    }


    var userData by remember { mutableStateOf<UserData?>(null) }

    // Firestore에서 데이터를 불러주자
    LaunchedEffect(key1 = user) {
        user?.uid?.let { uid ->
            db.collection("userdata").document(uid).get()
                .addOnSuccessListener { documentSnapshot ->
                    // UserData 객체에 Firestore 문서의 데이터를 매핑해줌
                    documentSnapshot.toObject(UserData::class.java)?.let { data ->
                        userData = data
                    }
                }
                .addOnFailureListener { e ->
                    Log.w("Firestore", "Error loading document", e)
                }
        }
    }



    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState),
        ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {

            // imeiValid가 true일 때만 인증 마크 표시해줍니당
            if (imeiValid && !isLoading) {
                Image(
                    painter = painterResource(id = R.drawable.imei_mark),
                    contentDescription = "IMEI 인증 마크",
                    modifier = Modifier
                        .size(32.dp)
                )
            }
            Text(
                text = userName,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
        Column {
            Row {
                Image(
                    painter = painterResource(id = imageResource),
                    contentDescription = "총점 이미지",
                    modifier = Modifier
                        .width(50.dp)
                        .height(70.dp)
                )
                userData?.let { data ->
                    Text(text = "제목: ${data.title}\n")
                    Text(text = "상세 설명: ${data.customertext}\n")
                    Text(text = "가격: ${data.price}")
                    //불러올 데이터를 더 추가할거면 여기다가 하면 됩니다
                }


            }
        }

        Spacer(modifier = Modifier.width(8.dp))


    }


}