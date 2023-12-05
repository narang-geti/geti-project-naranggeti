package com.app.getiproject_naranggeti

import android.content.ContentValues
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavController
import com.app.getiproject_naranggeti.ui.theme.Purple40
import com.app.getiproject_naranggeti.ui.theme.Purple80
import com.app.getiproject_naranggeti.ui.theme.elice
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.Date
import java.util.Locale


data class Message(
    val text: String? = "메세지 오류",
    val userId: String? = "UID 오류",
    val userName: String? = "이름 오류",
    val timestamp: Any? = null,
    val userImage: String? = "이미지 오류"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Chatting1(onNavigateToChatview: () -> Unit) {
    val user = Firebase.auth.currentUser
    val backgroundColor = Purple40
    val contentColor = Color.Black

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = backgroundColor
    ) {
        var newMessage by remember { mutableStateOf("") }
        var displayedMessages by remember { mutableStateOf(emptyList<Message>()) }
        val messageRef1 = remember { Firebase.database.getReference("chat").child("민형준") }

        user?.uid
        var userName: String? by remember { mutableStateOf("") }
        var userEmail: String? by remember { mutableStateOf("") }
        var userId: String? by remember { mutableStateOf("") }
        var userImage: Uri? by remember { mutableStateOf(null) }
        user?.let {
            userName = it.displayName
            userEmail = it.email
            userId = it.uid
            userImage = it.photoUrl
        }

        // Write a message to the database
        LaunchedEffect(Unit) {
            messageRef1.addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

                    val text = snapshot.child("text").getValue(String::class.java)
                    val userId = snapshot.child("userId").getValue(String::class.java)
                    val userName = snapshot.child("userName").getValue(String::class.java)
                    val timestamp = snapshot.child("timestamp").getValue(Long::class.java)
                    val userImage = snapshot.child("userImage").getValue(String::class.java)

                    if (text != null && userId != null && userName != null && timestamp != null && userImage != null) {
                        val message = Message(text, userId, userName, timestamp, userImage)

                        if (!displayedMessages.contains(message)) {
                            displayedMessages += message
                        }
                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
//            TODO("Not yet implemented")
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
//            TODO("Not yet implemented")
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
//            TODO("Not yet implemented")
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
                }
            })

        }

        // Create LazyListState to manage scrolling
        val scrollState = rememberLazyListState()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
        ) {
            Column(modifier = Modifier.align(Alignment.TopStart)) {
                Row(modifier = Modifier.padding(16.dp)) {
                    Icon(
                        imageVector = Icons.Outlined.KeyboardArrowLeft,
                        contentDescription = "back",
                        tint = contentColor,
                        modifier = Modifier
                            .align(
                                Alignment.CenterVertically
                            )
                            .size(32.dp)
                            .clickable(onClick = onNavigateToChatview)
                    )
                    Spacer(modifier = Modifier.size(24.dp, 0.dp))
                    Text(
                        text = "민형준",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = contentColor,
                        modifier = Modifier.align(
                            Alignment.CenterVertically
                        )
                    )
                }
                Divider(color = contentColor.copy(0.2f))
                Spacer(modifier = Modifier.size(0.dp, 16.dp))
                Column(Modifier.fillMaxSize()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.9f), // Expand to take remaining space
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                        verticalArrangement = Arrangement.Bottom,
                        state = scrollState // Assign LazyListState to LazyColumn
                    ) {
                        // "yyyy-MM-dd HH:mm:ss"
                        //Text(text = "Sender: ${message.user ?: ""}") // Display user ID
                        items(displayedMessages) { message ->
                            val isCurrentUserMessage = (userId == message.userId)
                            val arrangement =
                                if (isCurrentUserMessage) Arrangement.End else Arrangement.Start
                            val textBackgroundColor = if (isCurrentUserMessage) Color(0xFFFFEB3B) else Color.DarkGray
                            val textColor = if (isCurrentUserMessage) Color.Black else Color.White

                            val timestampShow =
                                SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(
                                    Date(message.timestamp as Long)
                                )
                            var image = painterResource(R.drawable.user)

                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = arrangement,
                                    verticalAlignment = Alignment.Bottom
                                ) {
                                    if (isCurrentUserMessage) {
                                        Text(text = "$userName", fontSize = 12.sp, color = contentColor)
                                    } else {
                                        Text(text = "${message.userName}", fontSize = 12.sp, color = contentColor)
                                    }
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = arrangement,
                                    verticalAlignment = Alignment.Bottom
                                ) {
                                    if (!isCurrentUserMessage) {
                                        // 여기에 uri값 받아서 이미지 출력하고 싶음
                                        Image(
                                            painter = image,
                                            contentDescription = "",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .size(45.dp)
                                                .clip(RoundedCornerShape(30))
                                                .align(Alignment.CenterVertically)
                                        )
                                        Spacer(modifier = Modifier.size(8.dp))
                                    }

                                    if (isCurrentUserMessage) {
                                        Text(text = timestampShow, color = Color.White, fontSize = 9.sp)
                                        Spacer(modifier = Modifier.size(4.dp, 0.dp))
                                    }

                                    Button(
                                        onClick = { /*TODO*/ },
                                        colors = ButtonDefaults.buttonColors(textBackgroundColor),
                                        shape = RoundedCornerShape(25),
                                        modifier = Modifier.align(Alignment.CenterVertically),
                                        contentPadding = PaddingValues(start = 12.dp, end = 12.dp, top = 4.dp , bottom = 4.dp)
                                    ) {
                                        message.text?.let {
                                            Text(
                                                text = it,
                                                textAlign = TextAlign.Start,
                                                color = textColor
                                            )
                                        }
                                    }

                                    if (!isCurrentUserMessage) {
                                        Spacer(modifier = Modifier.size(4.dp, 0.dp))
                                        Text(text = timestampShow, color = Color.White, fontSize = 9.sp, modifier = Modifier.padding(bottom = 4.dp))
                                    }
                                }
                                Spacer(modifier = Modifier.size(0.dp, 8.dp))
                            }
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(13.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { /*TODO*/ },
                            shape = RectangleShape,
                            colors = ButtonDefaults.buttonColors(contentColor),
                            modifier = Modifier.padding(0.dp)
                        ) {
                            Text(text = "+", color = backgroundColor)
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        TextField(
                            value = newMessage,
                            onValueChange = { newMessage = it },
                            placeholder = { Text(text = "채팅 입력", fontSize = 12.sp) },
                            modifier = Modifier
                                .weight(1f)
                                .border(
                                    BorderStroke(
                                        width = 1.dp,
                                        color = contentColor
                                    ), shape = RoundedCornerShape(
                                        10
                                    )
                                ),
                            maxLines = 1,
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
//                                focusedTextColor = contentColor,
                                disabledTextColor = Color.Gray,
//                                unfocusedTextColor = contentColor,
//                                focusedPlaceholderColor = Color.Gray,
                                disabledPlaceholderColor = Color.Gray,
//                                unfocusedPlaceholderColor = Color.Gray,
                                cursorColor = contentColor
                            )
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = {
                                if (newMessage.isNotBlank()) {
                                    val messageData = mapOf(
                                        "text" to newMessage,
                                        "userId" to userId,
                                        "userName" to userName,
                                        "timestamp" to ServerValue.TIMESTAMP,
                                        "userImage" to userImage.toString()
                                    )
                                    messageRef1.push().setValue(messageData)
//                            chatRoomRef.push()

//                            messages.add(newMessage)
                                    newMessage = "" // Clear the input
//                            time.add(ServerValue.TIMESTAMP.toString())
                                }
                            },
                            colors = ButtonDefaults.buttonColors(contentColor),
                            modifier = Modifier.wrapContentWidth()
                        ) {
                            Text(text = "보내기", color = backgroundColor)
                        }
                    }
                }
            }
        }

        LaunchedEffect(displayedMessages.size) {
            scrollState.animateScrollToItem(displayedMessages.size)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Chatting2(onNavigateToChatview: () -> Unit) {
    val user = Firebase.auth.currentUser
    val backgroundColor = Purple40
    val contentColor = Color.Black

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = backgroundColor
    ) {
        var newMessage by remember { mutableStateOf("") }
        var displayedMessages by remember { mutableStateOf(emptyList<Message>()) }
        val messageRef1 = remember { Firebase.database.getReference("chat").child("문세희") }

        user?.uid
        var userName: String? by remember { mutableStateOf("") }
        var userEmail: String? by remember { mutableStateOf("") }
        var userId: String? by remember { mutableStateOf("") }
        var userImage: Uri? by remember { mutableStateOf(null) }
        user?.let {
            userName = it.displayName
            userEmail = it.email
            userId = it.uid
            userImage = it.photoUrl
        }

        // Write a message to the database
        LaunchedEffect(Unit) {
            messageRef1.addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

                    val text = snapshot.child("text").getValue(String::class.java)
                    val userId = snapshot.child("userId").getValue(String::class.java)
                    val userName = snapshot.child("userName").getValue(String::class.java)
                    val timestamp = snapshot.child("timestamp").getValue(Long::class.java)
                    val userImage = snapshot.child("userImage").getValue(String::class.java)

                    if (text != null && userId != null && userName != null && timestamp != null && userImage != null) {
                        val message = Message(text, userId, userName, timestamp, userImage)

                        if (!displayedMessages.contains(message)) {
                            displayedMessages += message
                        }
                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
//            TODO("Not yet implemented")
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
//            TODO("Not yet implemented")
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
//            TODO("Not yet implemented")
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
                }
            })

        }

        // Create LazyListState to manage scrolling
        val scrollState = rememberLazyListState()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
        ) {
            Column(modifier = Modifier.align(Alignment.TopStart)) {
                Row(modifier = Modifier.padding(16.dp)) {
                    Icon(
                        imageVector = Icons.Outlined.KeyboardArrowLeft,
                        contentDescription = "back",
                        tint = contentColor,
                        modifier = Modifier
                            .align(
                                Alignment.CenterVertically
                            )
                            .size(32.dp)
                            .clickable(onClick = onNavigateToChatview)
                    )
                    Spacer(modifier = Modifier.size(24.dp, 0.dp))
                    Text(
                        text = "문세희",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = contentColor,
                        modifier = Modifier.align(
                            Alignment.CenterVertically
                        )
                    )
                }
                Divider(color = contentColor.copy(0.2f))
                Spacer(modifier = Modifier.size(0.dp, 16.dp))
                Column(Modifier.fillMaxSize()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.9f), // Expand to take remaining space
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                        verticalArrangement = Arrangement.Bottom,
                        state = scrollState // Assign LazyListState to LazyColumn
                    ) {
                        // "yyyy-MM-dd HH:mm:ss"
                        //Text(text = "Sender: ${message.user ?: ""}") // Display user ID
                        items(displayedMessages) { message ->
                            val isCurrentUserMessage = (userId == message.userId)
                            val arrangement =
                                if (isCurrentUserMessage) Arrangement.End else Arrangement.Start
                            val textBackgroundColor = if (isCurrentUserMessage) Color(0xFFFFEB3B) else Color.DarkGray
                            val textColor = if (isCurrentUserMessage) Color.Black else Color.White

                            val timestampShow =
                                SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(
                                    Date(message.timestamp as Long)
                                )
                            var image = painterResource(R.drawable.user)

                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = arrangement,
                                    verticalAlignment = Alignment.Bottom
                                ) {
                                    if (isCurrentUserMessage) {
                                        Text(text = "$userName", fontSize = 12.sp, color = contentColor)
                                    } else {
                                        Text(text = "${message.userName}", fontSize = 12.sp, color = contentColor)
                                    }
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = arrangement,
                                    verticalAlignment = Alignment.Bottom
                                ) {
                                    if (!isCurrentUserMessage) {
                                        // 여기에 uri값 받아서 이미지 출력하고 싶음
                                        Image(
                                            painter = image,
                                            contentDescription = "",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .size(45.dp)
                                                .clip(RoundedCornerShape(30))
                                                .align(Alignment.CenterVertically)
                                        )
                                        Spacer(modifier = Modifier.size(8.dp))
                                    }

                                    if (isCurrentUserMessage) {
                                        Text(text = timestampShow, color = Color.White, fontSize = 9.sp)
                                        Spacer(modifier = Modifier.size(4.dp, 0.dp))
                                    }

                                    Button(
                                        onClick = { /*TODO*/ },
                                        colors = ButtonDefaults.buttonColors(textBackgroundColor),
                                        shape = RoundedCornerShape(25),
                                        modifier = Modifier.align(Alignment.CenterVertically),
                                        contentPadding = PaddingValues(start = 12.dp, end = 12.dp, top = 4.dp , bottom = 4.dp)
                                    ) {
                                        message.text?.let {
                                            Text(
                                                text = it,
                                                textAlign = TextAlign.Start,
                                                color = textColor
                                            )
                                        }
                                    }

                                    if (!isCurrentUserMessage) {
                                        Spacer(modifier = Modifier.size(4.dp, 0.dp))
                                        Text(text = timestampShow, color = Color.White, fontSize = 9.sp, modifier = Modifier.padding(bottom = 4.dp))
                                    }
                                }
                                Spacer(modifier = Modifier.size(0.dp, 8.dp))
                            }
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(13.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { /*TODO*/ },
                            shape = RectangleShape,
                            colors = ButtonDefaults.buttonColors(contentColor),
                            modifier = Modifier.padding(0.dp)
                        ) {
                            Text(text = "+", color = backgroundColor)
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        TextField(
                            value = newMessage,
                            onValueChange = { newMessage = it },
                            placeholder = { Text(text = "채팅 입력", fontSize = 12.sp) },
                            modifier = Modifier
                                .weight(1f)
                                .border(
                                    BorderStroke(
                                        width = 1.dp,
                                        color = contentColor
                                    ), shape = RoundedCornerShape(
                                        10
                                    )
                                ),
                            maxLines = 1,
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
//                                focusedTextColor = contentColor,
                                disabledTextColor = Color.Gray,
//                                unfocusedTextColor = contentColor,
//                                focusedPlaceholderColor = Color.Gray,
                                disabledPlaceholderColor = Color.Gray,
//                                unfocusedPlaceholderColor = Color.Gray,
                                cursorColor = contentColor
                            )
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = {
                                if (newMessage.isNotBlank()) {
                                    val messageData = mapOf(
                                        "text" to newMessage,
                                        "userId" to userId,
                                        "userName" to userName,
                                        "timestamp" to ServerValue.TIMESTAMP,
                                        "userImage" to userImage.toString()
                                    )
                                    messageRef1.push().setValue(messageData)
//                            chatRoomRef.push()

//                            messages.add(newMessage)
                                    newMessage = "" // Clear the input
//                            time.add(ServerValue.TIMESTAMP.toString())
                                }
                            },
                            colors = ButtonDefaults.buttonColors(contentColor),
                            modifier = Modifier.wrapContentWidth()
                        ) {
                            Text(text = "보내기", color = backgroundColor)
                        }
                    }
                }
            }
        }

        LaunchedEffect(displayedMessages.size) {
            scrollState.animateScrollToItem(displayedMessages.size)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Chatting3(onNavigateToChatview: () -> Unit) {
    val user = Firebase.auth.currentUser
    val backgroundColor = Purple40
    val contentColor = Color.Black

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = backgroundColor
    ) {
        var newMessage by remember { mutableStateOf("") }
        var displayedMessages by remember { mutableStateOf(emptyList<Message>()) }
        val messageRef1 = remember { Firebase.database.getReference("chat").child("여효진") }

        user?.uid
        var userName: String? by remember { mutableStateOf("") }
        var userEmail: String? by remember { mutableStateOf("") }
        var userId: String? by remember { mutableStateOf("") }
        var userImage: Uri? by remember { mutableStateOf(null) }
        user?.let {
            userName = it.displayName
            userEmail = it.email
            userId = it.uid
            userImage = it.photoUrl
        }

        // Write a message to the database
        LaunchedEffect(Unit) {
            messageRef1.addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

                    val text = snapshot.child("text").getValue(String::class.java)
                    val userId = snapshot.child("userId").getValue(String::class.java)
                    val userName = snapshot.child("userName").getValue(String::class.java)
                    val timestamp = snapshot.child("timestamp").getValue(Long::class.java)
                    val userImage = snapshot.child("userImage").getValue(String::class.java)

                    if (text != null && userId != null && userName != null && timestamp != null && userImage != null) {
                        val message = Message(text, userId, userName, timestamp, userImage)

                        if (!displayedMessages.contains(message)) {
                            displayedMessages += message
                        }
                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
//            TODO("Not yet implemented")
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
//            TODO("Not yet implemented")
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
//            TODO("Not yet implemented")
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
                }
            })

        }

        // Create LazyListState to manage scrolling
        val scrollState = rememberLazyListState()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
        ) {
            Column(modifier = Modifier.align(Alignment.TopStart)) {
                Row(modifier = Modifier.padding(16.dp)) {
                    Icon(
                        imageVector = Icons.Outlined.KeyboardArrowLeft,
                        contentDescription = "back",
                        tint = contentColor,
                        modifier = Modifier
                            .align(
                                Alignment.CenterVertically
                            )
                            .size(32.dp)
                            .clickable(onClick = onNavigateToChatview)
                    )
                    Spacer(modifier = Modifier.size(24.dp, 0.dp))
                    Text(
                        text = "여효진",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = contentColor,
                        modifier = Modifier.align(
                            Alignment.CenterVertically
                        )
                    )
                }
                Divider(color = contentColor.copy(0.2f))
                Spacer(modifier = Modifier.size(0.dp, 16.dp))
                Column(Modifier.fillMaxSize()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.9f), // Expand to take remaining space
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                        verticalArrangement = Arrangement.Bottom,
                        state = scrollState // Assign LazyListState to LazyColumn
                    ) {
                        // "yyyy-MM-dd HH:mm:ss"
                        //Text(text = "Sender: ${message.user ?: ""}") // Display user ID
                        items(displayedMessages) { message ->
                            val isCurrentUserMessage = (userId == message.userId)
                            val arrangement =
                                if (isCurrentUserMessage) Arrangement.End else Arrangement.Start
                            val textBackgroundColor = if (isCurrentUserMessage) Color(0xFFFFEB3B) else Color.DarkGray
                            val textColor = if (isCurrentUserMessage) Color.Black else Color.White

                            val timestampShow =
                                SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(
                                    Date(message.timestamp as Long)
                                )
                            var image = painterResource(R.drawable.user)

                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = arrangement,
                                    verticalAlignment = Alignment.Bottom
                                ) {
                                    if (isCurrentUserMessage) {
                                        Text(text = "$userName", fontSize = 12.sp, color = contentColor)
                                    } else {
                                        Text(text = "${message.userName}", fontSize = 12.sp, color = contentColor)
                                    }
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = arrangement,
                                    verticalAlignment = Alignment.Bottom
                                ) {
                                    if (!isCurrentUserMessage) {
                                        // 여기에 uri값 받아서 이미지 출력하고 싶음
                                        Image(
                                            painter = image,
                                            contentDescription = "",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .size(45.dp)
                                                .clip(RoundedCornerShape(30))
                                                .align(Alignment.CenterVertically)
                                        )
                                        Spacer(modifier = Modifier.size(8.dp))
                                    }

                                    if (isCurrentUserMessage) {
                                        Text(text = timestampShow, color = Color.White, fontSize = 9.sp)
                                        Spacer(modifier = Modifier.size(4.dp, 0.dp))
                                    }

                                    Button(
                                        onClick = { /*TODO*/ },
                                        colors = ButtonDefaults.buttonColors(textBackgroundColor),
                                        shape = RoundedCornerShape(25),
                                        modifier = Modifier.align(Alignment.CenterVertically),
                                        contentPadding = PaddingValues(start = 12.dp, end = 12.dp, top = 4.dp , bottom = 4.dp)
                                    ) {
                                        message.text?.let {
                                            Text(
                                                text = it,
                                                textAlign = TextAlign.Start,
                                                color = textColor
                                            )
                                        }
                                    }

                                    if (!isCurrentUserMessage) {
                                        Spacer(modifier = Modifier.size(4.dp, 0.dp))
                                        Text(text = timestampShow, color = Color.White, fontSize = 9.sp, modifier = Modifier.padding(bottom = 4.dp))
                                    }
                                }
                                Spacer(modifier = Modifier.size(0.dp, 8.dp))
                            }
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(13.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { /*TODO*/ },
                            shape = RectangleShape,
                            colors = ButtonDefaults.buttonColors(contentColor),
                            modifier = Modifier.padding(0.dp)
                        ) {
                            Text(text = "+", color = backgroundColor)
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        TextField(
                            value = newMessage,
                            onValueChange = { newMessage = it },
                            placeholder = { Text(text = "채팅 입력", fontSize = 12.sp) },
                            modifier = Modifier
                                .weight(1f)
                                .border(
                                    BorderStroke(
                                        width = 1.dp,
                                        color = contentColor
                                    ), shape = RoundedCornerShape(
                                        10
                                    )
                                ),
                            maxLines = 1,
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
//                                focusedTextColor = contentColor,
                                disabledTextColor = Color.Gray,
//                                unfocusedTextColor = contentColor,
//                                focusedPlaceholderColor = Color.Gray,
                                disabledPlaceholderColor = Color.Gray,
//                                unfocusedPlaceholderColor = Color.Gray,
                                cursorColor = contentColor
                            )
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = {
                                if (newMessage.isNotBlank()) {
                                    val messageData = mapOf(
                                        "text" to newMessage,
                                        "userId" to userId,
                                        "userName" to userName,
                                        "timestamp" to ServerValue.TIMESTAMP,
                                        "userImage" to userImage.toString()
                                    )
                                    messageRef1.push().setValue(messageData)
//                            chatRoomRef.push()

//                            messages.add(newMessage)
                                    newMessage = "" // Clear the input
//                            time.add(ServerValue.TIMESTAMP.toString())
                                }
                            },
                            colors = ButtonDefaults.buttonColors(contentColor),
                            modifier = Modifier.wrapContentWidth()
                        ) {
                            Text(text = "보내기", color = backgroundColor)
                        }
                    }
                }
            }
        }

        LaunchedEffect(displayedMessages.size) {
            scrollState.animateScrollToItem(displayedMessages.size)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Chatting4(onNavigateToChatview: () -> Unit) {
    val user = Firebase.auth.currentUser
    val backgroundColor = Purple40
    val contentColor = Color.Black

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = backgroundColor
    ) {
        var newMessage by remember { mutableStateOf("") }
        var displayedMessages by remember { mutableStateOf(emptyList<Message>()) }
        val messageRef1 = remember { Firebase.database.getReference("chat").child("이수빈") }

        user?.uid
        var userName: String? by remember { mutableStateOf("") }
        var userEmail: String? by remember { mutableStateOf("") }
        var userId: String? by remember { mutableStateOf("") }
        var userImage: Uri? by remember { mutableStateOf(null) }
        user?.let {
            userName = it.displayName
            userEmail = it.email
            userId = it.uid
            userImage = it.photoUrl
        }

        // Write a message to the database
        LaunchedEffect(Unit) {
            messageRef1.addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

                    val text = snapshot.child("text").getValue(String::class.java)
                    val userId = snapshot.child("userId").getValue(String::class.java)
                    val userName = snapshot.child("userName").getValue(String::class.java)
                    val timestamp = snapshot.child("timestamp").getValue(Long::class.java)
                    val userImage = snapshot.child("userImage").getValue(String::class.java)

                    if (text != null && userId != null && userName != null && timestamp != null && userImage != null) {
                        val message = Message(text, userId, userName, timestamp, userImage)

                        if (!displayedMessages.contains(message)) {
                            displayedMessages += message
                        }
                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
//            TODO("Not yet implemented")
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
//            TODO("Not yet implemented")
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
//            TODO("Not yet implemented")
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
                }
            })

        }

        // Create LazyListState to manage scrolling
        val scrollState = rememberLazyListState()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
        ) {
            Column(modifier = Modifier.align(Alignment.TopStart)) {
                Row(modifier = Modifier.padding(16.dp)) {
                    Icon(
                        imageVector = Icons.Outlined.KeyboardArrowLeft,
                        contentDescription = "back",
                        tint = contentColor,
                        modifier = Modifier
                            .align(
                                Alignment.CenterVertically
                            )
                            .size(32.dp)
                            .clickable(onClick = onNavigateToChatview)
                    )
                    Spacer(modifier = Modifier.size(24.dp, 0.dp))
                    Text(
                        text = "이수빈",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = contentColor,
                        modifier = Modifier.align(
                            Alignment.CenterVertically
                        )
                    )
                }
                Divider(color = contentColor.copy(0.2f))
                Spacer(modifier = Modifier.size(0.dp, 16.dp))
                Column(Modifier.fillMaxSize()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.9f), // Expand to take remaining space
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                        verticalArrangement = Arrangement.Bottom,
                        state = scrollState // Assign LazyListState to LazyColumn
                    ) {
                        // "yyyy-MM-dd HH:mm:ss"
                        //Text(text = "Sender: ${message.user ?: ""}") // Display user ID
                        items(displayedMessages) { message ->
                            val isCurrentUserMessage = (userId == message.userId)
                            val arrangement =
                                if (isCurrentUserMessage) Arrangement.End else Arrangement.Start
                            val textBackgroundColor = if (isCurrentUserMessage) Color(0xFFFFEB3B) else Color.DarkGray
                            val textColor = if (isCurrentUserMessage) Color.Black else Color.White

                            val timestampShow =
                                SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(
                                    Date(message.timestamp as Long)
                                )
                            var image = painterResource(R.drawable.user)

                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = arrangement,
                                    verticalAlignment = Alignment.Bottom
                                ) {
                                    if (isCurrentUserMessage) {
                                        Text(text = "$userName", fontSize = 12.sp, color = contentColor)
                                    } else {
                                        Text(text = "${message.userName}", fontSize = 12.sp, color = contentColor)
                                    }
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = arrangement,
                                    verticalAlignment = Alignment.Bottom
                                ) {
                                    if (!isCurrentUserMessage) {
                                        // 여기에 uri값 받아서 이미지 출력하고 싶음
                                        Image(
                                            painter = image,
                                            contentDescription = "",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .size(45.dp)
                                                .clip(RoundedCornerShape(30))
                                                .align(Alignment.CenterVertically)
                                        )
                                        Spacer(modifier = Modifier.size(8.dp))
                                    }

                                    if (isCurrentUserMessage) {
                                        Text(text = timestampShow, color = Color.White, fontSize = 9.sp)
                                        Spacer(modifier = Modifier.size(4.dp, 0.dp))
                                    }

                                    Button(
                                        onClick = { /*TODO*/ },
                                        colors = ButtonDefaults.buttonColors(textBackgroundColor),
                                        shape = RoundedCornerShape(25),
                                        modifier = Modifier.align(Alignment.CenterVertically),
                                        contentPadding = PaddingValues(start = 12.dp, end = 12.dp, top = 4.dp , bottom = 4.dp)
                                    ) {
                                        message.text?.let {
                                            Text(
                                                text = it,
                                                textAlign = TextAlign.Start,
                                                color = textColor
                                            )
                                        }
                                    }

                                    if (!isCurrentUserMessage) {
                                        Spacer(modifier = Modifier.size(4.dp, 0.dp))
                                        Text(text = timestampShow, color = Color.White, fontSize = 9.sp, modifier = Modifier.padding(bottom = 4.dp))
                                    }
                                }
                                Spacer(modifier = Modifier.size(0.dp, 8.dp))
                            }
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(13.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { /*TODO*/ },
                            shape = RectangleShape,
                            colors = ButtonDefaults.buttonColors(contentColor),
                            modifier = Modifier.padding(0.dp)
                        ) {
                            Text(text = "+", color = backgroundColor)
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        TextField(
                            value = newMessage,
                            onValueChange = { newMessage = it },
                            placeholder = { Text(text = "채팅 입력", fontSize = 12.sp) },
                            modifier = Modifier
                                .weight(1f)
                                .border(
                                    BorderStroke(
                                        width = 1.dp,
                                        color = contentColor
                                    ), shape = RoundedCornerShape(
                                        10
                                    )
                                ),
                            maxLines = 1,
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
//                                focusedTextColor = contentColor,
                                disabledTextColor = Color.Gray,
//                                unfocusedTextColor = contentColor,
//                                focusedPlaceholderColor = Color.Gray,
                                disabledPlaceholderColor = Color.Gray,
//                                unfocusedPlaceholderColor = Color.Gray,
                                cursorColor = contentColor
                            )
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = {
                                if (newMessage.isNotBlank()) {
                                    val messageData = mapOf(
                                        "text" to newMessage,
                                        "userId" to userId,
                                        "userName" to userName,
                                        "timestamp" to ServerValue.TIMESTAMP,
                                        "userImage" to userImage.toString()
                                    )
                                    messageRef1.push().setValue(messageData)
//                            chatRoomRef.push()

//                            messages.add(newMessage)
                                    newMessage = "" // Clear the input
//                            time.add(ServerValue.TIMESTAMP.toString())
                                }
                            },
                            colors = ButtonDefaults.buttonColors(contentColor),
                            modifier = Modifier.wrapContentWidth()
                        ) {
                            Text(text = "보내기", color = backgroundColor)
                        }
                    }
                }
            }
        }

        LaunchedEffect(displayedMessages.size) {
            scrollState.animateScrollToItem(displayedMessages.size)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Chatting5(onNavigateToChatview: () -> Unit) {
    val user = Firebase.auth.currentUser
    val backgroundColor = Purple40
    val contentColor = Color.Black

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = backgroundColor
    ) {
        var newMessage by remember { mutableStateOf("") }
        var displayedMessages by remember { mutableStateOf(emptyList<Message>()) }
        val messageRef1 = remember { Firebase.database.getReference("chat").child("황인선") }

        user?.uid
        var userName: String? by remember { mutableStateOf("") }
        var userEmail: String? by remember { mutableStateOf("") }
        var userId: String? by remember { mutableStateOf("") }
        var userImage: Uri? by remember { mutableStateOf(null) }
        user?.let {
            userName = it.displayName
            userEmail = it.email
            userId = it.uid
            userImage = it.photoUrl
        }

        // Write a message to the database
        LaunchedEffect(Unit) {
            messageRef1.addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

                    val text = snapshot.child("text").getValue(String::class.java)
                    val userId = snapshot.child("userId").getValue(String::class.java)
                    val userName = snapshot.child("userName").getValue(String::class.java)
                    val timestamp = snapshot.child("timestamp").getValue(Long::class.java)
                    val userImage = snapshot.child("userImage").getValue(String::class.java)

                    if (text != null && userId != null && userName != null && timestamp != null && userImage != null) {
                        val message = Message(text, userId, userName, timestamp, userImage)

                        if (!displayedMessages.contains(message)) {
                            displayedMessages += message
                        }
                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
//            TODO("Not yet implemented")
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
//            TODO("Not yet implemented")
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
//            TODO("Not yet implemented")
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
                }
            })

        }

        // Create LazyListState to manage scrolling
        val scrollState = rememberLazyListState()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
        ) {
            Column(modifier = Modifier.align(Alignment.TopStart)) {
                Row(modifier = Modifier.padding(16.dp)) {
                    Icon(
                        imageVector = Icons.Outlined.KeyboardArrowLeft,
                        contentDescription = "back",
                        tint = contentColor,
                        modifier = Modifier
                            .align(
                                Alignment.CenterVertically
                            )
                            .size(32.dp)
                            .clickable(onClick = onNavigateToChatview)
                    )
                    Spacer(modifier = Modifier.size(24.dp, 0.dp))
                    Text(
                        text = "황인선",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = contentColor,
                        modifier = Modifier.align(
                            Alignment.CenterVertically
                        )
                    )
                }
                Divider(color = contentColor.copy(0.2f))
                Spacer(modifier = Modifier.size(0.dp, 16.dp))
                Column(Modifier.fillMaxSize()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.9f), // Expand to take remaining space
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                        verticalArrangement = Arrangement.Bottom,
                        state = scrollState // Assign LazyListState to LazyColumn
                    ) {
                        // "yyyy-MM-dd HH:mm:ss"
                        //Text(text = "Sender: ${message.user ?: ""}") // Display user ID
                        items(displayedMessages) { message ->
                            val isCurrentUserMessage = (userId == message.userId)
                            val arrangement =
                                if (isCurrentUserMessage) Arrangement.End else Arrangement.Start
                            val textBackgroundColor = if (isCurrentUserMessage) Color(0xFFFFEB3B) else Color.DarkGray
                            val textColor = if (isCurrentUserMessage) Color.Black else Color.White

                            val timestampShow =
                                SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(
                                    Date(message.timestamp as Long)
                                )
                            var image = painterResource(R.drawable.user)

                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = arrangement,
                                    verticalAlignment = Alignment.Bottom
                                ) {
                                    if (isCurrentUserMessage) {
                                        Text(text = "$userName", fontSize = 12.sp, color = contentColor)
                                    } else {
                                        Text(text = "${message.userName}", fontSize = 12.sp, color = contentColor)
                                    }
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = arrangement,
                                    verticalAlignment = Alignment.Bottom
                                ) {
                                    if (!isCurrentUserMessage) {
                                        // 여기에 uri값 받아서 이미지 출력하고 싶음
                                        Image(
                                            painter = image,
                                            contentDescription = "",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .size(45.dp)
                                                .clip(RoundedCornerShape(30))
                                                .align(Alignment.CenterVertically)
                                        )
                                        Spacer(modifier = Modifier.size(8.dp))
                                    }

                                    if (isCurrentUserMessage) {
                                        Text(text = timestampShow, color = Color.White, fontSize = 9.sp)
                                        Spacer(modifier = Modifier.size(4.dp, 0.dp))
                                    }

                                    Button(
                                        onClick = { /*TODO*/ },
                                        colors = ButtonDefaults.buttonColors(textBackgroundColor),
                                        shape = RoundedCornerShape(25),
                                        modifier = Modifier.align(Alignment.CenterVertically),
                                        contentPadding = PaddingValues(start = 12.dp, end = 12.dp, top = 4.dp , bottom = 4.dp)
                                    ) {
                                        message.text?.let {
                                            Text(
                                                text = it,
                                                textAlign = TextAlign.Start,
                                                color = textColor
                                            )
                                        }
                                    }

                                    if (!isCurrentUserMessage) {
                                        Spacer(modifier = Modifier.size(4.dp, 0.dp))
                                        Text(text = timestampShow, color = Color.White, fontSize = 9.sp, modifier = Modifier.padding(bottom = 4.dp))
                                    }
                                }
                                Spacer(modifier = Modifier.size(0.dp, 8.dp))
                            }
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(13.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { /*TODO*/ },
                            shape = RectangleShape,
                            colors = ButtonDefaults.buttonColors(contentColor),
                            modifier = Modifier.padding(0.dp)
                        ) {
                            Text(text = "+", color = backgroundColor)
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        TextField(
                            value = newMessage,
                            onValueChange = { newMessage = it },
                            placeholder = { Text(text = "채팅 입력", fontSize = 12.sp) },
                            modifier = Modifier
                                .weight(1f)
                                .border(
                                    BorderStroke(
                                        width = 1.dp,
                                        color = contentColor
                                    ), shape = RoundedCornerShape(
                                        10
                                    )
                                ),
                            maxLines = 1,
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
//                                focusedTextColor = contentColor,
                                disabledTextColor = Color.Gray,
//                                unfocusedTextColor = contentColor,
//                                focusedPlaceholderColor = Color.Gray,
                                disabledPlaceholderColor = Color.Gray,
//                                unfocusedPlaceholderColor = Color.Gray,
                                cursorColor = contentColor
                            )
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = {
                                if (newMessage.isNotBlank()) {
                                    val messageData = mapOf(
                                        "text" to newMessage,
                                        "userId" to userId,
                                        "userName" to userName,
                                        "timestamp" to ServerValue.TIMESTAMP,
                                        "userImage" to userImage.toString()
                                    )
                                    messageRef1.push().setValue(messageData)
//                            chatRoomRef.push()

//                            messages.add(newMessage)
                                    newMessage = "" // Clear the input
//                            time.add(ServerValue.TIMESTAMP.toString())
                                }
                            },
                            colors = ButtonDefaults.buttonColors(contentColor),
                            modifier = Modifier.wrapContentWidth()
                        ) {
                            Text(text = "보내기", color = backgroundColor)
                        }
                    }
                }
            }
        }

        LaunchedEffect(displayedMessages.size) {
            scrollState.animateScrollToItem(displayedMessages.size)
        }
    }
}

@Composable
fun ChatView(
    onNavigateToChatting1: () -> Unit,
    onNavigateToChatting2: () -> Unit,
    onNavigateToChatting3: () -> Unit,
    onNavigateToChatting4: () -> Unit,
    onNavigateToChatting5: () -> Unit,
) {

    val backgroundColor = Purple40
    val contentColor = Color.Black

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = backgroundColor
    ) {
        var chatRoomName = remember { mutableStateListOf("민형준", "문세희", "여효진", "이수빈", "황인선") }
        Box(modifier = Modifier.fillMaxSize()){
            Column(modifier = Modifier.align(Alignment.Center)) {
                LazyColumn(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    items(chatRoomName) { roomName ->

                        val onNavigateToChatting: () -> Unit = when (roomName) {
                            "민형준" -> onNavigateToChatting1
                            "문세희" -> onNavigateToChatting2
                            "여효진" -> onNavigateToChatting3
                            "이수빈" -> onNavigateToChatting4
                            else -> onNavigateToChatting5
                        }

                        RoomList(roomName = roomName, onNavigateToChatting = onNavigateToChatting, backgroundColor = backgroundColor, contentColor = contentColor)
                        Spacer(modifier = Modifier.size(24.dp))
                    }
                }
                Spacer(modifier = Modifier.size(64.dp))
            }
        }
    }
}


//메세지 창에 띄울 메세지 목록의 틀
@Composable
fun RoomList(roomName: String, onNavigateToChatting: () -> Unit, backgroundColor: Color, contentColor: Color) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(64.dp, 0.dp)
            //메세지 목록 중 하나를 눌렀을 때 해당 목록의 메세지 창으로 전환
            .clickable(onClick = onNavigateToChatting),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        shape = RectangleShape,
    ) {
        Row(
            Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.width(15.dp))
            Surface(
                modifier = Modifier.size(60.dp).border(width = 2.dp, color = contentColor.copy(0.2f), RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.somac_logo),
                    contentDescription = "단톡방 사진",
                    contentScale = ContentScale.Fit
                )
            }
            Spacer(modifier = Modifier.width(24.dp))
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(268.dp),
                verticalArrangement = Arrangement.Bottom,
            ) {

                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${roomName}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(25.dp),
                        color = contentColor
                    )
                }

            }
            Spacer(modifier = Modifier.width(15.dp))
        }
    }
}