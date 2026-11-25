package ui.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val MaroonPrimary = Color(0xFF7A0019)
private val SurfaceLight = Color(0xFFF8F9FA)
private val TextMuted = Color(0xFF6C757D)
private val TextDark = Color(0xFF212529)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    username: String,
    logoResId: Int? = null,
    @DrawableRes backgroundPhotoResId: Int? = null,
    onBackToDashboard: () -> Unit,
    onLogout: () -> Unit,
    onSelectTab: (AppTab) -> Unit,
    onEditProfile: () -> Unit = {},
    onChangePassword: () -> Unit = {},
    onAboutAims: () -> Unit = {},
    onHelpSupport: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    var activeName by remember(username) { mutableStateOf(username.ifBlank { "User" }) }
    var activeSubtitle by remember { mutableStateOf("User") }

    var showEditProfileDialog by remember { mutableStateOf(false) }
    var showChangePasswordDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }
    var showHelpDialog by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var statusNotice by remember { mutableStateOf<String?>(null) }

    val resolvedBgResId = backgroundPhotoResId ?: com.arasof.aims.R.drawable.splash_bg

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Profile",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackToDashboard) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            onSettingsClick()
                            showSettingsDialog = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaroonPrimary)
            )
        },
        bottomBar = {
            ArasofBottomBar(
                currentTab = AppTab.MORE,
                onSelectTab = onSelectTab
            )
        },
        containerColor = SurfaceLight
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Pa-fade pataas na background photo sa ibaba
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(220.dp)
            ) {
                Image(
                    painter = painterResource(id = resolvedBgResId),
                    contentDescription = "Campus Building Background",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                0.0f to SurfaceLight,
                                0.45f to SurfaceLight.copy(alpha = 0.72f),
                                0.78f to Color.Transparent,
                                1.0f to Color.Black.copy(alpha = 0.55f)
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "BATANGAS STATE UNIVERSITY",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "ARASOF-NASUGBU",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // User Profile Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (logoResId != null) {
                            Image(
                                painter = painterResource(id = logoResId),
                                contentDescription = "Profile Logo",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .background(MaroonPrimary.copy(alpha = 0.12f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Profile Icon",
                                    tint = MaroonPrimary,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Text(
                                text = activeName,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextDark
                            )
                            Text(
                                text = activeSubtitle,
                                fontSize = 13.sp,
                                color = TextMuted
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Options List
                ProfileOptionCard(
                    icon = Icons.Default.Edit,
                    label = "Edit Profile",
                    onClick = {
                        onEditProfile()
                        showEditProfileDialog = true
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                ProfileOptionCard(
                    icon = Icons.Default.Lock,
                    label = "Change Password",
                    onClick = {
                        onChangePassword()
                        showChangePasswordDialog = true
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                ProfileOptionCard(
                    icon = Icons.Default.Info,
                    label = "About ARASOF AIMS",
                    onClick = {
                        onAboutAims()
                        showAboutDialog = true
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                ProfileOptionCard(
                    icon = Icons.AutoMirrored.Filled.HelpOutline,
                    label = "Help & Support",
                    onClick = {
                        onHelpSupport()
                        showHelpDialog = true
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Logout Option (Highlighted Maroon)
                ProfileOptionCard(
                    icon = Icons.AutoMirrored.Filled.Logout,
                    label = "Log Out",
                    isLogout = true,
                    onClick = { showLogoutDialog = true }
                )

                Spacer(modifier = Modifier.height(170.dp))
            }
        }
    }

    if (showEditProfileDialog) {
        var nameInput by remember(activeName) { mutableStateOf(activeName) }
        var subtitleInput by remember(activeSubtitle) { mutableStateOf(activeSubtitle) }

        AlertDialog(
            onDismissRequest = { showEditProfileDialog = false },
            title = { Text("Edit Profile", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = nameInput,
                        onValueChange = { nameInput = it },
                        label = { Text("Display Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = subtitleInput,
                        onValueChange = { subtitleInput = it },
                        label = { Text("Label") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditProfileDialog = false }) {
                    Text("Cancel", color = TextMuted)
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (nameInput.isNotBlank()) {
                            activeName = nameInput.trim()
                        }
                        activeSubtitle = subtitleInput.trim().ifBlank { "User" }
                        showEditProfileDialog = false
                        statusNotice = "Profile updated."
                    }
                ) {
                    Text("Save", color = MaroonPrimary, fontWeight = FontWeight.SemiBold)
                }
            }
        )
    }

    if (showChangePasswordDialog) {
        var currentPassword by remember { mutableStateOf("") }
        var newPassword by remember { mutableStateOf("") }
        var confirmPassword by remember { mutableStateOf("") }
        var errorMessage by remember { mutableStateOf<String?>(null) }

        AlertDialog(
            onDismissRequest = { showChangePasswordDialog = false },
            title = { Text("Change Password", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = currentPassword,
                        onValueChange = {
                            currentPassword = it
                            errorMessage = null
                        },
                        label = { Text("Current Password") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = {
                            newPassword = it
                            errorMessage = null
                        },
                        label = { Text("New Password") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = {
                            confirmPassword = it
                            errorMessage = null
                        },
                        label = { Text("Confirm New Password") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (errorMessage != null) {
                        Text(
                            text = errorMessage.orEmpty(),
                            color = MaroonPrimary,
                            fontSize = 12.sp
                        )
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { showChangePasswordDialog = false }) {
                    Text("Cancel", color = TextMuted)
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        when {
                            currentPassword.isBlank() || newPassword.isBlank() || confirmPassword.isBlank() -> {
                                errorMessage = "Please fill out all password fields."
                            }
                            newPassword != confirmPassword -> {
                                errorMessage = "New passwords do not match."
                            }
                            else -> {
                                showChangePasswordDialog = false
                                statusNotice = "Password updated for $activeName."
                            }
                        }
                    }
                ) {
                    Text("Update", color = MaroonPrimary, fontWeight = FontWeight.SemiBold)
                }
            }
        )
    }

    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            title = { Text("About ARASOF AIMS", fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    text = "ARASOF-AIMS is the Asset and Inventory Management System for Batangas State University - ARASOF-Nasugbu, supporting campus asset tracking, building and room records, map viewing, and report generation.",
                    fontSize = 14.sp,
                    color = TextDark
                )
            },
            confirmButton = {
                TextButton(onClick = { showAboutDialog = false }) {
                    Text("Close", color = MaroonPrimary, fontWeight = FontWeight.SemiBold)
                }
            }
        )
    }

    if (showHelpDialog) {
        AlertDialog(
            onDismissRequest = { showHelpDialog = false },
            title = { Text("Help & Support", fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    text = "Need assistance with asset entries, building/room locations, or report generation? Reach out to the Batangas State University - ARASOF-Nasugbu support desk.",
                    fontSize = 14.sp,
                    color = TextDark
                )
            },
            confirmButton = {
                TextButton(onClick = { showHelpDialog = false }) {
                    Text("OK", color = MaroonPrimary, fontWeight = FontWeight.SemiBold)
                }
            }
        )
    }

    if (showSettingsDialog) {
        var notificationsEnabled by remember { mutableStateOf(true) }

        AlertDialog(
            onDismissRequest = { showSettingsDialog = false },
            title = { Text("Settings", fontWeight = FontWeight.Bold) },
            text = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Activity Alerts",
                        fontSize = 14.sp,
                        color = TextDark
                    )
                    Switch(
                        checked = notificationsEnabled,
                        onCheckedChange = { notificationsEnabled = it },
                        colors = SwitchDefaults.colors(checkedTrackColor = MaroonPrimary)
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showSettingsDialog = false }) {
                    Text("Done", color = MaroonPrimary, fontWeight = FontWeight.SemiBold)
                }
            }
        )
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Log Out", fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    text = "Are you sure you want to log out of your account?",
                    fontSize = 14.sp,
                    color = TextDark
                )
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel", color = TextMuted)
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        onLogout()
                    }
                ) {
                    Text("Log Out", color = MaroonPrimary, fontWeight = FontWeight.SemiBold)
                }
            }
        )
    }

    if (statusNotice != null) {
        AlertDialog(
            onDismissRequest = { statusNotice = null },
            title = { Text("Notice", fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    text = statusNotice.orEmpty(),
                    fontSize = 14.sp,
                    color = TextDark
                )
            },
            confirmButton = {
                TextButton(onClick = { statusNotice = null }) {
                    Text("OK", color = MaroonPrimary, fontWeight = FontWeight.SemiBold)
                }
            }
        )
    }
}

@Composable
private fun ProfileOptionCard(
    icon: ImageVector,
    label: String,
    isLogout: Boolean = false,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isLogout) MaroonPrimary else TextDark,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(14.dp))
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = if (isLogout) FontWeight.Bold else FontWeight.Medium,
                color = if (isLogout) MaroonPrimary else TextDark,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = if (isLogout) MaroonPrimary else TextMuted,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}