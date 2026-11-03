interface Identifiable<ID> {
    val id: ID
}

interface Updatable {
    fun update()
}

abstract class BaseEntity : Updatable {
    abstract fun getInfo(): String
    override fun update() {

    }


    class CollegeDepartment(
        override val id: Int,
        var departmentName: String,
        var departmentCode: String,
        var departmentHead: String
    ) : Identifiable<Int> {

        val assets: MutableList<CampusAsset> = mutableListOf()

        fun getDepartmentInfo(): String =
            "$departmentCode - $departmentName (Head: $departmentHead)"

        fun updateDepartment(
            departmentName: String,
            departmentCode: String,
            departmentHead: String
        ) {
            this.departmentName = departmentName
            this.departmentCode = departmentCode
            this.departmentHead = departmentHead
        }
    }

    class CampusAsset(
        var propertyNo: String,
        var itemName: String,
        var brandModel: String,
        var serialNo: String,
        var currentCondition: String,
        var dateAcquired: LocalDate,
        var department: CollegeDepartment,
        var category: AssetCategory,
        var room: FacilityRoom
    ) : BaseEntity() {

        override fun getInfo(): String =
            "$propertyNo - $itemName ($brandModel), condition: $currentCondition"

        fun getAssetDetails(): String =
            "Property No: $propertyNo, Item: $itemName, Brand/Model: $brandModel, " +
                    "Serial No: $serialNo, Condition: $currentCondition, " +
                    "Date Acquired: $dateAcquired"

        fun updateAsset(
            itemName: String,
            brandModel: String,
            serialNo: String,
            currentCondition: String,
            dateAcquired: LocalDate
        ) {
            this.itemName = itemName
            this.brandModel = brandModel
            this.serialNo = serialNo
            this.currentCondition = currentCondition
            this.dateAcquired = dateAcquired
        }

        override fun update() {

        }
    }

    class AssetCategory(
        override val id: Int,
        var categoryName: String,
        var description: String
    ) : Identifiable<Int> {

        val assets: MutableList<CampusAsset> = mutableListOf()

        fun getCategory(): String = categoryName

        fun updateCategory(categoryName: String, description: String) {
            this.categoryName = categoryName
            this.description = description
        }
    }

    class CampusBuilding(
        override val id: Int,
        var buildingName: String,
        var buildingCode: String,
        var totalFloors: Int
    ) : Identifiable<Int> {

        val rooms: MutableList<FacilityRoom> = mutableListOf()

        fun getBuildingInfo(): String =
            "$buildingCode - $buildingName ($totalFloors floors)"

        fun updateBuilding(
            buildingName: String,
            buildingCode: String,
            totalFloors: Int
        ) {
            this.buildingName = buildingName
            this.buildingCode = buildingCode
            this.totalFloors = totalFloors
        }
    }

    class FacilityRoom(
        override val id: Int,
        var roomNumber: String,
        var roomType: String,
        var floorLevel: Int,
        var building: CampusBuilding,
        var gisCoordinate: GISCoordinate
    ) : Identifiable<Int> {


        val assets: MutableList<CampusAsset> = mutableListOf()

        fun getRoomInfo(): String =
            "Room $roomNumber, $roomType, Floor $floorLevel"

        fun updateRoom(roomNumber: String, roomType: String, floorLevel: Int) {
            this.roomNumber = roomNumber
            this.roomType = roomType
            this.floorLevel = floorLevel
        }
    }

    class GISCoordinate(
        override val id: Int,
        var latitude: Double,
        var longitude: Double,
        var mapPinLabel: String,
        var zoneArea: String
    ) : Identifiable<Int> {

        fun getLocation(): String = "$latitude, $longitude"

        fun updateCoordinate(
            latitude: Double,
            longitude: Double,
            mapPinLabel: String,
            zoneArea: String
        ) {
            this.latitude = latitude
            this.longitude = longitude
            this.mapPinLabel = mapPinLabel
            this.zoneArea = zoneArea
        }
    }



    class CampusUser(
        override val id: Int,
        var fullName: String,
        var role: String
    ) : Identifiable<Int> {


        var campusMap: CampusMap? = null

        fun login(): Boolean = fullName.isNotBlank()

        fun logout() {

        }
    }

    class CampusMap(
        override val id: Int,
        var mapName: String,
        var owner: CampusUser
    ) : Identifiable<Int> {


        val layers: MutableList<MapLayer> = mutableListOf()

        val legends: MutableList<MapLegend> = mutableListOf()

        fun displayMap() {
            println("Displaying map: $mapName")
        }

        fun showAssets() {
            val allAssets = layers.flatMap { it.assets }
            allAssets.forEach { println(it.getAssetDetails()) }
        }

        fun updateMapView(mapName: String) {
            this.mapName = mapName
        }

        fun addLayer(layer: MapLayer) {
            layers.add(layer)
        }

        fun addLegend(legend: MapLegend) {
            legends.add(legend)
        }
    }

    class MapLayer(
        override val id: Int,
        var layerName: String,
        var layerType: String
    ) : Identifiable<Int> {

        var isVisible: Boolean = true


        val assets: MutableList<CampusAsset> = mutableListOf()

        fun toggleVisibility() {
            isVisible = !isVisible
        }
    }

    class MapLegend(
        override val id: Int,
        var label: String,
        var symbol: String
    ) : Identifiable<Int>

    class RouteCalculator {

        fun calculateRoute(
            start: GISCoordinate,
            destination: GISCoordinate
        ): List<GISCoordinate> {
            return listOf(start, destination)
        }

        fun optimizeRoute(route: List<GISCoordinate>): List<GISCoordinate> =
            route.distinctBy { it.id }
    }

    class NavigationService(
        private val routeCalculator: RouteCalculator
    ) {

        fun navigateToAsset(asset: CampusAsset): List<GISCoordinate> {
            val destination = asset.room.gisCoordinate
            return locateAsset(destination)
        }

        fun locateAsset(destination: GISCoordinate): List<GISCoordinate> {

            return routeCalculator.calculateRoute(
                start = destination,
                destination = destination
            )
        }
    }

    legends.add(legend)
}
}

class MapLayer(
    override val id: Int,
    var layerName: String,
    var layerType: String
) : Identifiable<Int> {

    var isVisible: Boolean = true


    val assets: MutableList<CampusAsset> = mutableListOf()

    fun toggleVisibility() {
        isVisible = !isVisible
    }
}

class MapLegend(
    override val id: Int,
    var label: String,
    var symbol: String
) : Identifiable<Int>

class RouteCalculator {

    fun calculateRoute(
        start: GISCoordinate,
        destination: GISCoordinate
    ): List<GISCoordinate> {
        return listOf(start, destination)
    }

    fun optimizeRoute(route: List<GISCoordinate>): List<GISCoordinate> =
        route.distinctBy { it.id }
}

class NavigationService(
    private val routeCalculator: RouteCalculator
) {

    fun navigateToAsset(asset: CampusAsset): List<GISCoordinate> {
        val destination = asset.room.gisCoordinate
        return locateAsset(destination)
    }

    fun locateAsset(destination: GISCoordinate): List<GISCoordinate> {

        return routeCalculator.calculateRoute(
            start = destination,
            destination = destination
        )
    }
}