package com.app.getiproject_naranggeti

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore

@Composable
fun WelcomeScreen(navController: NavController) {
    val scrollState = rememberScrollState()

    val auth = Firebase.auth
    val user = auth.currentUser
    val db = Firebase.firestore

    var userDataList by remember { mutableStateOf<List<UserData>>(emptyList()) }

    LaunchedEffect(key1 = Unit) {
        db.collection("userdata").get()
            .addOnSuccessListener { querySnapshot ->
                userDataList = querySnapshot.documents.mapNotNull { document ->
                    document.toObject(UserData::class.java)
                }
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error fetching data", e)
            }
    }


    var userName by remember { mutableStateOf("") }

    val userUid = user?.uid

    if (user != null) {
        user.displayName?.let {
            userName = it
        }
    }


//    val db = com.google.firebase.ktx.Firebase.firestore
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

//    var ipFront by remember { mutableStateOf<String?>(null) }
//    var ipBack by remember { mutableStateOf<String?>(null) }
//
//
//    userUID?.let { uid ->
//        db.collection("user").document(uid).get()
//            .addOnSuccessListener { document ->
//                if (document != null) {
//                    ipFront = document.getString("front")
//                    ipBack = document.getString("back")
//                } else {
//                    Log.d("Firestore", "문서 없음")
//                }
//            }
//            .addOnFailureListener { e ->
//                Log.w("Firestore", "에러", e)
//            }
//    }

//    val gradeScores = mapOf("S" to 100, "A" to 80, "B" to 60, "F" to 40)
//
//    val weightedScore = (gradeScores[ipFront] ?: 0) * 0.7 + (gradeScores[ipBack] ?: 0) * 0.3
//
//    val imageResource = when {
//        weightedScore >= 86 -> R.drawable.s_grade
//        weightedScore >= 74 -> R.drawable.a_grade
//        weightedScore >= 58 -> R.drawable.b_grade
//        else -> R.drawable.f_grade
//    }


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
                    Log.w("Firestore", "에러", e)
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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Card(
                shape = RoundedCornerShape(10.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier.padding(8.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Button(
                        onClick = { /*TODO*/ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF608DBC)
                        ),
                        modifier = Modifier
                            .width(80.dp)
                            .height(30.dp),
                        shape = RectangleShape
                    ) {
                        Text(text = "아이폰", fontSize = 10.sp,fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { /*TODO*/ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF608DBC)
                        ),
                        modifier = Modifier
                            .width(80.dp)
                            .height(30.dp),
                        shape = RectangleShape
                    ) {
                        Text(text = "맥북", fontSize = 10.sp,fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { /*TODO*/ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF608DBC)
                        ),
                        modifier = Modifier
                            .width(80.dp)
                            .height(30.dp),
                        shape = RectangleShape
                    ) {
                        Text(text = "아이패드", fontSize = 8.sp,fontWeight = FontWeight.Bold)
                    }
                }
            }

        }

        Column {

            userDataList.forEach { userData ->
                UserDataCard(userData)
            }

        }

    }

    Spacer(modifier = Modifier.width(8.dp))


}


@Composable
fun UserDataCard(userData: UserData) {

    val gradeScores = mapOf("S" to 100, "A" to 80, "B" to 60, "F" to 40)

//    val weightedScore = (gradeScores[ipFront] ?: 0) * 0.4 + (gradeScores[ipBack]
//        ?: 0) * 0.3 + (gradeScores[ipLateral] ?: 0) * 0.2 + (gradeScores[ipDown] ?: 0) * 0.1

    val weightedScore =
        (gradeScores[userData.front] ?: 0) * 0.4 + (gradeScores[userData.back] ?: 0) * 0.3+ (gradeScores[userData.lateral] ?: 0) * 0.2+(gradeScores[userData.down] ?: 0) * 0.1



    val imageResource = when {
        weightedScore >= 80 -> R.drawable.s_grade
        weightedScore >= 60 -> R.drawable.a_grade
        weightedScore >= 40 -> R.drawable.b_grade
//        weightedScore >= 0 -> R.drawable.f_grade
        else -> R.drawable.f_grade
    }


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, Color.White, RoundedCornerShape(4.dp)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xBABAD1EB))
    ) {

        Row {

            Image(
                painter = painterResource(id = imageResource),
                contentDescription = "Grade Image",
                modifier = Modifier
                    .size(100.dp)
                    .padding(2.dp)
            )

            if (userData.imeiValid == true) {
                Image(
                    painter = painterResource(id = R.drawable.imei_certificate),
                    contentDescription = "IMEI 인증 마크",
                    modifier = Modifier
                        .size(100.dp)
                        .padding(2.dp)
                )
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "제목: ${userData.title}",
                    fontSize = 14.sp)
                Text(text = "상세 설명: ${userData.customertext}",
                    fontSize = 14.sp)
                Text(text = "가격: ${userData.price}",
                    fontSize = 14.sp)
                Text(text = "배터리 성능: ${userData.batteryefficiency}",
                    fontSize = 14.sp)
            }

        }

    }
}
