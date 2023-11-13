package com.app.getiproject_naranggeti

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay

@Composable
fun StartScreen(navController: NavController) {

    var shouldAnimate by remember { mutableStateOf(true) }

    LaunchedEffect(shouldAnimate) {
        if (shouldAnimate) {
            delay(300)
            shouldAnimate = false
        }
    }

    Box() {

        Image(
            painter = painterResource(id = R.drawable.bg1),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .fillMaxWidth()
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.primary)


        )
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedVisibility(
                visible = !shouldAnimate,
                enter = slideInVertically(
                    initialOffsetY = { fullHeight -> -fullHeight },
                    animationSpec = tween(durationMillis = 1000, delayMillis = 300)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.somac_logo),
                    contentDescription = null,
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.medium)
                        .size(width = 600.dp, height = 250.dp)
                )
            }

            Button(
                onClick = {
                    navController.navigate("detect")
                },
                modifier = Modifier
                    .width(200.dp)
                    .height(100.dp)
            ) {
                Text(
                    text = "START",
                    fontSize = 40.sp
                )
            }

        }


    }


}