package ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val MaroonPrimary = Color(0xFF7A0019)
private val SurfaceLight = Color(0xFFF8F9FA)
private val TextMuted = Color(0xFF6C757D)

enum class AppTab(val route: String, val label: String) {
    HOME("dashboard", "Home"),
    MAP("map_view", "Map"),
    ASSETS("asset_list", "Assets"),
    MORE("profile", "More")
}

@Composable
fun ArasofBottomBar(
    currentTab: AppTab,
    onSelectTab: (AppTab) -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 4.dp
    ) {
        AppTab.entries.forEach { tab ->
            val selected = currentTab == tab
            val icon = when (tab) {
                AppTab.HOME -> if (selected) Icons.Filled.Home else Icons.Outlined.Home
                AppTab.MAP -> if (selected) Icons.Filled.Map else Icons.Outlined.Map
                AppTab.ASSETS -> if (selected) Icons.Filled.Inventory2 else Icons.Outlined.Inventory2
                AppTab.MORE -> if (selected) Icons.Filled.MoreVert else Icons.Outlined.MoreVert
            }
            NavigationBarItem(
                selected = selected,
                onClick = { onSelectTab(tab) },
                icon = { Icon(imageVector = icon, contentDescription = tab.label) },
                label = { Text(text = tab.label, fontSize = 11.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaroonPrimary,
                    selectedTextColor = MaroonPrimary,
                    indicatorColor = MaroonPrimary.copy(alpha = 0.12f),
                    unselectedIconColor = TextMuted,
                    unselectedTextColor = TextMuted
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    username: String,
    assetCount: Int,
    buildingCount: Int,
    departmentCount: Int,
    roomCount: Int,
    onOpenMap: () -> Unit,
    onOpenAssets: () -> Unit,
    onOpenBuildingsRooms: () -> Unit,
    onOpenReports: () -> Unit,
    onOpenProfile: () -> Unit,
    onSelectTab: (AppTab) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "ARASOF AIMS",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onOpenBuildingsRooms) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onOpenProfile) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Profile",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaroonPrimary)
            )
        },
        bottomBar = {
            ArasofBottomBar(currentTab = AppTab.HOME, onSelectTab = onSelectTab)
        },
        containerColor = SurfaceLight
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            val displayName = username.ifBlank { "User" }
            Text(
                text = "Hello, $displayName!",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaroonPrimary
            )
            Text(
                text = "Welcome to ARASOF AIMS",
                fontSize = 13.sp,
                color = TextMuted
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DashboardFeatureCard(
                    title = "Map",
                    icon = Icons.Default.Map,
                    onClick = onOpenMap,
                    modifier = Modifier.weight(1f)
                )
                DashboardFeatureCard(
                    title = "Assets",
                    icon = Icons.Default.Inventory2,
                    onClick = onOpenAssets,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DashboardFeatureCard(
                    title = "Buildings &\nRooms",
                    icon = Icons.Default.Apartment,
                    onClick = onOpenBuildingsRooms,
                    modifier = Modifier.weight(1f)
                )
                DashboardFeatureCard(
                    title = "Reports",
                    icon = Icons.Default.BarChart,
                    onClick = onOpenReports,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Quick Stats",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaroonPrimary
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatSummaryCard(
                    label = "Buildings",
                    value = buildingCount.toString(),
                    icon = Icons.Default.Apartment,
                    onClick = onOpenBuildingsRooms,
                    modifier = Modifier.weight(1f)
                )
                StatSummaryCard(
                    label = "Assets",
                    value = assetCount.toString(),
                    icon = Icons.Default.Devices,
                    onClick = onOpenAssets,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatSummaryCard(
                    label = "Departments",
                    value = departmentCount.toString(),
                    icon = Icons.Default.AccountTree,
                    onClick = onOpenReports,
                    modifier = Modifier.weight(1f)
                )
                StatSummaryCard(
                    label = "Rooms",
                    value = roomCount.toString(),
                    icon = Icons.Default.MeetingRoom,
                    onClick = onOpenBuildingsRooms,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun DashboardFeatureCard(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(112.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = MaroonPrimary,
                modifier = Modifier.size(34.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
private fun StatSummaryCard(
    label: String,
    value: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = MaroonPrimary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = label,
                    fontSize = 11.sp,
                    color = TextMuted
                )
                Text(
                    text = value,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}