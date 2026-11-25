package ui.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arasof.aims.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

private val MaroonPrimary = Color(0xFF7A0019)
private val SurfaceLight = Color(0xFFF8F9FA)
private val TextMuted = Color(0xFF6C757D)
private val TextDark = Color(0xFF212529)

// BSU ARASOF-Nasugbu — R. Martinez Street, Barangay Bucana, Nasugbu, Batangas
private val ArasofCampusCenter = LatLng(14.06724, 120.62672)
private const val DefaultCampusZoom = 18.0f

private data class CampusBuildingPin(
    val id: String,
    val title: String,
    val subtitle: String,
    val rooms: List<String>,
    @DrawableRes val photoRes: Int?,
    val pinColor: Color,
    val markerHue: Float,
    val position: LatLng
)

// Palitan o dagdagan ang coordinates/rooms kung may eksaktong GPS points ka para sa bawat building
private val campusPins = listOf(
    CampusBuildingPin(
        id = "heb_san_juan",
        title = "HEB / San Juan Building",
        subtitle = "Admin, Academic Offices & Rooms",
        rooms = listOf("HEB 101", "HEB 102", "Admin Office"),
        photoRes = R.drawable.bldg_heb_san_juan,
        pinColor = Color(0xFF8E24AA),
        markerHue = BitmapDescriptorFactory.HUE_VIOLET,
        position = LatLng(14.06724, 120.62672)
    ),
    CampusBuildingPin(
        id = "cteb_bldg",
        title = "CTEB Building",
        subtitle = "Teacher Education Rooms & Labs",
        rooms = listOf("CTEB 201", "CTEB 202", "Faculty Room"),
        photoRes = R.drawable.bldg_cteb,
        pinColor = Color(0xFF1E88E5),
        markerHue = BitmapDescriptorFactory.HUE_AZURE,
        position = LatLng(14.06745, 120.62655)
    ),
    CampusBuildingPin(
        id = "ibaan_bldg",
        title = "Ibaan Building",
        subtitle = "Lecture Rooms & Classrooms",
        rooms = listOf("Ibaan 101", "Ibaan 102", "Ibaan 201"),
        photoRes = R.drawable.bldg_ibaan,
        pinColor = Color(0xFF2E7D32),
        markerHue = BitmapDescriptorFactory.HUE_GREEN,
        position = LatLng(14.06738, 120.62695)
    ),
    CampusBuildingPin(
        id = "infirmary_bldg",
        title = "Campus Infirmary",
        subtitle = "Health & Medical Rooms",
        rooms = listOf("Clinic Room 1", "Consultation Room"),
        photoRes = R.drawable.bldg_infirmary,
        pinColor = Color(0xFFD32F2F),
        markerHue = BitmapDescriptorFactory.HUE_RED,
        position = LatLng(14.06705, 120.62650)
    ),
    CampusBuildingPin(
        id = "hostel_bldg",
        title = "ARASOF Hostel",
        subtitle = "Accommodation & Function Rooms",
        rooms = listOf("Room A", "Room B", "Function Hall"),
        photoRes = R.drawable.bldg_hostel,
        pinColor = Color(0xFF8E24AA),
        markerHue = BitmapDescriptorFactory.HUE_MAGENTA,
        position = LatLng(14.06695, 120.62685)
    ),
    CampusBuildingPin(
        id = "main_gate_1",
        title = "Main Gate (R. Martinez St.)",
        subtitle = "BSU ARASOF-Nasugbu Entrance",
        rooms = listOf("Guard House", "Visitor Desk"),
        photoRes = R.drawable.bldg_gate_1,
        pinColor = Color(0xFFEF6C00),
        markerHue = BitmapDescriptorFactory.HUE_ORANGE,
        position = LatLng(14.06712, 120.62628)
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    onBackToDashboard: () -> Unit,
    onSelectTab: (AppTab) -> Unit,
    onOpenBuildingsRooms: () -> Unit = {},
    onOpenBuildingDetail: (String) -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()
    var searchQuery by remember { mutableStateOf("") }
    var selectedPin by remember { mutableStateOf(campusPins.first()) }
    var currentZoom by remember { mutableFloatStateOf(DefaultCampusZoom) }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(ArasofCampusCenter, DefaultCampusZoom)
    }

    fun matchesQuery(pin: CampusBuildingPin, query: String): Boolean {
        if (query.isBlank()) return true
        return pin.title.contains(query, ignoreCase = true) ||
                pin.subtitle.contains(query, ignoreCase = true) ||
                pin.rooms.any { it.contains(query, ignoreCase = true) }
    }

    val matchingPins = campusPins.filter { matchesQuery(it, searchQuery) }

    fun focusOn(target: LatLng, zoom: Float = currentZoom) {
        currentZoom = zoom
        coroutineScope.launch {
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(target, zoom),
                durationMs = 500
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Campus Map",
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
                    IconButton(onClick = {
                        if (matchingPins.isNotEmpty()) {
                            val firstMatch = matchingPins.first()
                            selectedPin = firstMatch
                            focusOn(firstMatch.position)
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Focus Search",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaroonPrimary)
            )
        },
        bottomBar = {
            ArasofBottomBar(
                currentTab = AppTab.MAP,
                onSelectTab = onSelectTab
            )
        },
        containerColor = SurfaceLight
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Search input bar below top bar (supports building, subtitle, or room search)
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { query ->
                    searchQuery = query
                    campusPins.firstOrNull { matchesQuery(it, query) }?.let { matched ->
                        selectedPin = matched
                        focusOn(matched.position)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceLight)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                singleLine = true,
                placeholder = {
                    Text(
                        text = "Search building, room, or asset...",
                        fontSize = 13.sp,
                        color = TextMuted
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = TextMuted
                    )
                },
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = MaroonPrimary,
                    unfocusedBorderColor = Color(0xFFE0E0E0)
                )
            )

            // Google Map + floating card + controls
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
            ) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    uiSettings = MapUiSettings(
                        zoomControlsEnabled = false,
                        myLocationButtonEnabled = false
                    )
                ) {
                    matchingPins.forEach { pin ->
                        val isSelected = pin.id == selectedPin.id
                        Marker(
                            state = MarkerState(position = pin.position),
                            title = pin.title,
                            snippet = pin.subtitle,
                            alpha = if (isSelected) 1.0f else 0.85f,
                            icon = BitmapDescriptorFactory.defaultMarker(pin.markerHue),
                            onClick = {
                                selectedPin = pin
                                focusOn(pin.position)
                                false
                            }
                        )
                    }
                }

                // Floating selected building card with actual building photo + name + rooms preview
                Card(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .clickable {
                            onOpenBuildingDetail(selectedPin.id)
                            onOpenBuildingsRooms()
                        },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(width = 68.dp, height = 48.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaroonPrimary.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            if (selectedPin.photoRes != null) {
                                Image(
                                    painter = painterResource(id = selectedPin.photoRes!!),
                                    contentDescription = selectedPin.title,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Apartment,
                                    contentDescription = null,
                                    tint = MaroonPrimary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = selectedPin.title,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextDark
                            )
                            Text(
                                text = selectedPin.subtitle,
                                fontSize = 11.sp,
                                color = TextMuted
                            )
                            if (selectedPin.rooms.isNotEmpty()) {
                                Text(
                                    text = "Rooms: ${selectedPin.rooms.joinToString(", ")}",
                                    fontSize = 10.sp,
                                    color = MaroonPrimary,
                                    fontWeight = FontWeight.Medium,
                                    maxLines = 1
                                )
                            }
                        }

                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "Open Buildings & Rooms",
                            tint = TextMuted
                        )
                    }
                }

                // Right-side map controls: reset to BSU ARASOF-Nasugbu + zoom in/out
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 12.dp, bottom = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        modifier = Modifier
                            .size(40.dp)
                            .clickable {
                                val mainPin = campusPins.first()
                                selectedPin = mainPin
                                focusOn(ArasofCampusCenter, DefaultCampusZoom)
                            },
                        shape = RoundedCornerShape(10.dp),
                        color = Color.White,
                        shadowElevation = 4.dp
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.MyLocation,
                                contentDescription = "Reset View",
                                tint = TextDark,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.White)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clickable {
                                    val nextZoom = (cameraPositionState.position.zoom + 0.8f)
                                        .coerceAtMost(21f)
                                    focusOn(cameraPositionState.position.target, nextZoom)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Zoom In",
                                tint = TextDark,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        HorizontalDivider(thickness = 1.dp, color = Color(0xFFE9ECEF))

                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clickable {
                                    val nextZoom = (cameraPositionState.position.zoom - 0.8f)
                                        .coerceAtLeast(13f)
                                    focusOn(cameraPositionState.position.target, nextZoom)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = "Zoom Out",
                                tint = TextDark,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            // Bottom legend row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                MapLegendItem(dotColor = Color(0xFF8E24AA), label = "Buildings")
                MapLegendItem(dotColor = Color(0xFF2E7D32), label = "Rooms")
                MapLegendItem(dotColor = Color(0xFFD32F2F), label = "Assets")
            }
        }
    }
}

@Composable
private fun MapLegendItem(
    dotColor: Color,
    label: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(dotColor)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = TextDark
        )
    }
}