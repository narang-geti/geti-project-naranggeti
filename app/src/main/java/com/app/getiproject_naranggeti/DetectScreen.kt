package com.app.getiproject_naranggeti

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun DetectScreen(navController: NavController) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .width(150.dp) // Desired width
                    .height(80.dp) // Desired height
                    .padding(4.dp), // Optional padding
                shape = RectangleShape // Use RectangleShape to make the button rectangular
            ) {
                Text(
                    text = "Image",
                    fontSize = 30.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .width(150.dp) // Desired width
                    .height(80.dp) // Desired height
                    .padding(4.dp), // Optional padding
                shape = RectangleShape // Use RectangleShape to make the button rectangular
            ) {
                Text(
                    text = "Detect",
                    fontSize = 30.sp
                )
            }
        }

    }


}