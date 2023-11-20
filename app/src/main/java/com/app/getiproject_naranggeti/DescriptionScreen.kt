package com.app.getiproject_naranggeti

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.app.getiproject_naranggeti.R
import com.app.getiproject_naranggeti.ui.theme.elice

@Composable
fun DescriptionScreen(navController: NavHostController) {

    Box(){

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            Card {

                    Text(
                        text = "단순 참고용 등급 분류 기준 입니다.",
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        fontFamily = elice
                    )

            }

            Spacer(modifier = Modifier.height(12.dp))

            Image(
                painter = painterResource(id = R.drawable.description),
                contentDescription = "총점 이미지",
                modifier = Modifier
                    .width(700.dp)
                    .height(1000.dp)
            )

        }


    }

}