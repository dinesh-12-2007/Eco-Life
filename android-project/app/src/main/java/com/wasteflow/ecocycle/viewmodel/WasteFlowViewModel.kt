package com.wasteflow.ecocycle.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wasteflow.ecocycle.data.model.*
import com.wasteflow.ecocycle.data.repository.WasteFlowRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class WasteFlowViewModel(
    private val repository: WasteFlowRepository = WasteFlowRepository()
) : ViewModel() {

    val currentUser: StateFlow<User> = repository.currentUser
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), User())

    val tasks: StateFlow<List<ServiceTask>> = repository.tasks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val complaints: StateFlow<List<Complaint>> = repository.complaints
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val employees: StateFlow<List<Employee>> = repository.employees
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val rewards: StateFlow<List<Reward>> = repository.rewards
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun selectRole(role: UserRole) {
        repository.setUserRole(role)
    }

    fun logWaste(zone: String, type: WasteType, weightKg: Double, points: Int) {
        viewModelScope.launch {
            repository.submitWasteLog(zone, type, weightKg, points)
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

    fun redeemReward(reward: Reward, onResult: (Boolean) -> Unit) {
        val success = repository.redeemReward(reward)
        onResult(success)
    }
}
