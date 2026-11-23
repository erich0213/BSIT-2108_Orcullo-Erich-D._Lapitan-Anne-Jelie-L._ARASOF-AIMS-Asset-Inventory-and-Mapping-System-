package com.arasof.aims

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.arasof.aims.ui.theme.ARASOFAIMSTheme
import ui.screens.AppTab
import ui.screens.DashboardScreen
import ui.screens.LoginScreen
import ui.screens.SplashScreen

enum class ScreenState {
    SPLASH,
    LOGIN,
    DASHBOARD
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ARASOFAIMSTheme {
                var currentScreen by remember { mutableStateOf(ScreenState.SPLASH) }
                var loggedInUser by remember { mutableStateOf("") }

                when (currentScreen) {
                    ScreenState.SPLASH -> {
                        SplashScreen(
                            onFinished = {
                                currentScreen = ScreenState.LOGIN
                            }
                        )
                    }

                    ScreenState.LOGIN -> {
                        LoginScreen(
                            onLoginSuccess = { username ->
                                loggedInUser = username
                                currentScreen = ScreenState.DASHBOARD
                            }
                        )
                    }

                    ScreenState.DASHBOARD -> {
                        DashboardScreen(
                            username = loggedInUser,
                            assetCount = 0,
                            buildingCount = 0,
                            departmentCount = 0,
                            roomCount = 0,
                            onOpenMap = {
                                Toast.makeText(this, "Map coming soon", Toast.LENGTH_SHORT).show()
                            },
                            onOpenAssets = {
                                Toast.makeText(this, "Assets coming soon", Toast.LENGTH_SHORT).show()
                            },
                            onOpenBuildingsRooms = {
                                Toast.makeText(this, "Buildings & Rooms coming soon", Toast.LENGTH_SHORT).show()
                            },
                            onOpenReports = {
                                Toast.makeText(this, "Reports coming soon", Toast.LENGTH_SHORT).show()
                            },
                            onOpenProfile = {
                                loggedInUser = ""
                                currentScreen = ScreenState.LOGIN
                            },
                            onSelectTab = { tab ->
                                when (tab) {
                                    AppTab.HOME -> currentScreen = ScreenState.DASHBOARD
                                    AppTab.MAP -> Toast.makeText(this, "Map tab selected", Toast.LENGTH_SHORT).show()
                                    AppTab.ASSETS -> Toast.makeText(this, "Assets tab selected", Toast.LENGTH_SHORT).show()
                                    AppTab.MORE -> {
                                        loggedInUser = ""
                                        currentScreen = ScreenState.LOGIN
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}