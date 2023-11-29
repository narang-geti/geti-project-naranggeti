package com.app.getiproject_naranggeti

import android.app.DatePickerDialog
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key.Companion.Calendar
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDate


data class UserData(
    val title: String? = null,
    val buydate: String? = null,
    val memorycapacity: String? = null,
    val batteryefficiency: String? = null,
    val price: String? = null,
    val applecare: String? = null
)

enum class AppleCareOption(val value: String) {
    YES("Yes"),
    NO("No")
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AddPage(navController: NavHostController) {
    var title by remember { mutableStateOf(TextFieldValue()) }
    var memorycapacity by remember { mutableStateOf(TextFieldValue()) }
    var batteryefficiency by remember { mutableStateOf(TextFieldValue()) }
    var price by remember { mutableStateOf(TextFieldValue()) }
    var applecareOption by remember { mutableStateOf(AppleCareOption.YES) }
    val selectedDate = remember { mutableStateOf(LocalDate.now()) }
    val showDialog = remember { mutableStateOf(false) }
//    var buydate by remember { mutableStateOf(selectedDate.value.toString()) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent)
        )
        Text(
            text = "Buy Date: ${selectedDate.value}",
            modifier = Modifier
                .clickable { showDialog.value = true }
                .fillMaxWidth()
                .padding(16.dp)
        )
        LaunchedEffect(showDialog.value) {
            if (showDialog.value) {
                showDialog.value = false
            }
        }

        if (showDialog.value) {
            Dialog(onDismissRequest = { showDialog.value = false }) {
                val current = LocalDate.now()
                val year = current.year
                val month = current.monthValue - 1
                val day = current.dayOfMonth

                DatePickerDialog(
                    LocalContext.current,
                    { _, year, month, dayOfMonth ->
                        selectedDate.value = LocalDate.of(year, month + 1, dayOfMonth)
                        showDialog.value = false
                    },
                    year,
                    month,
                    day
                ).show()
            }
        }
        TextField(
            value = memorycapacity,
            onValueChange = { memorycapacity = it },
            label = { Text("Memory Capacity") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent)
        )

        TextField(
            value = batteryefficiency,
            onValueChange = { batteryefficiency = it },
            label = { Text("Battery Efficiency") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent)
        )

        TextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Price") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                "Apple\nCare",
                modifier = Modifier
                    .weight(2f)
                    .padding(start = 16.dp)
                    .align(Alignment.CenterVertically)
            )
            RadioButton(
                selected = applecareOption == AppleCareOption.YES,
                onClick = { applecareOption = AppleCareOption.YES },
                modifier = Modifier.weight(1f)
            )
            Text(
                "Yes",
                modifier = Modifier
                    .weight(2f)
                    .align(Alignment.CenterVertically)
            )
            RadioButton(
                selected = applecareOption == AppleCareOption.NO,
                onClick = { applecareOption = AppleCareOption.NO },
                modifier = Modifier.weight(1f)
            )
            Text(
                "No",
                modifier = Modifier
                    .weight(2f)
                    .align(Alignment.CenterVertically)
            )
        }


        Button(
            onClick = {
                val userData = UserData(
                    title = title.text,
                    buydate = selectedDate.value.toString(),                    memorycapacity = memorycapacity.text,
                    batteryefficiency = batteryefficiency.text,
                    price = price.text,
                    applecare = applecareOption.value
                )

                val db = Firebase.firestore

                db.collection("userdata")
                    .add(userData)
                    .addOnSuccessListener { documentReference ->
                        Log.d(
                            "Firestore",
                            "DocumentSnapshot added with ID: ${documentReference.id}"
                        )
                        navController.navigate("menu")
                    }
                    .addOnFailureListener { e ->
                        // 저장 중 오류가 발생한 경우
                        Log.w("Firestore", "Error adding document", e)
                    }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(text = "Save")
        }
    }
}
@Composable
fun showDatePickerDialog(selectedDate: MutableState<LocalDate>) {
    val current = LocalDate.now()
    val year = current.year
    val month = current.monthValue - 1
    val day = current.dayOfMonth

    val datePickerDialog = DatePickerDialog(
        LocalContext.current,
        { _, year, month, dayOfMonth ->
            selectedDate.value = LocalDate.of(year, month + 1, dayOfMonth)
        },
        year,
        month,
        day
    )
    datePickerDialog.show()
}