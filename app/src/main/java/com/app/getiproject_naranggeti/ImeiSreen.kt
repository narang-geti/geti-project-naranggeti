package com.app.getiproject_naranggeti

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import java.io.IOException

@Composable
fun ImeinScreen(navController: NavController){
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


    Column {

        Button(
            onClick = {
                launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiary,

                ),
            modifier = Modifier
                .width(120.dp)
                .height(50.dp)
                .padding(top = 12.dp)
        ) {
            Text(
                "IMEI 인증",
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold
            )

        }

        val koRecognizer = TextRecognition.getClient(KoreanTextRecognizerOptions.Builder().build())
        val context = LocalContext.current
        var trText by remember { mutableStateOf("") }

        selectUri?.let {
            try {
                val image = InputImage.fromFilePath(context, it)//선택된 URI를 사용,인스턴스 생성
                koRecognizer.process(image)//이미지를 텍스트 추출기에 제공하고 추출작업 수행
                    .addOnSuccessListener { result ->//텍스트 추출에 성공 하면 추출된 텍스트를 처리 하는 콜백
                        trText = result.text//추출된 텍스트를 trText변수에 할당하고 추출된 텍스트가 화면에 표시될 수 있또록
                    }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }


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


    }









}