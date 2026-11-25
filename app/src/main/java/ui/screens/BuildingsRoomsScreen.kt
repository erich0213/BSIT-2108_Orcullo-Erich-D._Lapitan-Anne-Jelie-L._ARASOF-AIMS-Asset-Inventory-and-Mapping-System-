package ui.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arasof.aims.R

data class CampusBuildingItem(
    val id: String,
    val name: String,
    val roomCount: Int,
    val assetCount: Int,
    @param:DrawableRes val imageRes: Int,
    val rooms: List<CampusRoomItem> = emptyList()
)

data class CampusRoomItem(
    val id: String,
    val roomName: String,
    val buildingName: String,
    val assetCount: Int
)

private val ArasofCrimson = Color(0xFF7A0C1E)
private val SoftScreenBg = Color(0xFFF6F6F8)

val sampleCampusBuildings = listOf(
    CampusBuildingItem(
        id = "ibaan",
        name = "IBAAN BUILDING",
        roomCount = 12,
        assetCount = 84,
        imageRes = R.drawable.bldg_ibaan,
        rooms = listOf(
            CampusRoomItem("ib-101", "Room 101", "IBAAN BUILDING", 24),
            CampusRoomItem("ib-102", "Room 102", "IBAAN BUILDING", 30),
            CampusRoomItem("ib-201", "Computer Lab 201", "IBAAN BUILDING", 30)
        )
    ),
    CampusBuildingItem(
        id = "heb_san_juan",
        name = "HEB BUILDING (SAN JUAN BUILDING)",
        roomCount = 10,
        assetCount = 65,
        imageRes = R.drawable.bldg_heb_san_juan,
        rooms = listOf(
            CampusRoomItem("heb-101", "Room 101", "HEB BUILDING (SAN JUAN BUILDING)", 20),
            CampusRoomItem("heb-102", "Room 102", "HEB BUILDING (SAN JUAN BUILDING)", 22),
            CampusRoomItem("heb-201", "Faculty Room 201", "HEB BUILDING (SAN JUAN BUILDING)", 23)
        )
    ),
    CampusBuildingItem(
        id = "gate_1",
        name = "Gate 1 or Main entrance",
        roomCount = 2,
        assetCount = 12,
        imageRes = R.drawable.bldg_gate_1,
        rooms = listOf(
            CampusRoomItem("g1-guard", "Security Guard Post", "Gate 1 or Main entrance", 7),
            CampusRoomItem("g1-visitor", "Visitor Reception Desk", "Gate 1 or Main entrance", 5)
        )
    ),
    CampusBuildingItem(
        id = "hostel",
        name = "HOSTEL",
        roomCount = 14,
        assetCount = 92,
        imageRes = R.drawable.bldg_hostel,
        rooms = listOf(
            CampusRoomItem("hst-101", "Guest Room 101", "HOSTEL", 30),
            CampusRoomItem("hst-102", "Guest Room 102", "HOSTEL", 32),
            CampusRoomItem("hst-lounge", "Hostel Lounge", "HOSTEL", 30)
        )
    ),
    CampusBuildingItem(
        id = "infirmary",
        name = "INFIRMARY BLDG. / Clinic",
        roomCount = 5,
        assetCount = 38,
        imageRes = R.drawable.bldg_infirmary,
        rooms = listOf(
            CampusRoomItem("inf-outpatient", "Outpatient Consultation", "INFIRMARY BLDG. / Clinic", 16),
            CampusRoomItem("inf-emergency", "Emergency Room", "INFIRMARY BLDG. / Clinic", 22)
        )
    ),
    CampusBuildingItem(
        id = "cteb",
        name = "CTEB BUILDING",
        roomCount = 11,
        assetCount = 74,
        imageRes = R.drawable.bldg_cteb,
        rooms = listOf(
            CampusRoomItem("cteb-101", "Lecture Room 101", "CTEB BUILDING", 24),
            CampusRoomItem("cteb-102", "Lecture Room 102", "CTEB BUILDING", 25),
            CampusRoomItem("cteb-201", "Simulation Room 201", "CTEB BUILDING", 25)
        )
    )
)

private enum class FacilitiesTab {
    BUILDINGS,
    ROOMS
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuildingsRoomsScreen(
    onBackClick: () -> Unit = {},
    onBuildingClick: (CampusBuildingItem) -> Unit = {},
    onRoomClick: (CampusRoomItem) -> Unit = {},
    buildings: List<CampusBuildingItem> = sampleCampusBuildings
) {
    var selectedTab by remember { mutableStateOf(FacilitiesTab.BUILDINGS) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedBuildingFilter by remember { mutableStateOf<CampusBuildingItem?>(null) }

    val allRooms = remember(buildings) {
        buildings.flatMap { building ->
            if (building.rooms.isNotEmpty()) {
                building.rooms
            } else {
                listOf(
                    CampusRoomItem(
                        id = "${building.id}_main",
                        roomName = "Main Room",
                        buildingName = building.name,
                        assetCount = building.assetCount
                    )
                )
            }
        }
    }

    val filteredBuildings = remember(buildings, searchQuery) {
        val query = searchQuery.trim()
        if (query.isEmpty()) {
            buildings
        } else {
            buildings.filter { it.name.contains(query, ignoreCase = true) }
        }
    }

    val filteredRooms = remember(allRooms, searchQuery, selectedBuildingFilter) {
        val query = searchQuery.trim()
        allRooms.filter { room ->
            val matchesBuilding = selectedBuildingFilter == null || room.buildingName == selectedBuildingFilter?.name
            val matchesQuery = query.isEmpty() ||
                    room.roomName.contains(query, ignoreCase = true) ||
                    room.buildingName.contains(query, ignoreCase = true)
            matchesBuilding && matchesQuery
        }
    }

    Scaffold(
        containerColor = SoftScreenBg,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Buildings & Rooms",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ArasofCrimson
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Segmented Toggle: Buildings / Rooms
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFE9EBEF)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val isBuildings = selectedTab == FacilitiesTab.BUILDINGS
                    val isRooms = selectedTab == FacilitiesTab.ROOMS

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(9.dp))
                            .background(if (isBuildings) ArasofCrimson else Color.Transparent)
                            .clickable {
                                selectedTab = FacilitiesTab.BUILDINGS
                                selectedBuildingFilter = null
                            }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Buildings",
                            color = if (isBuildings) Color.White else Color(0xFF4B525C),
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(9.dp))
                            .background(if (isRooms) ArasofCrimson else Color.Transparent)
                            .clickable { selectedTab = FacilitiesTab.ROOMS }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Rooms",
                            color = if (isRooms) Color.White else Color(0xFF4B525C),
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            // Search Input
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                placeholder = {
                    Text(
                        text = if (selectedTab == FacilitiesTab.BUILDINGS) {
                            "Search building..."
                        } else {
                            "Search room or building..."
                        }
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear search"
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = ArasofCrimson
                )
            )

            if (selectedTab == FacilitiesTab.ROOMS && selectedBuildingFilter != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Building: ${selectedBuildingFilter?.name}",
                        style = MaterialTheme.typography.labelLarge,
                        color = ArasofCrimson,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Show All",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color(0xFF4B525C),
                        modifier = Modifier.clickable { selectedBuildingFilter = null }
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(10.dp))
            }

            if (selectedTab == FacilitiesTab.BUILDINGS) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredBuildings, key = { it.id }) { building ->
                        BuildingListCard(
                            building = building,
                            onClick = {
                                selectedBuildingFilter = building
                                selectedTab = FacilitiesTab.ROOMS
                                onBuildingClick(building)
                            }
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredRooms, key = { it.id }) { room ->
                        RoomListCard(
                            room = room,
                            onClick = { onRoomClick(room) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BuildingListCard(
    building: CampusBuildingItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = building.imageRes),
                contentDescription = building.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(width = 72.dp, height = 56.dp)
                    .clip(RoundedCornerShape(10.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = building.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF181A1F)
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "${building.roomCount} rooms • ${building.assetCount} assets",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF5A606A)
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "View Rooms",
                tint = Color(0xFF8C939D)
            )
        }
    }
}

@Composable
private fun RoomListCard(
    room: CampusRoomItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
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
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color(0xFFF1F3F5), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.MeetingRoom,
                    contentDescription = room.roomName,
                    tint = ArasofCrimson,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = room.roomName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF181A1F)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${room.buildingName} • ${room.assetCount} assets",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF5A606A)
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "View Room",
                tint = Color(0xFF8C939D)
            )
        }
    }
}