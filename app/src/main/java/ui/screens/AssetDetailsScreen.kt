package ui.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.DevicesOther
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Tag
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private val AssetDetailsCrimson = Color(0xFF7A0C1E)
private val AssetDetailsBg = Color(0xFFF6F6F8)
private val StatusGreenBg = Color(0xFF1B8A4B)

data class AssetDetailInfo(
    val serialName: String = "Equipment",
    @param:DrawableRes val imageRes: Int? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssetDetailsScreen(
    asset: SampleCampusAsset,
    detailInfo: AssetDetailInfo = AssetDetailInfo(
        serialName = asset.categoryName,
        imageRes = null
    ),
    onBackClick: () -> Unit = {},
    onConfirmClick: (SampleCampusAsset) -> Unit = {}
) {
    val conditionLower = asset.currentCondition.lowercase()
    val badgeColor = when {
        conditionLower.contains("good") || conditionLower.contains("active") -> StatusGreenBg
        conditionLower.contains("fair") || conditionLower.contains("maintenance") -> Color(0xFFD97706)
        else -> AssetDetailsCrimson
    }

    Scaffold(
        containerColor = AssetDetailsBg,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Asset Details",
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
                    IconButton(onClick = { onConfirmClick(asset) }) {
                        Icon(
                            imageVector = Icons.Default.CheckBox,
                            contentDescription = "Confirm Asset",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AssetDetailsCrimson
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        if (detailInfo.imageRes != null) {
                            Image(
                                painter = painterResource(id = detailInfo.imageRes),
                                contentDescription = asset.itemName,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(175.dp)
                                    .clip(RoundedCornerShape(12.dp))
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(155.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFF0F2F5)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DevicesOther,
                                    contentDescription = asset.itemName,
                                    tint = Color(0xFF6B7280),
                                    modifier = Modifier.size(64.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = asset.itemName,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF14171A),
                                modifier = Modifier.weight(1f)
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Surface(
                                shape = RoundedCornerShape(50),
                                color = badgeColor
                            ) {
                                Text(
                                    text = asset.currentCondition,
                                    color = Color.White,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        HorizontalDivider(color = Color(0xFFECEEF2))
                        Spacer(modifier = Modifier.height(10.dp))

                        AssetSpecRow(
                            icon = Icons.Default.Tag,
                            label = "Property No.",
                            value = asset.propertyNo
                        )
                        AssetSpecRow(
                            icon = Icons.Default.DevicesOther,
                            label = "Serial Name",
                            value = detailInfo.serialName
                        )
                        AssetSpecRow(
                            icon = Icons.Default.Badge,
                            label = "Brand Model",
                            value = asset.brandModel
                        )
                        AssetSpecRow(
                            icon = Icons.Default.QrCode2,
                            label = "Serial No.",
                            value = asset.serialNo
                        )
                        AssetSpecRow(
                            icon = Icons.Default.CalendarToday,
                            label = "Date Acquired",
                            value = asset.dateAcquired
                        )
                        AssetSpecRow(
                            icon = Icons.Default.Category,
                            label = "Category",
                            value = asset.categoryName
                        )
                        AssetSpecRow(
                            icon = Icons.Default.Apartment,
                            label = "Department",
                            value = asset.departmentName
                        )
                        AssetSpecRow(
                            icon = Icons.Default.Apartment,
                            label = "Building",
                            value = asset.buildingName
                        )
                        AssetSpecRow(
                            icon = Icons.Default.MeetingRoom,
                            label = "Room",
                            value = asset.roomNumber
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AssetSpecRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color(0xFF6B7280),
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF6B7280),
            modifier = Modifier.width(120.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF14171A),
            modifier = Modifier.weight(1f)
        )
    }
}