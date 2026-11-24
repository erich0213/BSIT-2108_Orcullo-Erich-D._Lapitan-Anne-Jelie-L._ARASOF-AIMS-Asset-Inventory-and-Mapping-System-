package ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

private val ArasofCrimson = Color(0xFF7A0C1E)
private val SoftScreenBg = Color(0xFFF6F6F8)

private val categoryOptions = listOf("Computer", "Laptop", "Printer", "Projector", "Other")
private val departmentOptions = listOf("IT Department", "Admin Office", "Lecture Hall", "Office Basement")
private val buildingOptions = listOf("Main Building", "Science Building", "Library Building", "Student Center")
private val roomOptions = listOf("Room 104", "Room 201", "Room 203", "Lecture Hall A", "Basement")
private val conditionOptions = listOf("Good", "Fair")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAssetScreen(
    onBackClick: () -> Unit = {},
    onSaveAsset: (SampleCampusAsset) -> Unit = {}
) {
    var propertyNo by remember { mutableStateOf("") }
    var itemName by remember { mutableStateOf("") }
    var brandModel by remember { mutableStateOf("") }
    var serialNo by remember { mutableStateOf("") }
    var dateAcquired by remember { mutableStateOf("") }

    var selectedCategory by remember { mutableStateOf("") }
    var selectedDepartment by remember { mutableStateOf("") }
    var selectedBuilding by remember { mutableStateOf("") }
    var selectedRoom by remember { mutableStateOf("") }
    var selectedCondition by remember { mutableStateOf("") }

    var categoryExpanded by remember { mutableStateOf(false) }
    var departmentExpanded by remember { mutableStateOf(false) }
    var buildingExpanded by remember { mutableStateOf(false) }
    var roomExpanded by remember { mutableStateOf(false) }
    var conditionExpanded by remember { mutableStateOf(false) }

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    var errorMessage by remember { mutableStateOf<String?>(null) }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val millis = datePickerState.selectedDateMillis
                        if (millis != null) {
                            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).apply {
                                timeZone = TimeZone.getTimeZone("UTC")
                            }
                            dateAcquired = formatter.format(Date(millis))
                            errorMessage = null
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("OK", color = ArasofCrimson)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel", color = ArasofCrimson)
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Scaffold(
        containerColor = SoftScreenBg,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Add Asset",
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
                .verticalScroll(rememberScrollState())
                .padding(PaddingValues(horizontal = 16.dp, vertical = 14.dp)),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            RequiredTextField(
                label = "Property No.*",
                placeholder = "Enter property number",
                value = propertyNo,
                onValueChange = {
                    propertyNo = it
                    errorMessage = null
                }
            )

            RequiredTextField(
                label = "Item Name*",
                placeholder = "Enter item name",
                value = itemName,
                onValueChange = {
                    itemName = it
                    errorMessage = null
                }
            )

            RequiredTextField(
                label = "Brand Model*",
                placeholder = "Enter brand and model",
                value = brandModel,
                onValueChange = {
                    brandModel = it
                    errorMessage = null
                }
            )

            RequiredTextField(
                label = "Serial No.*",
                placeholder = "Enter serial number",
                value = serialNo,
                onValueChange = {
                    serialNo = it
                    errorMessage = null
                }
            )

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Date Acquired*",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1F2328)
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = dateAcquired,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Select date") },
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Select date"
                            )
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = ArasofCrimson
                    )
                )
            }

            DropdownField(
                label = "Category*",
                placeholder = "Select category",
                selectedValue = selectedCategory,
                options = categoryOptions,
                expanded = categoryExpanded,
                onExpandedChange = { categoryExpanded = it },
                onSelectOption = {
                    selectedCategory = it
                    errorMessage = null
                }
            )

            DropdownField(
                label = "Department*",
                placeholder = "Select department",
                selectedValue = selectedDepartment,
                options = departmentOptions,
                expanded = departmentExpanded,
                onExpandedChange = { departmentExpanded = it },
                onSelectOption = {
                    selectedDepartment = it
                    errorMessage = null
                }
            )

            DropdownField(
                label = "Building*",
                placeholder = "Select building",
                selectedValue = selectedBuilding,
                options = buildingOptions,
                expanded = buildingExpanded,
                onExpandedChange = { buildingExpanded = it },
                onSelectOption = {
                    selectedBuilding = it
                    errorMessage = null
                }
            )

            DropdownField(
                label = "Room*",
                placeholder = "Select room",
                selectedValue = selectedRoom,
                options = roomOptions,
                expanded = roomExpanded,
                onExpandedChange = { roomExpanded = it },
                onSelectOption = {
                    selectedRoom = it
                    errorMessage = null
                }
            )

            DropdownField(
                label = "Condition*",
                placeholder = "Select condition",
                selectedValue = selectedCondition,
                options = conditionOptions,
                expanded = conditionExpanded,
                onExpandedChange = { conditionExpanded = it },
                onSelectOption = {
                    selectedCondition = it
                    errorMessage = null
                }
            )

            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = ArasofCrimson,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Button(
                onClick = {
                    if (
                        propertyNo.isBlank() ||
                        itemName.isBlank() ||
                        brandModel.isBlank() ||
                        serialNo.isBlank() ||
                        dateAcquired.isBlank() ||
                        selectedCategory.isBlank() ||
                        selectedDepartment.isBlank() ||
                        selectedBuilding.isBlank() ||
                        selectedRoom.isBlank() ||
                        selectedCondition.isBlank()
                    ) {
                        errorMessage = "Pakipunan lahat ng required fields (*)."
                        return@Button
                    }

                    onSaveAsset(
                        SampleCampusAsset(
                            propertyNo = propertyNo.trim(),
                            itemName = itemName.trim(),
                            brandModel = brandModel.trim(),
                            serialNo = serialNo.trim(),
                            categoryName = selectedCategory,
                            departmentName = selectedDepartment,
                            buildingName = selectedBuilding,
                            roomNumber = selectedRoom,
                            currentCondition = selectedCondition,
                            dateAcquired = dateAcquired.trim()
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ArasofCrimson,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Save Asset",
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun RequiredTextField(
    label: String,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1F2328)
        )
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder) },
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = ArasofCrimson
            )
        )
    }
}

@Composable
private fun DropdownField(
    label: String,
    placeholder: String,
    selectedValue: String,
    options: List<String>,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onSelectOption: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1F2328)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(
                onClick = { onExpandedChange(true) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = if (selectedValue.isEmpty()) placeholder else selectedValue,
                    color = if (selectedValue.isEmpty()) Color(0xFF7A808A) else Color(0xFF1F2328),
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = Color(0xFF5A606A)
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { onExpandedChange(false) }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onSelectOption(option)
                            onExpandedChange(false)
                        }
                    )
                }
            }
        }
    }
}