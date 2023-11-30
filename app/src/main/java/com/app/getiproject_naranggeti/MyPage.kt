package com.app.getiproject_naranggeti

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun UserInfo(navController: NavController) {
    // Firebase Authentication 인스턴스 가져오기
    val auth = Firebase.auth

    // 현재 인증된 사용자 가져오기
    val user = auth.currentUser

    // 사용자 이름을 저장할 상태(State) 정의
    var userName by remember { mutableStateOf("") }

    // 사용자 UID를 가져오기
    val userUid = user?.uid

    // 사용자 이름 가져오기
    if (user != null) {
        user.displayName?.let {
            userName = it
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 사용자 이름 표시
        Text(
            text = "안녕하세요, $userName 님!",
            style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
        )

        Button(
            onClick = {
                auth.signOut()
                navController.navigate("login")
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("로그아웃")
        }
    }
}
