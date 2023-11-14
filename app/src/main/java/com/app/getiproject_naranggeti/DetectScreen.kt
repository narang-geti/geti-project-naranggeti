package com.app.getiproject_naranggeti

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red

@Composable
fun DetectScreen(navController: NavController) {
    val context = LocalContext.current
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var interpreter by remember { mutableStateOf<Interpreter?>(null) }
    var prediction1 by remember { mutableStateOf("") }
    var label1 by remember { mutableStateOf("") }
    var prediction2 by remember { mutableStateOf("") }
    var label2 by remember { mutableStateOf("") }
    var prediction3 by remember { mutableStateOf("") }
    val bitmapFromResource1: Bitmap =
        BitmapFactory.decodeResource(context.resources, R.drawable.paper)
    val bitmapFromResource2: Bitmap =
        BitmapFactory.decodeResource(context.resources, R.drawable.apple)
    val bitmapFromResource3: Bitmap =
        BitmapFactory.decodeResource(context.resources, R.drawable.plastic)

    DisposableEffect(Unit) {
        val modelConditions = CustomModelDownloadConditions.Builder()
            .requireWifi()  // Also possible: .requireCharging() and .requireDeviceIdle()
            .build()

        FirebaseModelDownloader.getInstance()
            .getModel("test1113", DownloadType.LOCAL_MODEL_UPDATE_IN_BACKGROUND, modelConditions)
            .addOnSuccessListener { model: CustomModel? ->
                val modelFile = model?.file
                if (modelFile != null) {
                    interpreter = Interpreter(modelFile)
                }

                val bitmap1 = bitmapFromResource1
                val bitmap2 = bitmapFromResource2
                val bitmap3 = bitmapFromResource3

                if (bitmap1 != null && bitmap2 != null && bitmap3 != null) {
                    val input1 = preprocessImage(bitmap1)
                    val input2 = preprocessImage(bitmap2)
                    val input3 = preprocessImage(bitmap3)

                    val bufferSize = 2 * java.lang.Float.SIZE / java.lang.Byte.SIZE
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
                    val probabilities1 = modelOutput1.asFloatBuffer()
                    try {
                        val reader = BufferedReader(
                            InputStreamReader(context.resources.openRawResource(R.raw.organic))
                        )
//                        val inputStream = context.resources.openRawResource("organic.txt")
//                        val reader = BufferedReader(InputStreamReader(inputStream))
                        for (i in 0 until probabilities1.capacity()) {
                            val label: String = reader.readLine()
                            val probability = probabilities1.get(i)
                            println("$label: $probability")
                            label1 = label
                            prediction1 = probability.toString()
                        }
                    } catch (e: IOException) {
                        // Handle the exception
                        e.printStackTrace()
                    }

                    modelOutput2.rewind()
                    val probabilities2 = modelOutput2.asFloatBuffer()
                    try {
                        val reader = BufferedReader(
                            InputStreamReader(context.resources.openRawResource(R.raw.organic))
                        )
//                        val inputStream = context.resources.openRawResource("organic.txt")
//                        val reader = BufferedReader(InputStreamReader(inputStream))
                        for (i in 0 until probabilities2.capacity()) {
                            val label: String = reader.readLine()
                            val probability = probabilities2.get(i)
                            println("$label: $probability")
                            label2 = label
                            prediction2 = probability.toString()
                        }
                    } catch (e: IOException) {
                        // Handle the exception
                        e.printStackTrace()
                    }


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
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row {
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .width(150.dp)
                        .height(80.dp)
                        .padding(4.dp),
                    shape = RectangleShape
                ) {
                    Text(
                        text = "Image",
                        fontSize = 30.sp
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .width(150.dp)
                        .height(80.dp)
                        .padding(4.dp),
                    shape = RectangleShape
                ) {
                    Text(
                        text = "Detect",
                        fontSize = 30.sp
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
        }
    }
}

private fun preprocessImage(inputBitmap: Bitmap): ByteBuffer {
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





//
//@Preview(showBackground = true)
//@Composable
//fun DetectScreenPreview() {
//    DetectScreen(navController = rememberNavController())
//}