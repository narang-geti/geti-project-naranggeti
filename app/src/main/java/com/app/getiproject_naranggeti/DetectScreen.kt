package com.app.getiproject_naranggeti

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
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
    val bitmapFromResource: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.apple)

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

                val bitmap = bitmapFromResource

                if (bitmap != null) {
                    val input = preprocessImage(bitmap)

                    val bufferSize = 2 * java.lang.Float.SIZE / java.lang.Byte.SIZE
                    val modelOutput = ByteBuffer.allocateDirect(bufferSize).order(ByteOrder.nativeOrder())
                    interpreter?.run(input, modelOutput)

                    modelOutput.rewind()
                    val probabilities = modelOutput.asFloatBuffer()
                    try {
                        val reader = BufferedReader(
                            InputStreamReader(context.resources.openRawResource(R.raw.organic))
                        )
//                        val inputStream = context.resources.openRawResource("organic.txt")
//                        val reader = BufferedReader(InputStreamReader(inputStream))
                        for (i in 0 until probabilities.capacity()) {
                            val label: String = reader.readLine()
                            val probability = probabilities.get(i)
                            println("$label: $probability")
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
                text = "Image not loaded",
                color = MaterialTheme.colorScheme.onSurface
            )
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