package com.app.getiproject_naranggeti

import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
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



@Composable
fun DetectScreen(navController: NavController) {

    val bitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.apple)


    val conditions = CustomModelDownloadConditions.Builder()
        .requireWifi()  // Also possible: .requireCharging() and .requireDeviceIdle()
        .build()
    FirebaseModelDownloader.getInstance()
        .getModel("your_model", DownloadType.LOCAL_MODEL_UPDATE_IN_BACKGROUND,
            conditions)
        .addOnSuccessListener { model: CustomModel? ->
            val bitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true)
            val input = ByteBuffer.allocateDirect(224*224*3*4).order(ByteOrder.nativeOrder())
            for (y in 0 until 224) {
                for (x in 0 until 224) {
                    val px = bitmap.getPixel(x, y)

                    // Get channel values from the pixel value.
                    val r = Color.red(px)
                    val g = Color.green(px)
                    val b = Color.blue(px)

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

            val bufferSize = 2 * java.lang.Float.SIZE / java.lang.Byte.SIZE
            val modelOutput = ByteBuffer.allocateDirect(bufferSize).order(ByteOrder.nativeOrder())
            interpreter?.run(input, modelOutput)

            modelOutput.rewind()
            val probabilities = modelOutput.asFloatBuffer()
            try {
                val reader = BufferedReader(
                    InputStreamReader(assets.open("custom_labels.txt"))
                )
                for (i in probabilities.capacity()) {
                    val label: String = reader.readLine()
                    val probability = probabilities.get(i)
                    println("$label: $probability")
                }
            } catch (e: IOException) {
                // File not found?
            }


            val modelFile = model?.file
            if (modelFile != null) {
                var interpreter = Interpreter(modelFile)
            }
        }

    //    Column(
//        modifier = Modifier.fillMaxSize(),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Row {
//            Button(
//                onClick = { /*TODO*/ },
//                modifier = Modifier
//                    .width(150.dp)
//                    .height(80.dp)
//                    .padding(4.dp),
//                shape = RectangleShape
//            ) {
//                Text(
//                    text = "Image",
//                    fontSize = 30.sp
//                )
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            Button(
//                onClick = { /*TODO*/ },
//                modifier = Modifier
//                    .width(150.dp)
//                    .height(80.dp)
//                    .padding(4.dp),
//                shape = RectangleShape
//            ) {
//                Text(
//                    text = "Detect",
//                    fontSize = 30.sp
//                )
//            }
//        }
//
//    }


}