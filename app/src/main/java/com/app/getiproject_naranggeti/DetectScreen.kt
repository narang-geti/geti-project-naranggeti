package com.app.getiproject_naranggeti

import android.content.ContentValues
import android.content.Context
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
import java.nio.ByteBuffer
import java.nio.ByteOrder
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.io.FileNotFoundException
import java.nio.FloatBuffer
import kotlin.math.exp


@Composable
fun DetectScreen(navController: NavController) {
    val context = LocalContext.current
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var interpreterf by remember { mutableStateOf<Interpreter?>(null) }
    var interpreterb by remember { mutableStateOf<Interpreter?>(null) }
    var predictionf by remember { mutableStateOf("") }
    var predictionb by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()

    var imagef by remember { mutableStateOf<Bitmap?>(null) }
    var imageb by remember { mutableStateOf<Bitmap?>(null) }
    var imagefp by remember { mutableStateOf<Bitmap?>(null) }
    var imagebp by remember { mutableStateOf<Bitmap?>(null) }

    val db = Firebase.firestore
    val userUID = Firebase.auth.currentUser?.uid

    userUID?.let { uid ->
        val userData = hashMapOf(
            "front" to predictionf,
            "back"  to predictionb
        )

        db.collection("user").document(uid)
            .set(userData, SetOptions.merge())
            .addOnSuccessListener { Log.d("Firestore", "성공") }
            .addOnFailureListener { e -> Log.w("Firestore", "에러", e) }
    }


    val launcher1 = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            imagef = uriToBitmap(uri!!, context)
        }
    )

    val cameraLauncher1 = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview(),
        onResult = { photo ->
            imagef = photo
        }
    )

    val launcher2 = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            imageb = uriToBitmap(uri!!, context)
        }
    )

    val cameraLauncher2 = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview(),
        onResult = { photo ->
            imageb = photo
        }
    )
    val resources = context.resources
    val defaultImageBitmap =
        BitmapFactory.decodeResource(resources, R.drawable.camera).asImageBitmap()


    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Image(
                    modifier = Modifier
                        .size(300.dp),
                    bitmap = imagef?.asImageBitmap() ?: defaultImageBitmap,
                    contentDescription = "image"
                )

                Row {
                    Button(
                        onClick = { cameraLauncher1.launch(null) },
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
                        onClick = { launcher1.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
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

                }
            }


            Image(
                modifier = Modifier
                    .size(300.dp),
                bitmap = imageb?.asImageBitmap() ?: defaultImageBitmap,
                contentDescription = "image"
            )

            Row {
                Button(
                    onClick = { cameraLauncher2.launch(null) },
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
                    onClick = { launcher2.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
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
                    onClick = {
                        if (imagef != null && imageb != null) {
                            imagefp = imagef as Bitmap
                            imagebp = imageb as Bitmap

                            val input1 = preprocessImage(imagefp!!)
                            val input2 = preprocessImage(imagebp!!)

                            val bufferSize = 4 * java.lang.Float.SIZE / java.lang.Byte.SIZE
                            val modelOutput1 =
                                ByteBuffer.allocateDirect(bufferSize)
                                    .order(ByteOrder.nativeOrder())
                            val modelOutput2 =
                                ByteBuffer.allocateDirect(bufferSize)
                                    .order(ByteOrder.nativeOrder())

                            interpreterf?.run(input1, modelOutput1)
                            interpreterb?.run(input2, modelOutput2)

                            modelOutput1.rewind()
                            val probabilities1 = FloatArray(4)
                            softmax(modelOutput1.asFloatBuffer(), probabilities1)
                            val predictedClassIndex1 = findIndexOfMax(probabilities1)
                            val classLabels = arrayOf("S", "A", "B", "F")
                            val predictedClassLabel1 = classLabels[predictedClassIndex1]
                            predictionf = predictedClassLabel1

                            modelOutput2.rewind()
                            val probabilities2 = FloatArray(4)
                            softmax(modelOutput2.asFloatBuffer(), probabilities2)
                            val predictedClassIndex2 = findIndexOfMax(probabilities2)
                            val predictedClassLabel2 = classLabels[predictedClassIndex2]
                            predictionb = predictedClassLabel2

                        }

//                        navController.navigate("review/$prediction1/$prediction2")
                    },
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

            Text(text = "앞면 : $predictionf")

            Text(text = "후면 : $predictionb")

            Button(onClick={navController.navigate("customer")}){
                Text(text="고객만족평가")
            }
            Button(onClick={navController.navigate("grade")}){
                Text(text="당신의등급은?")
            }
            Button(onClick={navController.navigate("review")}){
                Text(text="당신의등급은?")
            }


        }
    }

    DisposableEffect(Unit) {
        val modelConditions = CustomModelDownloadConditions.Builder()
            .requireWifi()  // Also possible: .requireCharging() and .requireDeviceIdle()
            .build()

        FirebaseModelDownloader.getInstance()
            .getModel("ipf1", DownloadType.LOCAL_MODEL_UPDATE_IN_BACKGROUND, modelConditions)
            .addOnSuccessListener { model: CustomModel? ->
                val modelFile = model?.file
                if (modelFile != null) {
                    interpreterf = Interpreter(modelFile)
                }

                if (imagefp != null) {
                    val input1 = preprocessImage(imagefp!!)

                    val bufferSize = 4 * java.lang.Float.SIZE / java.lang.Byte.SIZE
                    val modelOutput1 =
                        ByteBuffer.allocateDirect(bufferSize).order(ByteOrder.nativeOrder())


                    interpreterf?.run(input1, modelOutput1)

                    modelOutput1.rewind()
                    // 소프트맥스 함수를 사용하여 확률 벡터 얻기
                    val probabilities1 = FloatArray(4)
                    softmax(modelOutput1.asFloatBuffer(), probabilities1)

                    // 최대 확률을 가진 클래스 선택
                    val predictedClassIndex1 = findIndexOfMax(probabilities1)

                    // 클래스 레이블 매핑
                    val classLabels = arrayOf("S", "A", "B", "F")
                    val predictedClassLabel1 = classLabels[predictedClassIndex1]
                    predictionf = predictedClassLabel1
                }
            }


        // Clean up resources when the composable is disposed
        onDispose {
            interpreterf?.close()
        }
    }

    DisposableEffect(Unit) {
        val modelConditions = CustomModelDownloadConditions.Builder()
            .requireWifi()  // Also possible: .requireCharging() and .requireDeviceIdle()
            .build()

        FirebaseModelDownloader.getInstance()
            .getModel("ipb1", DownloadType.LOCAL_MODEL_UPDATE_IN_BACKGROUND, modelConditions)
            .addOnSuccessListener { model: CustomModel? ->
                val modelFile = model?.file
                if (modelFile != null) {
                    interpreterb = Interpreter(modelFile)
                }

                if (imagebp != null) {
                    val input2 = preprocessImage(imagebp!!)

                    val bufferSize = 4 * java.lang.Float.SIZE / java.lang.Byte.SIZE
                    val modelOutput2 =
                        ByteBuffer.allocateDirect(bufferSize).order(ByteOrder.nativeOrder())


                    interpreterb?.run(input2, modelOutput2)

                    modelOutput2.rewind()

                    // 소프트맥스 함수를 사용하여 확률 벡터 얻기
                    val probabilities2 = FloatArray(4)
                    softmax(modelOutput2.asFloatBuffer(), probabilities2)

                    // 최대 확률을 가진 클래스 선택
                    val predictedClassIndex2 = findIndexOfMax(probabilities2)

                    // 클래스 레이블 매핑
                    val classLabels = arrayOf("S", "A", "B", "F")
                    val predictedClassLabel2 = classLabels[predictedClassIndex2]
                    predictionb = predictedClassLabel2
                }
            }

        // Clean up resources when the composable is disposed
        onDispose {
            interpreterb?.close()
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


