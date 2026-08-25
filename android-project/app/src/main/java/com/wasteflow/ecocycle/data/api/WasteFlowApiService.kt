package com.wasteflow.ecocycle.data.api

import com.wasteflow.ecocycle.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface WasteFlowApiService {
    @POST("api/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<TokenResponse>

    @POST("api/auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<TokenResponse>

    @GET("api/auth/me")
    suspend fun getCurrentUser(
        @Header("Authorization") authorization: String
    ): Response<User>

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

    @GET("api/wallet/{userId}")
    suspend fun getWallet(@Path("userId") userId: String): Response<RewardWallet>

    @GET("api/wallet/{userId}/transactions")
    suspend fun getRewardTransactions(@Path("userId") userId: String): Response<List<RewardTransaction>>

    @GET("api/config/conversion-rate")
    suspend fun getConversionConfig(): Response<PointsConversionConfig>

    @GET("api/electricity/providers")
    suspend fun getElectricityProviders(): Response<List<ElectricityProvider>>

    @GET("api/electricity/bills/fetch")
    suspend fun fetchElectricityBill(
        @Query("providerId") providerId: String,
        @Query("consumerNumber") consumerNumber: String
    ): Response<ElectricityBill>

    @POST("api/electricity/bills/pay")
    suspend fun payElectricityBill(
        @Body request: BillPaymentRequest
    ): Response<BillPaymentReceipt>

    @GET("api/electricity/bills/history/{userId}")
    suspend fun getBillPaymentHistory(@Path("userId") userId: String): Response<List<BillPaymentReceipt>>
}
