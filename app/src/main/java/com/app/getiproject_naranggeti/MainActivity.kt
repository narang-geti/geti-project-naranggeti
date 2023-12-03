@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.app.getiproject_naranggeti

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.getiproject_naranggeti.ui.theme.GetiProject_naranggetiTheme
import com.app.getiproject_naranggeti.ui.theme.Purple40

data class BottomNavigationItem(
    val title: String,
    val selectedICon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNews: Boolean,
    val badgeCount: Int? = null

)


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Navi()
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navi() {

    GetiProject_naranggetiTheme {

        val CustomColor = Color(0xFF608EBD)

        val items = listOf(
            BottomNavigationItem(
                title = "Home",
                selectedICon = Icons.Filled.Home,
                unselectedIcon = Icons.Outlined.Home,
                hasNews = false,
            ),
            BottomNavigationItem(
                title = "Chat",
                selectedICon = Icons.Filled.Email,
                unselectedIcon = Icons.Outlined.Email,
                hasNews = false,
                badgeCount = 22
            ),
            BottomNavigationItem(
                title = "Menu",
                selectedICon = Icons.Filled.Menu,
                unselectedIcon = Icons.Outlined.Menu,
                hasNews = false,
            ),
            BottomNavigationItem(
                title = "Profile",
                selectedICon = Icons.Filled.AccountCircle,
                unselectedIcon = Icons.Outlined.AccountCircle,
                hasNews = false,
            ),

            )

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = CustomColor
        ) {
            var selectedItemIndex by rememberSaveable {
                mutableStateOf(0)

            }

            val navController = rememberNavController()



            Scaffold(
                bottomBar = {
                    NavigationBar {
                        items.forEachIndexed { index, item ->
                            NavigationBarItem(
                                selected = selectedItemIndex == index,
                                onClick = {
                                    selectedItemIndex = index

                                    when (item.title) {
                                        "Home" -> navController.navigate("welcome")
                                        "Chat" -> navController.navigate("imei")
                                        "Menu" -> navController.navigate("menu")
                                        "Profile" -> navController.navigate("info")
                                    }

                                },
                                label = {
                                    Text(text = item.title)
                                },
                                icon = {
                                    BadgedBox(
                                        badge = {
                                            if (item.badgeCount != null) {
                                                Badge {
                                                    Text(text = item.badgeCount.toString())
                                                }
                                            } else if (item.hasNews) {
                                                Badge()
                                            }
                                        }
                                    ) {
                                        Icon(
                                            imageVector = if (index == selectedItemIndex) {
                                                item.selectedICon

                                            } else item.unselectedIcon,
                                            contentDescription = item.title
                                        )

                                    }
                                }
                            )
                        }

                    }
                }
            ) { innerPadding ->
                Surface(modifier = Modifier.padding(innerPadding)) {


                }

//            val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "login") {
                    composable("start") {
                        StartScreen()
                    }
                    composable("detect") {
                        DetectScreen(navController)
                    }
                    composable("login") {
                        LoginScreen(LoginViewModel(), {}, navController)
                    }
                    composable("Sign Up") {
                        SignupScreen(navController)
                    }
                    composable("customer") {
                        CustomerEvaluation(navController)
                    }
                    composable("grade") {
                        GradeScreen(navController)
                    }
                    composable("description") {
                        DescriptionScreen(navController)
                    }
                    composable("customerReviews") {
                        CustomerReviewScreen(navController)
                    }
                    composable("menu") {
                        MenuScreen(navController)
                    }
                    composable("product") {
                        ProductRegistration(navController)
                    }
                    composable("info") {
                        UserInfo(navController)
                    }
                    composable("imei") {
                        ImeinScreen(navController)
                    }
                    composable("welcome") {
                        WelcomeScreen(navController)
                    }


                }
            }
        }
    }
}

