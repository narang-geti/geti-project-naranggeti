package com.app.getiproject_naranggeti

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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

    //firestore 사용
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

        Spacer(modifier = Modifier.width(8.dp))


    }


}