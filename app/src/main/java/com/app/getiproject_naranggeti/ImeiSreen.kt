package com.app.getiproject_naranggeti

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import java.io.IOException
import java.util.regex.Pattern

@Composable
fun ImeinScreen(navController: NavController) {
    //일단 텍스트 인식으로 인식을 완료
    //IMEI 숫자 부분만 가져와서
    //15자리 숫자가 인식되면
    //DB에 true false로 넣어준다
    //DB에 true가 들어가 있으면 인증마크를 띄워주는 방식으로 하자

    var selectUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            selectUri = uri

        }
    )

    val db = Firebase.firestore
    val userUID = Firebase.auth.currentUser?.uid

    var imeiValid by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

//        Button(
//            onClick = {
//                launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
//            },
//            colors = ButtonDefaults.buttonColors(
//                containerColor = MaterialTheme.colorScheme.tertiary,
//
//                ),
//            modifier = Modifier
//                .width(120.dp)
//                .height(50.dp)
//                .padding(top = 12.dp)
//        ) {
//            Text(
//                "IMEI 인증",
//                fontSize = 16.sp,
//                fontWeight = FontWeight.ExtraBold
//            )
//
//        }

        val koRecognizer = TextRecognition.getClient(KoreanTextRecognizerOptions.Builder().build())
        val context = LocalContext.current
        var trText by remember { mutableStateOf("") }

//        selectUri?.let {
//            try {
//                val image = InputImage.fromFilePath(context, it)//선택된 URI를 사용,인스턴스 생성했음
//                koRecognizer.process(image)//이미지를 텍스트 추출기에 제공하고 추출작업 수행했음
//                    .addOnSuccessListener { result ->//텍스트 추출에 성공 하면 추출된 텍스트를 처리 하는 콜백임
//                        trText = result.text//추출된 텍스트를 trText변수에 할당하고 추출된 텍스트가 화면에 표시될 수 있또록
//                    }
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//        }

//        selectUri?.let {
//            try {
//                val image = InputImage.fromFilePath(context, it)
//                koRecognizer.process(image)
//                    .addOnSuccessListener { result ->
//                        trText = result.text
//
//                        userUID?.let { uid ->
//                            val userData = hashMapOf("textExtracted" to true)
//                            db.collection("userdata").document(uid)
//                                .set(userData, SetOptions.merge())
//                                .addOnSuccessListener { Log.d("Firestore", "imei 등록 완료") }
//                                .addOnFailureListener { e -> Log.w("Firestore", "등록 실패", e) }
//                        }
//                    }
//                    .addOnFailureListener {
//                        userUID?.let { uid ->
//                            val userData = hashMapOf("textExtracted" to false)
//                            db.collection("userdata").document(uid)
//                                .set(userData, SetOptions.merge())
//                        }
//                    }
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//        }

        //IMEI가 들어있으면 true값으로 들어가도록 했음 1
//        selectUri?.let {
//            try {
//                val image = InputImage.fromFilePath(context, it)
//                koRecognizer.process(image)
//                    .addOnSuccessListener { result ->
//                        trText = result.text
//
//                        userUID?.let { uid ->
//                            val containsIMEI = trText.contains("IMEI")
//                            val userData = hashMapOf("imeiValid" to containsIMEI)
//                            db.collection("userdata").document(uid)
//                                .set(userData, SetOptions.merge())
//                                .addOnSuccessListener {
//                                    if (containsIMEI) {
//                                        Log.d("Firestore", "IMEI 인식 완료: $trText")
//                                    } else {
//                                        Log.d("Firestore", "IMEI 미발견")
//                                    }
//                                }
//                                .addOnFailureListener { e ->
//                                    Log.w("Firestore", "등록 실패", e)
//                                }
//                        }
//                    }
//                    .addOnFailureListener {
//                        userUID?.let { uid ->
//                            val userData = hashMapOf("imeiValid" to false)
//                            db.collection("userdata").document(uid)
//                                .set(userData, SetOptions.merge())
//                        }
//                    }
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//        }
        selectUri?.let {
            try {
                val image = InputImage.fromFilePath(context, it)
                koRecognizer.process(image)
                    .addOnSuccessListener { result ->
                        trText = result.text

                        userUID?.let { uid ->
                            val containsIMEI = trText.contains("IMEI")
                            val imeiData = hashMapOf("imeiValid" to containsIMEI)

                            db.collection("userdata").document(uid)
                                .set(imeiData, SetOptions.merge())
                                .addOnSuccessListener {
                                    Log.d("Firestore", "IMEI 인식 완료: $trText")
                                }
                                .addOnFailureListener { e ->
                                    Log.w("Firestore", "등록 실패", e)
                                }
                        }
                    }
                    .addOnFailureListener {
                        userUID?.let { uid ->
                            val imeiData = hashMapOf("imeiValid" to false)
                            db.collection("userdata").document(uid)
                                .set(imeiData, SetOptions.merge())
                        }
                    }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }


//
        //15자리 숫자... 실패
//        selectUri?.let {
//            try {
//                val image = InputImage.fromFilePath(context, it)
//                koRecognizer.process(image)
//                    .addOnSuccessListener { result ->
//                        trText = result.text
//
//                        // 15자리 숫자를 찾기 위한 정규 표현식
//                        val pattern = Pattern.compile("\\b\\d{15}\\b")
//                        val matcher = pattern.matcher(trText)
//
//                        if (matcher.find()) {
//                            // 15자리 숫자가 발견되면 Firestore에 true 저장
//                            userUID?.let { uid ->
//                                val userData = hashMapOf("imeiValid" to true)
//                                db.collection("userdata").document(uid)
//                                    .set(userData, SetOptions.merge())
//                                    .addOnSuccessListener { Log.d("Firestore", "imei 등록 완료") }
//                                    .addOnFailureListener { e -> Log.w("Firestore", "등록 실패", e) }
//                            }
//                        } else {
//                            // 15자리 숫자가 없으면 Firestore에 false 저장
//                            userUID?.let { uid ->
//                                val userData = hashMapOf("imeiValid" to false)
//                                db.collection("userdata").document(uid)
//                                    .set(userData, SetOptions.merge())
//                            }
//                        }
//                    }
//                    .addOnFailureListener {
//                        userUID?.let { uid ->
//                            val userData = hashMapOf("imeiValid" to false)
//                            db.collection("userdata").document(uid)
//                                .set(userData, SetOptions.merge())
//                        }
//                    }
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//        }

        Card(
            shape = MaterialTheme.shapes.medium,
            elevation = CardDefaults.cardElevation(
                defaultElevation = 12.dp
            )
        ) {
            Text(
                text = trText,
                fontSize = 20.sp,
                style = LocalTextStyle.current.merge(
                    TextStyle(
                        lineHeight = 1.5.em,
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                        ),
                        lineHeightStyle = LineHeightStyle(
                            alignment = LineHeightStyle.Alignment.Center,
                            trim = LineHeightStyle.Trim.None
                        )
                    )
                ),
            )


        }
        Button(
            onClick = {
                launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF608DBC) // HEX 색상 코드 사용
            ),
            modifier = Modifier
                .width(120.dp)
                .height(50.dp)
                .padding(top = 12.dp),
            shape = RectangleShape
        ) {
            Text(
                "IMEI 인증",
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold
            )

        }

        //여긴 저장된 true/false값 가져오는 코드임
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
        //확인하고 인증마크 띄워줌 저장된 값이 true면 인증마크 나옴
        if (imeiValid && !isLoading) {
            Image(
                painter = painterResource(id = R.drawable.imei_mark),
                contentDescription = "IMEI 인증 마크"
            )
        }




    }


}