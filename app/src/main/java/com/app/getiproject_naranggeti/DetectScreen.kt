package com.app.getiproject_naranggeti

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.ml.modeldownloader.CustomModel
import com.google.firebase.ml.modeldownloader.CustomModelDownloadConditions
import com.google.firebase.ml.modeldownloader.DownloadType
import com.google.firebase.ml.modeldownloader.FirebaseModelDownloader
import org.tensorflow.lite.Interpreter
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.nio.ByteBuffer
import java.nio.ByteOrder
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import java.io.FileNotFoundException
import java.lang.Math.exp
import java.nio.FloatBuffer
import kotlin.math.exp


@Composable
fun DetectScreen(navController: NavController) {
    val context = LocalContext.current
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var interpreter by remember { mutableStateOf<Interpreter?>(null) }
    var prediction1 by remember { mutableStateOf("") }
    var label1 by remember { mutableStateOf("") }
    var prediction2 by remember { mutableStateOf("") }

    var prediction3 by remember { mutableStateOf("") }

    var label2 by remember { mutableStateOf("") }
    var predictionNew by remember { mutableStateOf("") }
    val bitmapFromResource1: Bitmap =
        BitmapFactory.decodeResource(context.resources, R.drawable.aphone)
    val bitmapFromResource2: Bitmap =
        BitmapFactory.decodeResource(context.resources, R.drawable.fphoto)
    val bitmapFromResource3: Bitmap =
        BitmapFactory.decodeResource(context.resources, R.drawable.bphone)
//    val detectImage by remember { mutableStateOf<Bitmap?>(null)}

    var image2 by remember { mutableStateOf<Bitmap?>(null)}


    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            image2  = uriToBitmap(uri!!, context)
        }
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview(),
        onResult = { photo ->
            image2 = photo
        }
    )
    val resources = context.resources
    val defaultImageBitmap = BitmapFactory.decodeResource(resources, R.drawable.camera).asImageBitmap()

    DisposableEffect(Unit) {
        val modelConditions = CustomModelDownloadConditions.Builder()
            .requireWifi()  // Also possible: .requireCharging() and .requireDeviceIdle()
            .build()


        FirebaseModelDownloader.getInstance()
            .getModel("ipf1", DownloadType.LOCAL_MODEL_UPDATE_IN_BACKGROUND, modelConditions)
            .addOnSuccessListener { model: CustomModel? ->
                val modelFile = model?.file
                if (modelFile != null) {
                    interpreter = Interpreter(modelFile)
                }

                if (bitmapFromResource1 !=null && bitmapFromResource2 != null) {
                    val input1 = preprocessImage(bitmapFromResource1)
                    val input2 = preprocessImage(bitmapFromResource2)
                    val input3 = preprocessImage(bitmapFromResource3)

                    val bufferSize = 4 * java.lang.Float.SIZE / java.lang.Byte.SIZE
                    val modelOutput1 =
                        ByteBuffer.allocateDirect(bufferSize).order(ByteOrder.nativeOrder())
                    val modelOutput2 =
                        ByteBuffer.allocateDirect(bufferSize).order(ByteOrder.nativeOrder())
                    val modelOutput3 =
                        ByteBuffer.allocateDirect(bufferSize).order(ByteOrder.nativeOrder())


                    interpreter?.run(input1, modelOutput1)
                    interpreter?.run(input2, modelOutput2)
                    interpreter?.run(input3, modelOutput3)

                    modelOutput1.rewind()
//                    val probabilities1 = modelOutput1.asFloatBuffer()
                    // 소프트맥스 함수를 사용하여 확률 벡터 얻기
                    val probabilities1 = FloatArray(4)
                    softmax(modelOutput1.asFloatBuffer(), probabilities1)

                    // 최대 확률을 가진 클래스 선택
//                    val maxProbability = probabilities1.maxOrNull()
//                    val predictedClassIndex = probabilities1.indexOf(maxProbability)
                    val predictedClassIndex1 = findIndexOfMax(probabilities1)

                    // 클래스 레이블 매핑
                    val classLabels = arrayOf("S", "A", "B", "F")
                    val predictedClassLabel1 = classLabels[predictedClassIndex1]
                    prediction1 = predictedClassLabel1

//                    try {
//                        val reader = BufferedReader(
//                            InputStreamReader(context.resources.openRawResource(R.raw.organic))
//                        )
////                        val inputStream = context.resources.openRawResource("organic.txt")
////                        val reader = BufferedReader(InputStreamReader(inputStream))
//                        for (i in 0 until probabilities1.capacity()) {
//                            val label: String = reader.readLine()
//                            val probability = probabilities1.get(i)
//                            println("$label: $probability")
//                            label1 = label
//                            prediction1 = probability.toString()
//                        }
//                    } catch (e: IOException) {
//                        // Handle the exception
//                        e.printStackTrace()
//                    }

                    modelOutput2.rewind()
                    val numClasses = 4
                    val probabilities2 = FloatArray(4)
//                    softmax(modelOutput2.asFloatBuffer(), probabilities2)  <- try문 사용하면 softmax함수 사용할 필요없음

                    try {
                        val reader = BufferedReader(
                            InputStreamReader(context.resources.openRawResource(R.raw.organic))
                        )

                        // probabilities.capacity() 대신 numClasses를 사용하도록 변경
                        for (i in 0 until numClasses) {
                            // 클래스 레이블 읽어오기
                            val label: String = reader.readLine()

                            // 확률 값 읽어오기
                            val probability = probabilities2.get(i)

                            // 클래스 레이블과 확률 출력
                            println("$label: $probability")

                            // 각 클래스에 대한 확률을 배열에 저장
                            probabilities2[i] = probability
                        }
                    } catch (e: IOException) {
                        // 예외 처리
                        e.printStackTrace()
                    }

                    modelOutput3.rewind()
                    val probabilities3 = FloatArray(4)
                    softmax(modelOutput3.asFloatBuffer(), probabilities3)

                    // 최대 확률을 가진 클래스 선택
//                    val maxProbability = probabilities1.maxOrNull()
//                    val predictedClassIndex = probabilities1.indexOf(maxProbability)
                    val predictedClassIndex = findIndexOfMax(probabilities3)

                    // 클래스 레이블 매핑
                    val predictedClassLabel = classLabels[predictedClassIndex]
                    prediction3 = predictedClassLabel

//                    modelOutputNew.rewind()
//                    val probabilitiesNew = modelOutputNew.asFloatBuffer()
//                    try {
//                        val reader = BufferedReader(
//                            InputStreamReader(context.resources.openRawResource(R.raw.organic))
//                        )
////                        val inputStream = context.resources.openRawResource("organic.txt")
////                        val reader = BufferedReader(InputStreamReader(inputStream))
//                        for (i in 0 until probabilitiesNew.capacity()) {
//                            val label: String = reader.readLine()
//                            val probability = probabilitiesNew.get(i)
//                            println("$label: $probability")
//                            label2 = label
//                            predictionNew = probability.toString()
//                        }
//                    } catch (e: IOException) {
//                        // Handle the exception
//                        e.printStackTrace()
//                    }

                }
            }

        // Clean up resources when the composable is disposed
        onDispose {
            interpreter?.close()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(modifier = Modifier
                .size(300.dp),
                bitmap = image2?.asImageBitmap() ?: defaultImageBitmap, contentDescription = "image"
                )

            Row {
                Button(
                    onClick = { cameraLauncher.launch(null) },
                    modifier = Modifier
                        .width(100.dp)
                        .height(80.dp)
                        .padding(4.dp),
                    shape = RectangleShape
                ) {
                    Text(
                        text = "Camera",
                        fontSize = 10.sp
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = { launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
                    modifier = Modifier
                        .width(100.dp)
                        .height(80.dp)
                        .padding(4.dp),
                    shape = RectangleShape
                ) {
                    Text(
                        text = "Image",
                        fontSize = 10.sp
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .width(100.dp)
                        .height(80.dp)
                        .padding(4.dp),
                    shape = RectangleShape
                ) {
                    Text(
                        text = "Detect",
                        fontSize = 10.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            imageBitmap?.let { bitmap ->
                Image(
                    bitmap = bitmap,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            } ?: Text(
                text = "Image not loacded",
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(text = "1번째 $label1: $prediction1")

            Text(text = "2번째 $label2: $prediction2")

            Text(text = "3번째 $prediction3")


            Button(onClick = { navController.navigate("evaluation") }) {
                Text(text = "고객만족평가")
            }
        }
    }
}

fun preprocessImage(inputBitmap: Bitmap): ByteBuffer {
    val bitmap = Bitmap.createScaledBitmap(inputBitmap, 224, 224, true)
    val input = ByteBuffer.allocateDirect(224 * 224 * 3 * 4).order(ByteOrder.nativeOrder())

    for (y in 0 until 224) {
        for (x in 0 until 224) {
            val px = bitmap.getPixel(x, y)

            // Get channel values from the pixel value.
            val r = px.red
            val g = px.green
            val b = px.blue

            // Normalize channel values to [-1.0, 1.0]. This requirement depends on the model.
            // For example, some models might require values to be normalized to the range
            // [0.0, 1.0] instead.
            val rf = (r - 127) / 255f
            val gf = (g - 127) / 255f
            val bf = (b - 127) / 255f

            input.putFloat(rf)
            input.putFloat(gf)
            input.putFloat(bf)
        }
    }
    return input
}
fun bitmapToUri(context: Context, bitmap: Bitmap): Uri? {
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
    }

    val uri = context.contentResolver.insert(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        contentValues
    )

    uri?.let {
        context.contentResolver.openOutputStream(it).use { outputStream ->
            if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream!!)) {
                return null
            }
        }
    }

    return uri
}
fun uriToBitmap(uri: Uri, context: Context): Bitmap? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        BitmapFactory.decodeStream(inputStream)
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
        null
    }
}

//fun softmax(input: FloatArray, output: FloatArray) {
//    if (input.size != output.size) {
//        throw IllegalArgumentException("Input and output arrays must have the same size.")
//    }
//
//    // Find the maximum value in the input array for numerical stability
//    val maxInput = input.maxOrNull() ?: 0.0f
//
//    // Calculate the exponential of each element and the sum of exponentials
//    var sumExp = 0.0f
//    for (i in input.indices) {
//        val expValue = exp(input[i] - maxInput)
//        output[i] = expValue
//        sumExp += expValue
//    }
//
//    // Normalize by dividing each element by the sum of exponentials
//    for (i in output.indices) {
//        output[i] /= sumExp
//    }
//}

fun softmax(input: FloatBuffer, output: FloatArray) {
    if (input.capacity() != output.size) {
        throw IllegalArgumentException("Input and output arrays must have the same size.")
    }

    // Find the maximum value in the input buffer for numerical stability
    var maxInput = Float.NEGATIVE_INFINITY
    for (i in 0 until input.capacity()) {
        maxInput = maxInput.coerceAtLeast(input.get(i))
    }

    // Calculate the exponential of each element and the sum of exponentials
    var sumExp = 0.0f
    for (i in 0 until input.capacity()) {
        val expValue = exp(input.get(i) - maxInput)
        output[i] = expValue
        sumExp += expValue
    }

    // Normalize by dividing each element by the sum of exponentials
    for (i in 0 until input.capacity()) {
        output[i] /= sumExp
    }
}

fun findIndexOfMax(array: FloatArray): Int {
    var maxIndex = 0
    var maxValue = array[0]

    for (i in 1 until array.size) {
        if (array[i] > maxValue) {
            maxIndex = i
            maxValue = array[i]
        }
    }

    return maxIndex
}


//
//@Preview(showBackground = true)
//@Composable
//fun DetectScreenPreview() {
//    DetectScreen(navController = rememberNavController())
//}