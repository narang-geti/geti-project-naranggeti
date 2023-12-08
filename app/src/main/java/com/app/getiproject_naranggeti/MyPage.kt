package com.app.getiproject_naranggeti

import android.media.tv.TvContract.Channels.Logo
import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.BlurredEdgeTreatment.Companion.Rectangle
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
    val CustomColor = Color(0xFF608EBD)

    if (user != null) {
        user.displayName?.let {
            userName = it
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 20.dp, end = 20.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        Row {
            Icon(
                imageVector = Icons.Outlined.AccountCircle,
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = "$userName",
                fontFamily = elice,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .padding(top = 6.dp)

            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Outlined.Settings,
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )
        }
        Spacer(modifier = Modifier.height(30.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .border(
                    width = 1.dp,
                    color = Color.LightGray,
                    shape = RoundedCornerShape(10.dp)
                ),
            horizontalAlignment = Alignment.CenterHorizontally  // 가운데 정렬 설정
        ) {
            Image(
                painter = painterResource(id = R.drawable.somac_logo),
                contentDescription = null,
                modifier = Modifier
                    .size(width = 100.dp, height = 100.dp)
            )
            Text(
                text = "0원",
                fontFamily = elice,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(10.dp))

            Row {
                ICardButton("충전", onClick = {}, CustomColor)
                ICardButton("계좌이체", onClick = {}, CustomColor)
            }
        }
        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "나의 거래",
            fontFamily = elice,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(top = 15.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row {
            Icon(
                imageVector = Icons.Outlined.FavoriteBorder, contentDescription = null,
            )

            Spacer(modifier = Modifier.width(10.dp)) // 아이콘과 텍스트 간의 간격을 조절하는 Spacer

            Text(
                text = "관심목록",
                color = Color.Black,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 3.dp),
                fontFamily = elice,
                fontWeight = FontWeight.Medium
            )
        }
        IconButton(
            onClick = {
                navController.navigate("userDetails/${userUid}")
            },
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth()
        ) {
            Row {
                Icon(imageVector = Icons.Outlined.Search, contentDescription = null)
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "판매목록",
                    color = Color.Black,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 3.dp),
                    fontFamily = elice,
                    fontWeight = FontWeight.Medium
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))

        Row {
            Icon(
                imageVector = Icons.Outlined.ShoppingCart, contentDescription = null,
            )

            Spacer(modifier = Modifier.width(10.dp)) // 아이콘과 텍스트 간의 간격을 조절하는 Spacer

            Text(
                text = "구매내역",
                color = Color.Black,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 3.dp),
                fontFamily = elice,
                fontWeight = FontWeight.Medium
            )
        }
        Spacer(modifier = Modifier.height(20.dp))

        Row {
            Icon(
                imageVector = Icons.Outlined.Add, contentDescription = null,
            )

            Spacer(modifier = Modifier.width(10.dp)) // 아이콘과 텍스트 간의 간격을 조절하는 Spacer

            Text(
                text = "모아보기",
                color = Color.Black,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 3.dp),
                fontFamily = elice,
                fontWeight = FontWeight.Medium
            )
        }
        Spacer(modifier = Modifier.height(20.dp))

        Row {
            Icon(
                imageVector = Icons.Outlined.DateRange, contentDescription = null,
            )

            Spacer(modifier = Modifier.width(10.dp)) // 아이콘과 텍스트 간의 간격을 조절하는 Spacer

            Text(
                text = "가계부",
                color = Color.Black,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 3.dp),
                fontFamily = elice,
                fontWeight = FontWeight.Medium
            )
        }

        IconButton(
            onClick = {
                auth.signOut()
                navController.navigate("login")
            },
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
        ) {
            Row {
                Icon(imageVector = Icons.Outlined.Warning, contentDescription = null)
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "로그아웃",
                    color = Color.Black,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 3.dp),
                    fontFamily = elice,
                    fontWeight = FontWeight.Medium
                )
            }
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
fun UserDetailsText(
    navController: NavController,
    userDataViewModel: UserDataViewModel,
    userDataId: String
) {
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
@Composable
fun ICardButton(text: String, onClick: () -> Unit, backgroundColor: Color) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .height(65.dp)
            .padding(8.dp)
            .border(1.dp, Color.White, RoundedCornerShape(4.dp)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF608DBC))

    ) {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(backgroundColor, contentColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                text = text,
                fontFamily = elice,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
