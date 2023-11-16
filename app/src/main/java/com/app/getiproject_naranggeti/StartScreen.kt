package com.app.getiproject_naranggeti

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
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
                    navController.navigate("login")
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(viewModel: LoginViewModel, onLoginSuccess: (Boolean) -> Unit, navController: NavController) {
    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 76.dp, start = 16.dp, end = 16.dp, bottom = 0.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        OutlinedTextField(
            value = emailState.value,
            onValueChange = { emailState.value = it },
            label = { Text("Email") },
            leadingIcon = {
                Icon(imageVector = Icons.Outlined.Email, contentDescription = null)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        OutlinedTextField(
            value = passwordState.value,
            onValueChange = { passwordState.value = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            leadingIcon = {
                Icon(imageVector = Icons.Outlined.Check, contentDescription = null)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Button(
                onClick = {
                    viewModel.login(emailState.value, passwordState.value)
                },
                colors = ButtonDefaults.buttonColors(
                    Color.Black,
                    contentColor = Color.White
                )
            ) {
                Text(text = "Sign In")
            }

            Button(
                onClick = {

                },
                colors = ButtonDefaults.buttonColors(
                    Color.Black,
                    contentColor = Color.White
                )
            ) {
                Text(text = "Sign Up")
            }
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    val email = emailState.value
                    if (email.isNotEmpty()) {
                        viewModel.sendPasswordResetEmail(email)
                    } else {
                        viewModel.errorMessage = "이메일을 입력해주세요."
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    Color.Transparent,
                    contentColor = Color.Gray
                )
            ) {
                Text(
                    text = "Forgot Password",
                    textDecoration = TextDecoration.Underline,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        if (viewModel.errorMessage.isNotEmpty()) {
            Text(
                text = viewModel.errorMessage,
                color = Color.Red,
                modifier = Modifier.padding(8.dp)
            )
        }

        if (viewModel.loginSuccess) {
            onLoginSuccess(true)
        }
    }
}


class LoginViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()

    var errorMessage by mutableStateOf("")
    var loginSuccess by mutableStateOf(false)

    fun login(email: String, password: String) {
        if (email.isNotBlank() && password.isNotBlank()) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        loginSuccess = true
                    } else {
                        errorMessage = "로그인에 실패했습니다. 다시 시도해주세요."
                    }
                }
        } else {
            errorMessage = "이메일과 비밀번호를 입력해주세요."
        }
    }

    fun sendPasswordResetEmail(email: String) {
        if (email.isNotBlank()) {
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        errorMessage = "비밀번호 재설정 이메일을 보냈습니다. 이메일을 확인해주세요."
                    } else {
                        errorMessage = "비밀번호 재설정 이메일 전송에 실패했습니다. \n다시 시도해주세요."
                    }
                }
        } else {
            errorMessage = "이메일을 입력해주세요."
        }
    }
}