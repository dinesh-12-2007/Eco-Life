package com.wasteflow.ecocycle.data.repository

import com.wasteflow.ecocycle.data.api.ApiClient
import com.wasteflow.ecocycle.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class WasteFlowRepository {
    private val api = ApiClient.apiService

    // In-memory persistent state for realistic offline-first experience
    private val _currentUser = MutableStateFlow(
        User(
            id = "u-01",
            name = "Alex",
            email = "alex@ecocycle.org",
            role = UserRole.CITIZEN,
            balancePoints = 1250,
            recycledKgYtd = 45.0,
            zone = "Zone B"
        )
    )
    val currentUser: StateFlow<User> = _currentUser.asStateFlow()

    private val _tasks = MutableStateFlow(
        listOf(
            ServiceTask(
                id = "t-101",
                type = TaskType.COMPLAINT,
                title = "Missed Collection",
                location = "Oak St",
                time = "08:30 AM",
                distance = "1.2 km away",
                priority = "URGENT",
                status = TaskStatus.ASSIGNED
            ),
            ServiceTask(
                id = "t-102",
                type = TaskType.GIVE_AWAY,
                title = "Give Away Pickup",
                location = "Maple Ave",
                time = "10:15 AM",
                distance = "2.4 km away",
                itemCount = 4,
                priority = "NORMAL",
                status = TaskStatus.PENDING
            ),
            ServiceTask(
                id = "t-103",
                type = TaskType.ROUTINE,
                title = "Weekly Collection",
                location = "Sector 4",
                time = "11:30 AM",
                distance = "Current",
                priority = "ROUTINE",
                status = TaskStatus.IN_PROGRESS,
                progress = 65
            )
        )
    )
    val tasks: StateFlow<List<ServiceTask>> = _tasks.asStateFlow()

    private val _complaints = MutableStateFlow(
        listOf(
            Complaint(
                id = "C-9921",
                title = "Missed Collection",
                description = "Bins overflowing for three consecutive days. Attracting pests. Immediate action required at corner intersection.",
                location = "42 Oak St, West District",
                priority = ComplaintPriority.HIGH,
                status = ComplaintStatus.ASSIGNED,
                assignedCrew = "Unit Alpha (Rapid)",
                imageUrl = "https://images.unsplash.com/photo-1530587191325-3db32d826c18?w=500&auto=format&fit=crop&q=60"
            ),
            Complaint(
                id = "C-9920",
                title = "Damaged Public Bin",
                description = "Lid broken, causing recyclables to blow into the park area during high winds.",
                location = "Centennial Park North",
                priority = ComplaintPriority.MEDIUM,
                status = ComplaintStatus.PENDING,
                assignedCrew = "Maintenance Team 2"
            ),
            Complaint(
                id = "C-9918",
                title = "Illegal Dumping",
                description = "Large electronic appliances discarded beside recycling station.",
                location = "88 Pine Avenue",
                priority = ComplaintPriority.HIGH,
                status = ComplaintStatus.PENDING,
                assignedCrew = "Unassigned"
            ),
            Complaint(
                id = "C-9915",
                title = "Hazardous Waste Alert",
                description = "Automotive batteries placed in household recycling bin.",
                location = "14 Elm St",
                priority = ComplaintPriority.HIGH,
                status = ComplaintStatus.RESOLVED,
                assignedCrew = "Unit Bravo (Heavy)",
                isVerified = true
            )
        )
    )
    val complaints: StateFlow<List<Complaint>> = _complaints.asStateFlow()

    private val _employees = MutableStateFlow(
        listOf(
            Employee("emp-01", "Sarah Jenkins", "Zone B", "ON-DUTY", 4.8, 142),
            Employee("emp-02", "Elena Rodriguez", "Zone C", "ON-DUTY", 4.6, 128),
            Employee("emp-03", "Marcus Cole", "Zone A", "BREAK", 4.2, 98),
            Employee("emp-04", "David Kim", "Zone B", "OFF-DUTY", 4.5, 87)
        )
    )
    val employees: StateFlow<List<Employee>> = _employees.asStateFlow()

    private val _rewards = MutableStateFlow(
        listOf(
            Reward("r-01", "$15 Eco Groceries Voucher", "Redeemable at Organic Corner & Whole Earth markets", 500, "Vouchers"),
            Reward("r-02", "Free 7-Day Transit Pass", "Unlimited subway and bus transit pass", 750, "Transit"),
            Reward("r-03", "EcoCycle Heavy-Duty Canvas Tote", "Limited edition brutalist upcycled tote bag", 300, "Merchandise"),
            Reward("r-04", "$25 Electric Bill Credit", "Direct municipal solar power rebate", 1200, "Utilities")
        )
    )
    val rewards: StateFlow<List<Reward>> = _rewards.asStateFlow()

    fun setUserRole(role: UserRole) {
        _currentUser.value = _currentUser.value.copy(role = role)
    }

    fun submitWasteLog(zone: String, type: WasteType, weightKg: Double, points: Int) {
        val user = _currentUser.value
        _currentUser.value = user.copy(
            balancePoints = user.balancePoints + points,
            recycledKgYtd = user.recycledKgYtd + weightKg
        )
    }

    fun redeemReward(reward: Reward): Boolean {
        val user = _currentUser.value
        if (user.balancePoints >= reward.costPoints) {
            _currentUser.value = user.copy(balancePoints = user.balancePoints - reward.costPoints)
            return true
        }
        return false
    }

    fun updateTaskStatus(taskId: String, status: TaskStatus) {
        _tasks.value = _tasks.value.map {
            if (it.id == taskId) it.copy(status = status) else it
        }
    }

    fun resolveComplaint(complaintId: String) {
        _complaints.value = _complaints.value.map {
            if (it.id == complaintId) it.copy(status = ComplaintStatus.RESOLVED, isVerified = true) else it
        }
    }

    fun addComplaint(title: String, description: String, location: String, priority: ComplaintPriority) {
        val newComplaint = Complaint(
            id = "C-${(9000..9999).random()}",
            title = title,
            description = description,
            location = location,
            priority = priority,
            status = ComplaintStatus.PENDING
        )
        _complaints.value = listOf(newComplaint) + _complaints.value
    }
}
