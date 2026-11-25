package com.arasof.aims

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.arasof.aims.ui.theme.ARASOFAIMSTheme
import ui.screens.AddAssetScreen
import ui.screens.AppTab
import ui.screens.AssetDetailsScreen
import ui.screens.AssetsScreen
import ui.screens.BuildingsRoomsScreen
import ui.screens.DashboardScreen
import ui.screens.LoginScreen
import ui.screens.MapScreen
import ui.screens.ProfileScreen
import ui.screens.ReportsScreen
import ui.screens.SampleCampusAsset
import ui.screens.SplashScreen
import ui.screens.sampleCampusAssets
import ui.screens.sampleCampusBuildings

enum class ScreenState {
    SPLASH,
    LOGIN,
    DASHBOARD,
    MAP,
    ASSETS,
    ADD_ASSET,
    ASSET_DETAILS,
    BUILDINGS_ROOMS,
    REPORTS,
    PROFILE
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ARASOFAIMSTheme {
                var currentScreen by remember { mutableStateOf(ScreenState.SPLASH) }
                var loggedInUser by remember { mutableStateOf("") }

                // DITO ANG PAGBABAGO: Inilagay ang sample assets sa initial state ng listahan
                val assetList = remember {
                    mutableStateListOf<SampleCampusAsset>().apply {
                        addAll(sampleCampusAssets)
                    }
                }
                var selectedAsset by remember { mutableStateOf<SampleCampusAsset?>(null) }

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
                            assetCount = assetList.size,
                            buildingCount = sampleCampusBuildings.size,
                            departmentCount = 0,
                            roomCount = 0,
                            onOpenMap = {
                                currentScreen = ScreenState.MAP
                            },
                            onOpenAssets = {
                                currentScreen = ScreenState.ASSETS
                            },
                            onOpenBuildingsRooms = {
                                currentScreen = ScreenState.BUILDINGS_ROOMS
                            },
                            onOpenReports = {
                                currentScreen = ScreenState.REPORTS
                            },
                            onOpenProfile = {
                                currentScreen = ScreenState.PROFILE
                            },
                            onSelectTab = { tab ->
                                when (tab) {
                                    AppTab.HOME -> currentScreen = ScreenState.DASHBOARD
                                    AppTab.MAP -> currentScreen = ScreenState.MAP
                                    AppTab.ASSETS -> currentScreen = ScreenState.ASSETS
                                    AppTab.MORE -> currentScreen = ScreenState.PROFILE
                                }
                            }
                        )
                    }

                    ScreenState.MAP -> {
                        MapScreen(
                            onBackToDashboard = {
                                currentScreen = ScreenState.DASHBOARD
                            },
                            onSelectTab = { tab ->
                                when (tab) {
                                    AppTab.HOME -> currentScreen = ScreenState.DASHBOARD
                                    AppTab.MAP -> currentScreen = ScreenState.MAP
                                    AppTab.ASSETS -> currentScreen = ScreenState.ASSETS
                                    AppTab.MORE -> currentScreen = ScreenState.PROFILE
                                }
                            },
                            onOpenBuildingsRooms = {
                                currentScreen = ScreenState.BUILDINGS_ROOMS
                            }
                        )
                    }

                    ScreenState.ASSETS -> {
                        AssetsScreen(
                            onBackClick = {
                                currentScreen = ScreenState.DASHBOARD
                            },
                            onAddAssetClick = {
                                currentScreen = ScreenState.ADD_ASSET
                            },
                            onAssetClick = { clickedAsset ->
                                selectedAsset = clickedAsset
                                currentScreen = ScreenState.ASSET_DETAILS
                            },
                            assets = assetList // DITO ANG PAGBABAGO: Ipinasa ang live list papunta sa screen
                        )
                    }

                    ScreenState.ADD_ASSET -> {
                        AddAssetScreen(
                            onBackClick = {
                                currentScreen = ScreenState.ASSETS
                            },
                            onSaveAsset = { newAsset ->
                                val existingIndex = assetList.indexOfFirst { it.propertyNo == newAsset.propertyNo }
                                if (existingIndex >= 0) {
                                    assetList[existingIndex] = newAsset
                                } else {
                                    assetList.add(0, newAsset) // Idadagdag sa pinakataas ng listahan
                                }
                                currentScreen = ScreenState.ASSETS
                                Toast.makeText(this, "Asset saved", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }

                    ScreenState.ASSET_DETAILS -> {
                        val currentAsset = selectedAsset
                        if (currentAsset != null) {
                            AssetDetailsScreen(
                                asset = currentAsset,
                                onBackClick = {
                                    currentScreen = ScreenState.ASSETS
                                },
                                onConfirmClick = {
                                    currentScreen = ScreenState.ASSETS
                                    Toast.makeText(this, "Asset verified", Toast.LENGTH_SHORT).show()
                                }
                            )
                        } else {
                            currentScreen = ScreenState.ASSETS
                        }
                    }

                    ScreenState.BUILDINGS_ROOMS -> {
                        BuildingsRoomsScreen(
                            onBackClick = {
                                currentScreen = ScreenState.DASHBOARD
                            }
                        )
                    }

                    ScreenState.REPORTS -> {
                        ReportsScreen(
                            assets = assetList,
                            onBackClick = {
                                currentScreen = ScreenState.DASHBOARD
                            },
                            onSelectTab = { tab ->
                                when (tab) {
                                    AppTab.HOME -> currentScreen = ScreenState.DASHBOARD
                                    AppTab.MAP -> currentScreen = ScreenState.MAP
                                    AppTab.ASSETS -> currentScreen = ScreenState.ASSETS
                                    AppTab.MORE -> currentScreen = ScreenState.PROFILE
                                }
                            }
                        )
                    }

                    ScreenState.PROFILE -> {
                        ProfileScreen(
                            username = loggedInUser,
                            logoResId = R.drawable.bsu_logo,
                            backgroundPhotoResId = R.drawable.arasof_profile_bg, // o alisin ito para gamitin ang splash_bg
                            onBackToDashboard = {
                                currentScreen = ScreenState.DASHBOARD
                            },
                            onLogout = {
                                loggedInUser = ""
                                currentScreen = ScreenState.LOGIN
                            },
                            onSelectTab = { tab ->
                                when (tab) {
                                    AppTab.HOME -> currentScreen = ScreenState.DASHBOARD
                                    AppTab.MAP -> currentScreen = ScreenState.MAP
                                    AppTab.ASSETS -> currentScreen = ScreenState.ASSETS
                                    AppTab.MORE -> currentScreen = ScreenState.PROFILE
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}