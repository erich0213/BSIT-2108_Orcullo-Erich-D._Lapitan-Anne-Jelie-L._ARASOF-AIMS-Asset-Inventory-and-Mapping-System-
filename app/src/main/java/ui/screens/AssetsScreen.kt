package ui.screens

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.LaptopMac
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class SampleCampusAsset(
    val propertyNo: String,
    val itemName: String,
    val brandModel: String,
    val serialNo: String,
    val categoryName: String,
    val departmentName: String,
    val buildingName: String,
    val roomNumber: String,
    val currentCondition: String,
    val dateAcquired: String
)

private val ArasofCrimson = Color(0xFF7A0C1E)
private val SoftScreenBg = Color(0xFFF6F6F8)

val sampleCampusAssets = listOf(
    SampleCampusAsset(
        propertyNo = "PROP-001",
        itemName = "Computer - 001",
        brandModel = "Dell OptiPlex",
        serialNo = "20680022",
        categoryName = "Computer",
        departmentName = "IT Department",
        buildingName = "Main Building",
        roomNumber = "Room 201",
        currentCondition = "Good",
        dateAcquired = "2025-08-10"
    ),
    SampleCampusAsset(
        propertyNo = "PROP-015",
        itemName = "Printer - 015",
        brandModel = "Epson EcoTank",
        serialNo = "17766601",
        categoryName = "Printer",
        departmentName = "Admin Office",
        buildingName = "Main Building",
        roomNumber = "Room 104",
        currentCondition = "Good",
        dateAcquired = "2025-07-18"
    ),
    SampleCampusAsset(
        propertyNo = "PROP-222",
        itemName = "Printer - 222",
        brandModel = "HP LaserJet",
        serialNo = "111628206",
        categoryName = "Printer",
        departmentName = "Office Basement",
        buildingName = "Science Building",
        roomNumber = "Basement",
        currentCondition = "Fair",
        dateAcquired = "2024-11-05"
    ),
    SampleCampusAsset(
        propertyNo = "PROP-023",
        itemName = "Laptop - 023",
        brandModel = "Acer Aspire 5",
        serialNo = "11085073",
        categoryName = "Laptop",
        departmentName = "IT Department",
        buildingName = "Main Building",
        roomNumber = "Room 203",
        currentCondition = "Good",
        dateAcquired = "2025-08-10"
    ),
    SampleCampusAsset(
        propertyNo = "PROP-022",
        itemName = "Projector - 022",
        brandModel = "Epson EB-X06",
        serialNo = "19050023",
        categoryName = "Projector",
        departmentName = "Lecture Hall",
        buildingName = "Main Building",
        roomNumber = "Lecture Hall A",
        currentCondition = "Good",
        dateAcquired = "2025-06-22"
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssetsScreen(
    onBackClick: () -> Unit = {},
    onAddAssetClick: () -> Unit = {},
    onAssetClick: (SampleCampusAsset) -> Unit = {},
    assets: List<SampleCampusAsset> = sampleCampusAssets
) {
    var searchQuery by remember { mutableStateOf("") }
    var showSearchBar by remember { mutableStateOf(true) }
    var selectedCategory by remember { mutableStateOf("All Categories") }
    var selectedBuilding by remember { mutableStateOf("All Buildings") }
    var categoryMenuExpanded by remember { mutableStateOf(false) }
    var buildingMenuExpanded by remember { mutableStateOf(false) }

    val categories = remember(assets) {
        listOf("All Categories") + assets.map { it.categoryName }.distinct()
    }
    val buildings = remember(assets) {
        listOf("All Buildings") + assets.map { it.buildingName }.distinct()
    }

    val filteredAssets = remember(assets, searchQuery, selectedCategory, selectedBuilding) {
        assets.filter { asset ->
            val matchesCategory = selectedCategory == "All Categories" || asset.categoryName == selectedCategory
            val matchesBuilding = selectedBuilding == "All Buildings" || asset.buildingName == selectedBuilding
            val query = searchQuery.trim()
            val matchesQuery = query.isEmpty() ||
                    asset.itemName.contains(query, ignoreCase = true) ||
                    asset.brandModel.contains(query, ignoreCase = true) ||
                    asset.serialNo.contains(query, ignoreCase = true) ||
                    asset.departmentName.contains(query, ignoreCase = true) ||
                    asset.buildingName.contains(query, ignoreCase = true) ||
                    asset.propertyNo.contains(query, ignoreCase = true)

            matchesCategory && matchesBuilding && matchesQuery
        }
    }

    Scaffold(
        containerColor = SoftScreenBg,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Assets",
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
                actions = {
                    IconButton(onClick = { showSearchBar = !showSearchBar }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Toggle Search",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = onAddAssetClick) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Asset",
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
            if (showSearchBar) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    placeholder = { Text("Search asset...") },
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
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    OutlinedButton(
                        onClick = { categoryMenuExpanded = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = selectedCategory,
                            maxLines = 1,
                            color = Color(0xFF1F2328)
                        )
                    }
                    DropdownMenu(
                        expanded = categoryMenuExpanded,
                        onDismissRequest = { categoryMenuExpanded = false }
                    ) {
                        categories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category) },
                                onClick = {
                                    selectedCategory = category
                                    categoryMenuExpanded = false
                                }
                            )
                        }
                    }
                }

                Box(modifier = Modifier.weight(1f)) {
                    OutlinedButton(
                        onClick = { buildingMenuExpanded = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = selectedBuilding,
                            maxLines = 1,
                            color = Color(0xFF1F2328)
                        )
                    }
                    DropdownMenu(
                        expanded = buildingMenuExpanded,
                        onDismissRequest = { buildingMenuExpanded = false }
                    ) {
                        buildings.forEach { building ->
                            DropdownMenuItem(
                                text = { Text(building) },
                                onClick = {
                                    selectedBuilding = building
                                    buildingMenuExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredAssets, key = { it.propertyNo }) { asset ->
                    AssetListCard(
                        asset = asset,
                        onClick = { onAssetClick(asset) }
                    )
                }
            }
        }
    }
}

@Composable
private fun AssetListCard(
    asset: SampleCampusAsset,
    onClick: () -> Unit
) {
    val isGood = asset.currentCondition.equals("Good", ignoreCase = true)
    val badgeBg = if (isGood) Color(0xFFE6F4EA) else Color(0xFFFEF3E2)
    val badgeFg = if (isGood) Color(0xFF1E7E34) else Color(0xFFB26A00)

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
                    .size(56.dp)
                    .background(Color(0xFFF1F3F5), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = assetIconForCategory(asset.categoryName),
                    contentDescription = asset.categoryName,
                    tint = Color(0xFF2B2F36),
                    modifier = Modifier.size(30.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = asset.itemName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF181A1F)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = asset.departmentName,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF5A606A)
                )
                Text(
                    text = asset.brandModel,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF5A606A)
                )
                Text(
                    text = "Serial No: ${asset.serialNo}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF5A606A)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Surface(
                    color = badgeBg,
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = asset.currentCondition,
                        color = badgeFg,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                    )
                }
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "View Details",
                tint = Color(0xFF8C939D)
            )
        }
    }
}

private fun assetIconForCategory(categoryName: String): ImageVector {
    return when (categoryName.lowercase()) {
        "computer" -> Icons.Default.Computer
        "laptop" -> Icons.Default.LaptopMac
        "printer" -> Icons.Default.Print
        "projector" -> Icons.Default.Videocam
        else -> Icons.Default.Computer
    }
}