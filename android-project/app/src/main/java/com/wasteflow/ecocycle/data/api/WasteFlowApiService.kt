package com.wasteflow.ecocycle.data.api

import com.wasteflow.ecocycle.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface WasteFlowApiService {
    @GET("api/user/{id}")
    suspend fun getUserProfile(@Path("id") userId: String): Response<User>

    @POST("api/waste/log")
    suspend fun logWaste(@Body wasteLog: WasteLog): Response<WasteLog>

    @GET("api/tasks")
    suspend fun getTasks(): Response<List<ServiceTask>>

    @POST("api/tasks/{id}/status")
    suspend fun updateTaskStatus(
        @Path("id") taskId: String,
        @Query("status") status: TaskStatus
    ): Response<ServiceTask>

    @GET("api/complaints")
    suspend fun getComplaints(): Response<List<Complaint>>

    @POST("api/complaints/{id}/resolve")
    suspend fun resolveComplaint(@Path("id") complaintId: String): Response<Complaint>

    @GET("api/employees")
    suspend fun getEmployees(): Response<List<Employee>>

    @GET("api/rewards")
    suspend fun getRewards(): Response<List<Reward>>
}
