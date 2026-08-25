package com.wasteflow.ecocycle.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wasteflow.ecocycle.data.model.*
import com.wasteflow.ecocycle.data.repository.WasteFlowRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class WasteFlowViewModel(
    private val repository: WasteFlowRepository = WasteFlowRepository()
) : ViewModel() {

    private val _authLoading = MutableStateFlow(false)
    val authLoading: StateFlow<Boolean> = _authLoading.asStateFlow()

    private val _authError = MutableStateFlow<String?>(null)
    val authError: StateFlow<String?> = _authError.asStateFlow()

    val currentUser: StateFlow<User> = repository.currentUser
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), User())

    val tasks: StateFlow<List<ServiceTask>> = repository.tasks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val complaints: StateFlow<List<Complaint>> = repository.complaints
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val employees: StateFlow<List<Employee>> = repository.employees
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val conversionConfig: StateFlow<PointsConversionConfig> = repository.conversionConfig
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PointsConversionConfig())

    val electricityProviders: StateFlow<List<ElectricityProvider>> = repository.electricityProviders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val transactions: StateFlow<List<RewardTransaction>> = repository.transactions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val billPaymentReceipts: StateFlow<List<BillPaymentReceipt>> = repository.billPaymentReceipts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun selectRole(role: UserRole) {
        repository.setUserRole(role)
    }

    fun logWaste(zone: String, type: WasteType, weightKg: Double, points: Int) {
        viewModelScope.launch {
            repository.submitWasteLog(zone, type, weightKg, points)
        }
    }

    fun fetchElectricityBill(providerId: String, consumerNumber: String): ElectricityBill {
        return repository.fetchElectricityBill(providerId, consumerNumber)
    }

    fun payElectricityBill(
        providerId: String,
        consumerNumber: String,
        billNumber: String,
        totalBillAmount: Double,
        pointsToRedeem: Int,
        onResult: (Result<BillPaymentReceipt>) -> Unit
    ) {
        viewModelScope.launch {
            val result = repository.payElectricityBill(
                providerId = providerId,
                consumerNumber = consumerNumber,
                billNumber = billNumber,
                totalBillAmount = totalBillAmount,
                pointsToRedeem = pointsToRedeem
            )
            onResult(result)
        }
    }

    fun updateConversionRate(pointsPerUnit: Int, currencySymbol: String, description: String) {
        viewModelScope.launch {
            repository.setConversionConfig(pointsPerUnit, currencySymbol, description)
        }
    }

    fun completeTask(taskId: String) {
        viewModelScope.launch {
            repository.updateTaskStatus(taskId, TaskStatus.COMPLETED)
        }
    }

    fun startTask(taskId: String) {
        viewModelScope.launch {
            repository.updateTaskStatus(taskId, TaskStatus.IN_PROGRESS)
        }
    }

    fun acceptTask(taskId: String) {
        viewModelScope.launch {
            repository.updateTaskStatus(taskId, TaskStatus.IN_PROGRESS)
        }
    }

    fun resolveComplaint(complaintId: String) {
        viewModelScope.launch {
            repository.resolveComplaint(complaintId)
        }
    }

    fun submitComplaint(title: String, description: String, location: String, priority: ComplaintPriority) {
        viewModelScope.launch {
            repository.addComplaint(title, description, location, priority)
        }
    }

    fun login(
        email: String,
        password: String,
        onResult: (Result<TokenResponse>) -> Unit
    ) {
        viewModelScope.launch {
            _authLoading.value = true
            _authError.value = null

            val result = repository.login(email, password)

            _authLoading.value = false

            if (result.isFailure) {
                _authError.value = result.exceptionOrNull()?.message
            }

            onResult(result)
        }
    }

    fun register(
        email: String,
        password: String,
        name: String,
        role: String = "CITIZEN",
        zone: String? = null,
        phone: String? = null,
        onResult: (Result<TokenResponse>) -> Unit
    ) {
        viewModelScope.launch {
            _authLoading.value = true
            _authError.value = null

            val result = repository.register(
                email = email,
                password = password,
                name = name,
                role = role,
                zone = zone,
                phone = phone
            )

            _authLoading.value = false

            if (result.isFailure) {
                _authError.value = result.exceptionOrNull()?.message
            }

            onResult(result)
        }
    }
}
