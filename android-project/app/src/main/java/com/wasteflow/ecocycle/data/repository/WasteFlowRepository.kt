package com.wasteflow.ecocycle.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.wasteflow.ecocycle.data.api.ApiClient
import com.wasteflow.ecocycle.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class WasteFlowRepository(
    private val context: Context? = null
) {
    private val api = ApiClient.apiService

    private val prefs: SharedPreferences? by lazy {
        val appContext = context ?: try {
            Class.forName("android.app.ActivityThread")
                .getMethod("currentApplication")
                .invoke(null) as? Context
        } catch (e: Exception) {
            null
        }
        appContext?.getSharedPreferences("wasteflow_auth", Context.MODE_PRIVATE)
    }

    private var inMemoryToken: String? = null
    private var inMemoryUserId: String? = null

    private fun getAccessToken(): String? {
        return prefs?.getString("access_token", inMemoryToken) ?: inMemoryToken
    }

    private fun getStoredUserId(): String? {
        return prefs?.getString("user_id", inMemoryUserId) ?: inMemoryUserId
    }

    private fun saveAccessToken(token: String) {
        inMemoryToken = token
        prefs?.edit()?.putString("access_token", token)?.apply()
    }

    private fun saveUserId(userId: String) {
        inMemoryUserId = userId
        prefs?.edit()?.putString("user_id", userId)?.apply()
    }

    fun logout() {
        inMemoryToken = null
        inMemoryUserId = null
        prefs?.edit()
            ?.remove("access_token")
            ?.remove("user_id")
            ?.apply()
    }

    fun isLoggedIn(): Boolean {
        val token = getAccessToken()
        return !token.isNullOrBlank()
    }

    suspend fun login(
        email: String,
        password: String
    ): Result<TokenResponse> {
        return try {
            val response = api.login(LoginRequest(email, password))
            if (response.isSuccessful && response.body() != null) {
                val tokenResponse = response.body()!!
                saveAccessToken(tokenResponse.access_token)
                saveUserId(tokenResponse.user_id)
                _currentUser.value = _currentUser.value.copy(
                    id = tokenResponse.user_id,
                    name = tokenResponse.name,
                    role = try { UserRole.valueOf(tokenResponse.role) } catch (e: Exception) { UserRole.CITIZEN }
                )
                refreshWallet(tokenResponse.user_id)
                Result.success(tokenResponse)
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Login failed with code ${response.code()}"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(
        email: String,
        password: String,
        name: String,
        role: String = "CITIZEN",
        zone: String? = null,
        phone: String? = null
    ): Result<TokenResponse> {
        return try {
            val response = api.register(
                RegisterRequest(
                    email = email,
                    password = password,
                    name = name,
                    role = role,
                    zone = zone,
                    phone = phone
                )
            )
            if (response.isSuccessful && response.body() != null) {
                val tokenResponse = response.body()!!
                saveAccessToken(tokenResponse.access_token)
                saveUserId(tokenResponse.user_id)
                _currentUser.value = _currentUser.value.copy(
                    id = tokenResponse.user_id,
                    name = tokenResponse.name,
                    role = try { UserRole.valueOf(tokenResponse.role) } catch (e: Exception) { UserRole.CITIZEN },
                    zone = zone ?: "Zone B"
                )
                refreshWallet(tokenResponse.user_id)
                Result.success(tokenResponse)
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Registration failed with code ${response.code()}"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun refreshWallet(userId: String = getStoredUserId() ?: _currentUser.value.id) {
        try {
            val walletResp = api.getWallet(userId)
            if (walletResp.isSuccessful && walletResp.body() != null) {
                val wallet = walletResp.body()!!
                _currentUser.value = _currentUser.value.copy(
                    balancePoints = wallet.currentPoints
                )
                if (wallet.transactions.isNotEmpty()) {
                    _transactions.value = wallet.transactions
                }
            }
        } catch (e: Exception) {
            // Non-blocking wallet refresh
        }
    }

    suspend fun getCurrentUser(
        token: String
    ): Result<User> {
        return try {
            val response = api.getCurrentUser("Bearer $token")
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Failed to fetch user profile with code ${response.code()}"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logWaste(
        wasteLog: WasteLog
    ): Result<WasteLog> {
        return submitWasteLog(
            zone = wasteLog.zone,
            type = wasteLog.type,
            weightKg = wasteLog.weightKg,
            points = wasteLog.pointsCalculated
        )
    }

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
                type = TaskType.MISSED_COLLECTION,
                title = "Commercial Bin Overflow",
                location = "Maple Ave",
                time = "10:15 AM",
                distance = "2.4 km away",
                itemCount = 3,
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

    private val _conversionConfig = MutableStateFlow(
        PointsConversionConfig(
            pointsPerUnit = 10,
            currencySymbol = "₹",
            description = "100 Reward Points = ₹10 Electricity Bill Credit"
        )
    )
    val conversionConfig: StateFlow<PointsConversionConfig> = _conversionConfig.asStateFlow()

    private val _electricityProviders = MutableStateFlow(
        listOf(
            ElectricityProvider("prov-01", "BESCOM (Bangalore Electricity)", "Karnataka", "BESCOM"),
            ElectricityProvider("prov-02", "TANGEDCO (Tamil Nadu Generation & Distribution)", "Tamil Nadu", "TANGEDCO"),
            ElectricityProvider("prov-03", "MSEDCL (Mahavitaran Maharashtra)", "Maharashtra", "MSEDCL"),
            ElectricityProvider("prov-04", "BSES Yamuna Power Limited", "Delhi", "BSES-Y"),
            ElectricityProvider("prov-05", "APSPDCL (Southern Power AP)", "Andhra Pradesh", "APSPDCL"),
            ElectricityProvider("prov-06", "Tata Power DDL", "Delhi-NCR", "TATAPOWER")
        )
    )
    val electricityProviders: StateFlow<List<ElectricityProvider>> = _electricityProviders.asStateFlow()

    private val _transactions = MutableStateFlow(
        listOf(
            RewardTransaction(
                id = "tx-101",
                type = TransactionType.EARN,
                points = 150,
                description = "Segregated Plastic Recycling (10.0 kg)",
                date = "2026-08-22 14:30",
                referenceType = "WASTE_LOG",
                referenceId = "WL-8819"
            ),
            RewardTransaction(
                id = "tx-102",
                type = TransactionType.EARN,
                points = 200,
                description = "Metal & Aluminium Cans Drop-off (10.0 kg)",
                date = "2026-08-18 10:15",
                referenceType = "WASTE_LOG",
                referenceId = "WL-8702"
            ),
            RewardTransaction(
                id = "tx-103",
                type = TransactionType.REDEEM,
                points = 600,
                description = "Electricity Bill Credit - BESCOM (Cons. #90283471)",
                date = "2026-08-10 11:20",
                referenceType = "ELECTRICITY_BILL",
                referenceId = "PAY-5510"
            ),
            RewardTransaction(
                id = "tx-104",
                type = TransactionType.EARN,
                points = 1500,
                description = "Citizen Bonus: Zero-Contamination Waste Streak",
                date = "2026-08-01 09:00",
                referenceType = "STREAK_BONUS",
                referenceId = "STRK-01"
            )
        )
    )
    val transactions: StateFlow<List<RewardTransaction>> = _transactions.asStateFlow()

    private val _billPaymentReceipts = MutableStateFlow(
        listOf(
            BillPaymentReceipt(
                paymentId = "PAY-5510",
                billNumber = "BILL-AUG-9921",
                consumerNumber = "90283471",
                providerName = "BESCOM (Bangalore Electricity)",
                totalBillAmount = 850.0,
                pointsRedeemed = 600,
                pointsDiscountAmount = 60.0,
                amountPaid = 790.0,
                transactionRef = "TXN_ELEC_993821093",
                timestamp = "2026-08-10 11:20",
                updatedWalletBalance = 1250,
                status = "SUCCESS"
            )
        )
    )
    val billPaymentReceipts: StateFlow<List<BillPaymentReceipt>> = _billPaymentReceipts.asStateFlow()

    fun setUserRole(role: UserRole) {
        _currentUser.value = _currentUser.value.copy(role = role)
    }

    fun setConversionConfig(pointsPerUnit: Int, currencySymbol: String, description: String) {
        _conversionConfig.value = PointsConversionConfig(pointsPerUnit, currencySymbol, description)
    }

    suspend fun submitWasteLog(
        zone: String,
        type: WasteType,
        weightKg: Double,
        points: Int = 0
    ): Result<WasteLog> {
        val token = getAccessToken()
        if (token.isNullOrBlank()) {
            return Result.failure(IllegalStateException("User is not authenticated. Please log in first."))
        }

        return try {
            val wasteLogPayload = WasteLog(
                id = "wl-${UUID.randomUUID().toString().take(8)}",
                zone = zone,
                type = type,
                weightKg = weightKg,
                pointsCalculated = 0 // Server calculates points securely based on waste type and weight
            )

            val response = api.logWaste(
                authorization = "Bearer $token",
                wasteLog = wasteLogPayload
            )

            if (response.isSuccessful && response.body() != null) {
                val returnedLog = response.body()!!

                // Update cumulative recycled weight
                val user = _currentUser.value
                _currentUser.value = user.copy(
                    recycledKgYtd = user.recycledKgYtd + weightKg
                )

                // Refresh user's wallet balance and transactions from live backend
                val currentUserId = getStoredUserId() ?: _currentUser.value.id
                refreshWallet(currentUserId)

                Result.success(returnedLog)
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMsg = if (!errorBody.isNullOrBlank()) {
                    errorBody
                } else {
                    "Waste logging failed with HTTP code ${response.code()}"
                }
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun fetchElectricityBill(providerId: String, consumerNumber: String): ElectricityBill {
        val provider = _electricityProviders.value.find { it.id == providerId }
            ?: _electricityProviders.value.first()
        
        // Deterministic realistic bill generation based on consumer number
        val hash = (consumerNumber.hashCode() and 0x7FFFFFFF)
        val calculatedAmount = 450.0 + (hash % 1200)
        val month = "August 2026"
        val dueDate = "2026-09-10"

        return ElectricityBill(
            id = "EB-${consumerNumber.takeLast(4)}",
            providerId = provider.id,
            providerName = provider.name,
            consumerNumber = consumerNumber,
            consumerName = _currentUser.value.name,
            billNumber = "EBILL-${(hash % 90000) + 10000}",
            billingMonth = month,
            dueDate = dueDate,
            billAmount = calculatedAmount,
            status = "UNPAID"
        )
    }

    fun payElectricityBill(
        providerId: String,
        consumerNumber: String,
        billNumber: String,
        totalBillAmount: Double,
        pointsToRedeem: Int
    ): Result<BillPaymentReceipt> {
        val user = _currentUser.value
        val config = _conversionConfig.value

        // Server-side validation: Citizen cannot redeem more points than owned
        if (pointsToRedeem < 0) {
            return Result.failure(IllegalArgumentException("Points to redeem cannot be negative."))
        }
        if (pointsToRedeem > user.balancePoints) {
            return Result.failure(IllegalStateException("Insufficient reward points. Available: ${user.balancePoints} PTS, Requested: $pointsToRedeem PTS."))
        }

        // Calculate discount (points / pointsPerUnit)
        val discountAmount = pointsToRedeem.toDouble() / config.pointsPerUnit
        if (discountAmount > totalBillAmount) {
            return Result.failure(IllegalArgumentException("Reward discount cannot exceed total bill amount."))
        }

        val amountPaid = totalBillAmount - discountAmount
        val updatedPoints = user.balancePoints - pointsToRedeem

        // Atomically update user balance
        _currentUser.value = user.copy(balancePoints = updatedPoints)

        val provider = _electricityProviders.value.find { it.id == providerId }
        val providerName = provider?.name ?: "Electricity Board"

        val paymentId = "PAY-${System.currentTimeMillis() % 100000}"
        val txnRef = "TXN_ELEC_${System.currentTimeMillis()}"

        // Record REDEEM transaction
        if (pointsToRedeem > 0) {
            val tx = RewardTransaction(
                id = "tx-${System.currentTimeMillis() % 100000}",
                type = TransactionType.REDEEM,
                points = pointsToRedeem,
                description = "Electricity Bill Credit - $providerName (Cons. #$consumerNumber)",
                date = "Today, Just now",
                referenceType = "ELECTRICITY_BILL",
                referenceId = paymentId
            )
            _transactions.value = listOf(tx) + _transactions.value
        }

        val receipt = BillPaymentReceipt(
            paymentId = paymentId,
            billNumber = billNumber,
            consumerNumber = consumerNumber,
            providerName = providerName,
            totalBillAmount = totalBillAmount,
            pointsRedeemed = pointsToRedeem,
            pointsDiscountAmount = discountAmount,
            amountPaid = amountPaid,
            transactionRef = txnRef,
            timestamp = "Today, Just now",
            updatedWalletBalance = updatedPoints,
            status = "SUCCESS"
        )

        _billPaymentReceipts.value = listOf(receipt) + _billPaymentReceipts.value
        return Result.success(receipt)
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
