# ARASOF AIMS - System Source Code

This repository contains the full source code implementation for the **ARASOF Asset Inventory and Mapping System (ARASOF AIMS)** built with Kotlin, Android Jetpack Compose, Room SQLite, and MVVM Architecture.

---

## 1. Data Models (`Entities.kt`)

```kotlin
package com.arasof.aims.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

abstract class BaseEntity(
    open val id: String
) {
    abstract fun getInfo(): String
}

interface Identifiable {
    fun update(): Boolean
}

@Entity(
    tableName = "campus_asset",
    foreignKeys = [
        ForeignKey(entity = FacilityRoom::class, parentColumns = ["roomId"], childColumns = ["roomId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = AssetCategory::class, parentColumns = ["categoryId"], childColumns = ["categoryId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = CollegeDepartment::class, parentColumns = ["departmentId"], childColumns = ["departmentId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index("roomId"), Index("categoryId"), Index("departmentId")]
)
data class CampusAsset(
    @PrimaryKey override val id: String,
    val propertyNo: String,
    val itemName: String,
    val brandModel: String,
    val serialNo: String,
    val currentCondition: String,
    val categoryId: String,
    val roomId: String,
    val departmentId: String,
    val dateAcquired: String
) : BaseEntity(id), Identifiable {
    override fun getInfo(): String = "Asset [$propertyNo]: $itemName - Condition:$currentCondition"
    override fun update(): Boolean = true
}

@Entity(tableName = "campus_building")
data class CampusBuilding(
    @PrimaryKey override val id: String,
    val buildingId: String,
    val buildingName: String,
    val buildingCode: String,
    val totalFloors: Int,
    val gisId: String?
) : BaseEntity(id), Identifiable {
    override fun getInfo(): String = "Building [$buildingCode]: $buildingName ($totalFloors Floors)"
    override fun update(): Boolean = true
}

@Entity(
    tableName = "facility_room",
    foreignKeys = [ForeignKey(entity = CampusBuilding::class, parentColumns = ["buildingId"], childColumns = ["buildingId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("buildingId")]
)
data class FacilityRoom(
    @PrimaryKey override val id: String,
    val roomId: String,
    val roomNumber: String,
    val roomType: String,
    val floorLevel: Int,
    val buildingId: String
) : BaseEntity(id), Identifiable {
    override fun getInfo(): String = "Room $roomNumber ($roomType) - Floor$floorLevel"
    override fun update(): Boolean = true
}

@Entity(tableName = "asset_category")
data class AssetCategory(
    @PrimaryKey override val id: String,
    val categoryId: String,
    val categoryName: String,
    val description: String?
) : BaseEntity(id), Identifiable {
    override fun getInfo(): String = "Category: $categoryName"
    override fun update(): Boolean = true
}

@Entity(tableName = "college_department")
data class CollegeDepartment(
    @PrimaryKey override val id: String,
    val departmentId: String,
    val departmentName: String,
    val departmentCode: String,
    val departmentHead: String
) : BaseEntity(id), Identifiable {
    override fun getInfo(): String = "Department: $departmentName ($departmentCode)"
    override fun update(): Boolean = true
}

@Entity(tableName = "gis_coordinate")
data class GISCoordinate(
    @PrimaryKey override val id: String,
    val gisId: String,
    val latitude: Double,
    val longitude: Double,
    val mapPinLabel: String,
    val zoneArea: String
) : BaseEntity(id), Identifiable {
    override fun getInfo(): String = "GIS Pin [$mapPinLabel]: ($latitude,$longitude)"
    override fun update(): Boolean = true
}