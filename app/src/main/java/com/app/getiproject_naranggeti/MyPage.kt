package com.app.getiproject_naranggeti

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.app.getiproject_naranggeti.ui.theme.elice
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun UserDataScreen(navController: NavController, userDataViewModel: UserDataViewModel) {
    // Firebase Authentication 인스턴스 가져오기
    val auth = Firebase.auth
    // 현재 인증된 사용자 가져오기
    val user = auth.currentUser
    // 사용자 이름을 저장할 상태(State) 정의
    var userName by remember { mutableStateOf("") }
    // 사용자 UID를 가져오기
    val userUid = user?.uid
    // 사용자 데이터 가져오기
    val userDatasState = userDataViewModel.userDatas.collectAsState()
    val userDatas: List<UserData> = userDatasState.value
    var showUserDetails by remember { mutableStateOf(false) }

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
                navController.navigate("userDetails/${userUid}")
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("판매 목록")
        }

        if (showUserDetails) {
            UserDetails(navController = navController, userDatas = userDatas, userUid = userUid)
        }

        // 로그아웃 버튼
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

// 판매 제목 !!!
@Composable
fun UserDetails(navController: NavController, userDatas: List<UserData>, userUid: String?) {
    val CustomColor = Color(0xFF608EBD)

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            userDatas
                .filter { it.uid == userUid }
                .forEach { userData ->
                    CardButton(
                        text = "Title: ${userData.title}",
                        onClick = { navController.navigate("UserDetailsText/${userData.uid}") },
                        CustomColor
                    )
                }
        }
    }
}
//판매 상세 페이지
@Composable
fun UserDetailsText(navController: NavController, userDataViewModel: UserDataViewModel, userDataId: String) {
    val userDatasState = userDataViewModel.userDatas.collectAsState()
    val userDatas: List<UserData> = userDatasState.value
    val userData = userDatas.find { it.uid == userDataId }

    if (userData != null) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(text = "Buy Date: ${userData.buydate}")
                Text(text = "Storage: ${userData.storage}")
                Text(text = "Battery Efficiency: ${userData.batteryefficiency}")
                Text(text = "Price: ${userData.price}")
                Text(text = "Apple Care: ${userData.applecare}")
                Text(text = "Custom Text: ${userData.customertext}")
            }
        }
    }
}

//firebase에서 데이터 가져오기
class UserDataViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val userDataCollection = db.collection("userdata")
    private val _userDatas = MutableStateFlow<List<UserData>>(emptyList())
    val userDatas: StateFlow<List<UserData>> get() = _userDatas

    init {
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val querySnapshot = userDataCollection.get().await()
                val dataList = mutableListOf<UserData>()
                for (document in querySnapshot.documents) {
                    val userData = document.toObject(UserData::class.java)
                    if (userData != null) {
                        dataList.add(userData)
                    }
                }
                _userDatas.value = dataList
            } catch (e: Exception) {
                Log.e("Firestore", "Error fetching data", e)
            }
        }
    }
}
