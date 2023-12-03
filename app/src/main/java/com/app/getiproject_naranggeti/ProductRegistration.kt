package com.app.getiproject_naranggeti

import android.app.DatePickerDialog
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDate
import kotlin.text.*


data class UserData(
    val title: String? = null,
    val buydate: String? = null,
    val storage: String? = null,
    val batteryefficiency: String? = null,
    val price: String? = null,
    val applecare: String? = null,
    val customertext: String? = null,


)

enum class AppleCareOption(val value: String) {
    YES("Yes"),
    NO("No")
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun ProductRegistration(navController: NavHostController) {
    var title by remember { mutableStateOf(TextFieldValue()) }
    var batteryefficiency by remember { mutableStateOf(TextFieldValue()) }
    var price by remember { mutableStateOf(TextFieldValue()) }
    var applecareOption by remember { mutableStateOf(AppleCareOption.YES) }
    val selectedDate = remember { mutableStateOf(LocalDate.now()) }
    val showDialog = remember { mutableStateOf(false) }
    var selectedStorage by remember { mutableStateOf("") }
    var customerText by remember { mutableStateOf(TextFieldValue()) }
    val scrollState = rememberScrollState()
    val auth = Firebase.auth
    val userUid = auth.currentUser?.uid ?: ""

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState),
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
            //달력 재선택 가능하게 해줌
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
                //달력 코드
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

        DropDownMenu(selectedItem = selectedStorage, onChangeItem = { selectedStorage = it })

        TextField(
            value = "${batteryefficiency.text}%",
            onValueChange = {
                batteryefficiency =
                    TextFieldValue(it.filterIndexed { index, char -> index < 3 && char.isDigit() })
            },
            label = { Text("Battery Efficiency") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        TextField(
            value = "${price.text}₩",
            onValueChange = {
                price =
                    TextFieldValue(it.filterIndexed { index, char -> index < 9 && char.isDigit() })
            },
            label = { Text("Price") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
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
        OutlinedTextField(
            value = customerText,
            onValueChange = { customerText = it },
            label = { Text("Custom Text") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
                .height(200.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color.Gray,
                textColor = Color.Black
            )
        )


        Button(
            onClick = {
                val userData = UserData(
                    title = title.text,
                    buydate = selectedDate.value.toString(),
                    storage = selectedStorage,
                    batteryefficiency = "${batteryefficiency.text}%",
                    price = "${price.text}원",
                    applecare = applecareOption.value,
                    customertext = customerText.text
                )

                val db = Firebase.firestore

                db.collection("userdata").document(userUid)
                    .set(userData, SetOptions.merge())
                    .addOnSuccessListener {
                        Log.d("Firestore", "DocumentSnapshot added with ID: $userUid")
                        navController.navigate("menu")
                    }
                    .addOnFailureListener { e ->
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownMenu(selectedItem: String, onChangeItem: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val list = listOf("128GB", "256GB", "512GB", "1TB")

    Column(modifier = Modifier.padding(20.dp)) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                readOnly = true,
                value = selectedItem,
                onValueChange = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                label = { Text(text = "Select Storage") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                }
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                list.forEach { label ->
                    DropdownMenuItem(
                        text = {
                            Text(text = label)
                        },
                        onClick = {
                            onChangeItem(label)
                            expanded = false
                        },
                    )
                }
            }

        }
    }
}
