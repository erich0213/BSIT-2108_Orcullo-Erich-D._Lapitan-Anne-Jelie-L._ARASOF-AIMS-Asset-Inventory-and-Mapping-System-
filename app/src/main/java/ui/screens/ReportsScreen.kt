package ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private val ReportsCrimson = Color(0xFF7A0C1E)
private val ReportsBg = Color(0xFFF6F6F8)
private val ReportIconBoxBg = Color(0xFFFBECE3)
private val ReportIconTint = Color(0xFFC2591B)

private data class ReportSection(
    val title: String,
    val assets: List<SampleCampusAsset>
)

enum class ReportType(
    val title: String,
    val subtitle: String,
    val icon: ImageVector
) {
    ASSET_LIST(
        title = "Asset List Report",
        subtitle = "List of all campus assets",
        icon = Icons.Default.Description
    ),
    BY_DEPARTMENT(
        title = "By Department",
        subtitle = "Assets grouped by department",
        icon = Icons.Default.Apartment
    ),
    BY_BUILDING(
        title = "By Building",
        subtitle = "Assets per building",
        icon = Icons.Default.Apartment
    ),
    BY_CATEGORY(
        title = "By Category",
        subtitle = "Assets per category",
        icon = Icons.Default.Category
    ),
    BY_CONDITION(
        title = "By Condition",
        subtitle = "Assets by current condition",
        icon = Icons.Default.Shield
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(
    assets: List<SampleCampusAsset> = sampleCampusAssets,
    onBackClick: () -> Unit = {},
    onGenerateReport: (ReportType, List<SampleCampusAsset>) -> Unit = { _, _ -> },
    onSelectTab: (AppTab) -> Unit = {}
) {
    var selectedType by remember { mutableStateOf(ReportType.ASSET_LIST) }
    var generatedSections by remember { mutableStateOf(emptyList<ReportSection>()) }
    var showReportDialog by remember { mutableStateOf(false) }

    val reportOptions = remember { ReportType.entries }

    Scaffold(
        containerColor = ReportsBg,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Reports",
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
                    containerColor = ReportsCrimson
                )
            )
        },
        bottomBar = {
            Surface(
                color = Color.White,
                shadowElevation = 6.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ReportBottomItem(
                        label = "Home",
                        icon = Icons.Default.Home,
                        selected = false,
                        onClick = { onSelectTab(AppTab.HOME) }
                    )
                    ReportBottomItem(
                        label = "Map",
                        icon = Icons.Default.LocationOn,
                        selected = false,
                        onClick = { onSelectTab(AppTab.MAP) }
                    )
                    ReportBottomItem(
                        label = "Assets",
                        icon = Icons.Default.Inventory2,
                        selected = false,
                        onClick = { onSelectTab(AppTab.ASSETS) }
                    )
                    ReportBottomItem(
                        label = "More",
                        icon = Icons.Default.MoreVert,
                        selected = false,
                        onClick = { onSelectTab(AppTab.MORE) }
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(reportOptions, key = { it.name }) { option ->
                    val isSelected = selectedType == option
                    ReportOptionCard(
                        reportType = option,
                        isSelected = isSelected,
                        onClick = { selectedType = option }
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ReportsBg)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Button(
                    onClick = {
                        generatedSections = buildReportSections(selectedType, assets)
                        showReportDialog = true
                        onGenerateReport(selectedType, assets)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ReportsCrimson,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Generate Report",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }

    if (showReportDialog) {
        AlertDialog(
            onDismissRequest = { showReportDialog = false },
            title = {
                Text(
                    text = selectedType.title,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 360.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Total Assets: ${assets.size}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = ReportsCrimson
                    )
                    HorizontalDivider()

                    generatedSections.forEach { section ->
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = section.title,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF181A1F),
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = "${section.assets.size}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = ReportsCrimson
                                )
                            }

                            section.assets.forEach { asset ->
                                Text(
                                    text = "• ${asset.itemName} (${asset.propertyNo}) — ${asset.buildingName}, ${asset.roomNumber}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF4B5563)
                                )
                            }
                        }
                        HorizontalDivider(color = Color(0xFFECEEF2))
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showReportDialog = false }) {
                    Text("Close", color = ReportsCrimson, fontWeight = FontWeight.SemiBold)
                }
            }
        )
    }
}

@Composable
private fun ReportOptionCard(
    reportType: ReportType,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        border = if (isSelected) {
            BorderStroke(1.5.dp, ReportsCrimson)
        } else {
            BorderStroke(1.dp, Color(0xFFECEEF2))
        },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFFFFF8F9) else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(ReportIconBoxBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = reportType.icon,
                    contentDescription = reportType.title,
                    tint = ReportIconTint,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = reportType.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF181A1F)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = reportType.subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF6B7280)
                )
            }
        }
    }
}

@Composable
private fun ReportBottomItem(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    val itemColor = if (selected) ReportsCrimson else Color(0xFF5A606A)
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = itemColor,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = itemColor,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

private fun buildReportSections(
    type: ReportType,
    assets: List<SampleCampusAsset>
): List<ReportSection> {
    if (assets.isEmpty()) {
        return listOf(ReportSection(title = "No Assets Recorded", assets = emptyList()))
    }

    return when (type) {
        ReportType.ASSET_LIST -> listOf(
            ReportSection(title = "All Recorded Assets", assets = assets)
        )
        ReportType.BY_DEPARTMENT -> assets
            .groupBy { it.departmentName }
            .map { (dept, items) -> ReportSection(title = dept, assets = items) }
        ReportType.BY_BUILDING -> assets
            .groupBy { it.buildingName }
            .map { (building, items) -> ReportSection(title = building, assets = items) }
        ReportType.BY_CATEGORY -> assets
            .groupBy { it.categoryName }
            .map { (category, items) -> ReportSection(title = category, assets = items) }
        ReportType.BY_CONDITION -> assets
            .groupBy { it.currentCondition }
            .map { (condition, items) -> ReportSection(title = condition, assets = items) }
    }
}