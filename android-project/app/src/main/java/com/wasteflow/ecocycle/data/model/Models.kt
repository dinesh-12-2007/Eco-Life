package com.wasteflow.ecocycle.data.model

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val email: String,
    val password: String,
    val name: String,
    val role: String = "CITIZEN",
    val zone: String? = null,
    val phone: String? = null
)

data class TokenResponse(
    val access_token: String,
    val token_type: String,
    val user_id: String,
    val role: String,
    val name: String
)

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val role: UserRole = UserRole.CITIZEN,
    val balancePoints: Int = 0,
    val recycledKgYtd: Double = 0.0,
    val zone: String = ""
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
    MISSED_COLLECTION,
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

enum class TransactionType {
    EARN,
    REDEEM
}

data class RewardTransaction(
    val id: String,
    val type: TransactionType,
    val points: Int,
    val description: String,
    val date: String,
    val referenceType: String? = null,
    val referenceId: String? = null
)

data class RewardWallet(
    val currentPoints: Int = 0,
    val totalEarned: Int = 0,
    val totalUsed: Int = 0,
    val transactions: List<RewardTransaction> = emptyList()
)

data class ElectricityProvider(
    val id: String,
    val name: String,
    val state: String,
    val code: String
)

data class ElectricityBill(
    val id: String,
    val providerId: String,
    val providerName: String,
    val consumerNumber: String,
    val consumerName: String,
    val billNumber: String,
    val billingMonth: String,
    val dueDate: String,
    val billAmount: Double,
    val status: String = "UNPAID" // "UNPAID", "PAID"
)

data class BillPaymentRequest(
    val providerId: String,
    val consumerNumber: String,
    val billNumber: String,
    val totalBillAmount: Double,
    val pointsToRedeem: Int
)

data class BillPaymentReceipt(
    val paymentId: String,
    val billNumber: String,
    val consumerNumber: String,
    val providerName: String,
    val totalBillAmount: Double,
    val pointsRedeemed: Int,
    val pointsDiscountAmount: Double,
    val amountPaid: Double,
    val transactionRef: String,
    val timestamp: String,
    val updatedWalletBalance: Int,
    val status: String = "SUCCESS"
)

data class PointsConversionConfig(
    val pointsPerUnit: Int = 10,       // 10 points = 1.0 currency unit (i.e. 100 pts = ₹10)
    val currencySymbol: String = "₹",
    val description: String = "100 Points = ₹10 Electricity Bill Credit"
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
