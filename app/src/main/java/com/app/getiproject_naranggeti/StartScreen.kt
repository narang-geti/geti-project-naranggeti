package com.app.getiproject_naranggeti

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.app.getiproject_naranggeti.ui.theme.elice
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

@Composable
fun StartScreen() {


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
            val CustomColor = Color(0xFF608EBD) // 색상 정의

            Button(
                onClick = {
//                    navController.navigate("login")
                },
                modifier = Modifier
                    .width(200.dp)
                    .height(100.dp),
                colors = ButtonDefaults.buttonColors(CustomColor),
                shape = RectangleShape

            ) {
                Text(
                    text = "START",
                    fontSize = 30.sp,
                    fontFamily = elice,
                    fontWeight = FontWeight.Bold,

                    )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginSuccess: (Boolean) -> Unit,
    navController: NavController
) {
    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }

    var shouldAnimate by remember { mutableStateOf(true) }

    LaunchedEffect(shouldAnimate) {
        if (shouldAnimate) {
            delay(300)
            shouldAnimate = false
        }
    }

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

        val CustomColor = Color(0xFF608EBD)

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Button(
                onClick = {
                    viewModel.login(emailState.value, passwordState.value) { success ->
                        if (success) {
                            navigateToDetectScreen(navController)
                        } else {
                            viewModel.errorMessage = "로그인에 실패했습니다. 다시 시도해주세요."
                        }
                    }
                },

                colors = ButtonDefaults.buttonColors(CustomColor, contentColor = Color.White)
            ) {
                Text(text = "Sign In")
            }

            Button(
                onClick = {
                    navController.navigate("Sign Up")
                },
                colors = ButtonDefaults.buttonColors(CustomColor, contentColor = Color.White)
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
                    painter = painterResource(id = R.drawable.somac_logo),
                    contentDescription = null,
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.medium)
                        .size(width = 300.dp, height = 250.dp)
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

    fun login(email: String, password: String, onResult: (Boolean) -> Unit) {
        if (email.isNotBlank() && password.isNotBlank()) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onResult(true)
                    } else {
                        onResult(false)
                    }
                }
        } else {
            onResult(false)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var verificationCode by remember { mutableStateOf("") }
    var verificationId by remember { mutableStateOf<String?>(null) }
    var isVerificationCodeSent by remember { mutableStateOf(false) }
    var signupErrorMessage by remember { mutableStateOf("") }
    val auth: FirebaseAuth = Firebase.auth
    val CustomColor = Color(0xFF608EBD)
    val context = LocalContext.current // context 변수 선언
    val verificationCredential = remember { mutableStateOf<PhoneAuthCredential?>(null) }
    val verificationFailedException = remember { mutableStateOf<FirebaseException?>(null) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 76.dp, start = 16.dp, end = 16.dp, bottom = 0.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.iphone),
            contentDescription = "Profile Image",
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
                .align(Alignment.CenterHorizontally)
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("닉네임을 입력하세요") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("이메일을 입력하세요") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("비밀번호를 입력하세요") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 추가: 전화 번호 입력 필드
        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("전화 번호를 입력하세요") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
        )

        // 추가: 전화 인증 코드 입력 필드
        if (isVerificationCodeSent) {
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = verificationCode,
                onValueChange = { verificationCode = it },
                label = { Text("인증 코드를 입력하세요") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (!isVerificationCodeSent) {
                    val modifiedPhoneNumber = phoneNumber82(phoneNumber)
                    val options = PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(modifiedPhoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(context as Activity)
                        .setCallbacks(
                            object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                                    verificationCredential.value = credential
                                }

                                override fun onVerificationFailed(e: FirebaseException) {
                                    verificationFailedException.value = e
                                }

                                override fun onCodeSent(
                                    sentVerificationId: String,
                                    token: PhoneAuthProvider.ForceResendingToken
                                ) {
                                    verificationId = sentVerificationId
                                    isVerificationCodeSent = true
                                }
                            }
                        )
                        .build()

                    PhoneAuthProvider.verifyPhoneNumber(options)
                } else {
                    val credential =
                        PhoneAuthProvider.getCredential(verificationId!!, verificationCode)
                    signInWithPhoneAuthCredential(
                        auth,
                        credential,
                        navController,
                        context,
                        name,
                        email,
                        password
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = ButtonDefaults.buttonColors(
                CustomColor,
                contentColor = Color.White
            )
        ) {
            // 버튼 텍스트 변경
            if (isVerificationCodeSent) {
                Text("확인")
            } else {
                Text("인증 코드 요청")
            }
        }

        if (signupErrorMessage.isNotEmpty()) {
            Text(
                text = signupErrorMessage,
                color = Color.Red,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

private fun signInWithPhoneAuthCredential(
    auth: FirebaseAuth, credential: PhoneAuthCredential, navController: NavController,
    context: Context, name: String, email: String, password: String
) {
    GlobalScope.launch {
        try {
            val result = auth.signInWithCredential(credential).await()
            if (result.user != null) {
                // 전화 인증에 성공한 경우의 처리
                // 회원가입 진행
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = FirebaseAuth.getInstance().currentUser
                            val profileUpdates = UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .build()
                            user?.updateProfile(profileUpdates)
                                ?.addOnCompleteListener { updateProfileTask ->
                                    if (updateProfileTask.isSuccessful) {
                                        runBlocking {
                                            navigateToLoginScreen(navController)
                                        }
                                    } else {
                                        runBlocking {
                                            Toast.makeText(
                                                context,
                                                "회원가입에 실패했습니다\n입력창을 확인하십시오",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                }
                        } else {
                            runBlocking {
                                Toast.makeText(
                                    context,
                                    "회원가입에 실패했습니다\n입력창을 확인하십시오",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
            } else {
                // 전화 인증에 실패한 경우의 처리
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context,
                        "전화 인증에 실패했습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } catch (e: Exception) {
            // 전화 인증에 실패한 경우의 처리
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    "전화 인증에 실패했습니다: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}

fun navigateToDetectScreen(navController: NavController) {
    navController.navigate("menu")
}

fun navigateToLoginScreen(navController: NavController) {
    navController.navigate("login")
}

//+82 10-1234-1234로 인증 해야되는데 그걸 010-1234-1234 처럼 써도 되게 해주는 함수
fun phoneNumber82(msg: String): String {
    val firstNumber: String = msg.substring(0, 3)
    var phoneEdit = msg.substring(3)

    when (firstNumber) {
        "010" -> phoneEdit = "+8210$phoneEdit"
    }
    Log.d("국가코드로 변경된 번호 ", phoneEdit)
    return phoneEdit
}