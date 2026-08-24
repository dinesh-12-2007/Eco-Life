package com.wasteflow.ecocycle.data.model

data class User(
    val id: String = "u-01",
    val name: String = "Alex",
    val email: String = "alex@ecocycle.org",
    val role: UserRole = UserRole.CITIZEN,
    val balancePoints: Int = 1250,
    val recycledKgYtd: Double = 45.0,
    val zone: String = "Zone B"
)

enum class UserRole {
    CITIZEN,
    SERVICE_EMPLOYEE,
    SYSTEM_MANAGEMENT
}

enum class WasteType(val title: String, val multiplier: Int) {
    PLASTIC("PLASTIC", 15),
    PAPER("PAPER", 5),
    METAL("METAL", 20),
    ORGANIC("ORGANIC", 2)
}

data class WasteLog(
    val id: String,
    val zone: String,
    val type: WasteType,
    val weightKg: Double,
    val pointsCalculated: Int,
    val timestamp: Long = System.currentTimeMillis()
)

enum class TaskType {
    COMPLAINT,
    GIVE_AWAY,
    ROUTINE
}

enum class TaskStatus {
    ASSIGNED,
    PENDING,
    IN_PROGRESS,
    COMPLETED
}

data class ServiceTask(
    val id: String,
    val type: TaskType,
    val title: String,
    val location: String,
    val time: String,
    val distance: String,
    val itemCount: Int = 0,
    val priority: String = "NORMAL",
    val status: TaskStatus = TaskStatus.ASSIGNED,
    val progress: Int = 0
)

enum class ComplaintPriority {
    HIGH,
    MEDIUM,
    LOW
}

enum class ComplaintStatus {
    PENDING,
    ASSIGNED,
    RESOLVED
}

data class Complaint(
    val id: String,
    val title: String,
    val description: String,
    val location: String,
    val priority: ComplaintPriority,
    val status: ComplaintStatus,
    val assignedCrew: String = "Unassigned",
    val imageUrl: String? = null,
    val isVerified: Boolean = false
)

data class Employee(
    val id: String,
    val name: String,
    val zone: String,
    val status: String, // "ON-DUTY", "BREAK", "OFF-DUTY"
    val performance: Double, // e.g. 4.8
    val tasksDone: Int,
    val avatarUrl: String? = null
)

data class Reward(
    val id: String,
    val title: String,
    val description: String,
    val costPoints: Int,
    val category: String,
    val isAvailable: Boolean = true
)

data class LiveTruckLocation(
    val truckNumber: String = "#402",
    val driverName: String = "Sarah Jenkins",
    val zone: String = "Zone B",
    val status: String = "EN ROUTE",
    val etaMinutes: Int = 12,
    val latitude: Double = 34.0522,
    val longitude: Double = -118.2437
)
