package com.app.getiproject_naranggeti

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.app.getiproject_naranggeti.ui.theme.elice
import kotlinx.coroutines.delay

@Composable
fun MenuScreen(navController: NavController) {

    val scrollState = rememberScrollState()
    val CustomColor = Color(0xFF608EBD)
    var shouldAnimate by remember { mutableStateOf(true) }

    LaunchedEffect(shouldAnimate) {
        if (shouldAnimate) {
            delay(300)
            shouldAnimate = false
        }
    }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        AnimatedVisibility(
            visible = !shouldAnimate,
            enter = slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(durationMillis = 1000, delayMillis = 300)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        ) {
            Image(
                painter = painterResource(id = R.drawable.somacmenu_logo),
                contentDescription = null,
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .size(width = 200.dp, height = 120.dp)
            )
        }

        CardButton("올라온 상품", onClick = { navController.navigate("welcome") }, CustomColor)
        CardButton("상품 등록", onClick = { navController.navigate("product/{imageResource}") }, CustomColor)
        CardButton("IMEI 인증 마크", onClick = { navController.navigate("imei") }, CustomColor)
//        CardButton("등급 분류 서비스", onClick = { navController.navigate("detect") }, CustomColor)
        CardButton("등급 분류 기준", onClick = { navController.navigate("description") }, CustomColor)
        CardButton("고객 만족 평가", onClick = { navController.navigate("customer") }, CustomColor)
        CardButton("고객 리뷰 모음", onClick = { navController.navigate("info") }, CustomColor)
    }
}


@Composable
fun CardButton(text: String, onClick: () -> Unit, backgroundColor: Color) {
    Card(
        modifier = Modifier
            .width(300.dp)
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
