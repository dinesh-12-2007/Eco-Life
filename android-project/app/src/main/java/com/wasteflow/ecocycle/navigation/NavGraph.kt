package com.wasteflow.ecocycle.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.wasteflow.ecocycle.data.model.UserRole
import com.wasteflow.ecocycle.ui.screens.admin.*
import com.wasteflow.ecocycle.ui.screens.auth.AuthScreen
import com.wasteflow.ecocycle.ui.screens.citizen.*
import com.wasteflow.ecocycle.ui.screens.splash.RoleSelectionScreen
import com.wasteflow.ecocycle.viewmodel.WasteFlowViewModel

@Composable
fun WasteFlowNavGraph(
    navController: NavHostController,
    viewModel: WasteFlowViewModel
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val tasks by viewModel.tasks.collectAsState()
    val complaints by viewModel.complaints.collectAsState()
    val employees by viewModel.employees.collectAsState()
    val transactions by viewModel.transactions.collectAsState()
    val conversionConfig by viewModel.conversionConfig.collectAsState()
    val electricityProviders by viewModel.electricityProviders.collectAsState()
    val billPaymentReceipts by viewModel.billPaymentReceipts.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Screen.RoleSelection.route
    ) {
        // Role Selection / Splash
        composable(Screen.RoleSelection.route) {
            RoleSelectionScreen(
                onRoleSelected = { role ->
                    viewModel.selectRole(role)
                },
                onGetStarted = {
                    navController.navigate(Screen.Auth.route)
                }
            )
        }

        // Auth Screen
        composable(Screen.Auth.route) {
            AuthScreen(
                viewModel = viewModel,
                onLoginSuccess = { tokenResponse ->
                    when (tokenResponse.role) {
                        "CITIZEN" -> {
                            navController.navigate(Screen.CitizenHome.route) {
                                popUpTo(Screen.RoleSelection.route) { inclusive = true }
                            }
                        }

                        "SERVICE_EMPLOYEE", "SYSTEM_MANAGEMENT" -> {
                            navController.navigate(Screen.EmployeeDashboard.route) {
                                popUpTo(Screen.RoleSelection.route) { inclusive = true }
                            }
                        }

                        else -> {
                            navController.navigate(Screen.CitizenHome.route) {
                                popUpTo(Screen.RoleSelection.route) { inclusive = true }
                            }
                        }
                    }
                }
            )
        }

        // Citizen Home
        composable(Screen.CitizenHome.route) {
            CitizenDashboardScreen(
                user = currentUser,
                onNavigateLiveRoute = { navController.navigate(Screen.LiveTracking.route) },
                onNavigateReportIssue = { navController.navigate(Screen.Complaints.route) },
                onNavigateRedeemPoints = { navController.navigate(Screen.Rewards.route) },
                onNavigateElectricityBill = { navController.navigate(Screen.ElectricityBill.route) },
                onRoleSwitchClick = { navController.navigate(Screen.RoleSelection.route) }
            )
        }

        // Live Tracking Screen
        composable(Screen.LiveTracking.route) {
            LiveTrackingScreen(
                onNavigateBack = { navController.popBackStack() },
                onReportIssue = { navController.navigate(Screen.Complaints.route) }
            )
        }

        // Rewards Screen (Reward Points Wallet)
        composable(Screen.Rewards.route) {
            RewardsScreen(
                user = currentUser,
                transactions = transactions,
                conversionConfig = conversionConfig,
                onNavigateElectricityBill = { navController.navigate(Screen.ElectricityBill.route) },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Electricity Bill Payment Screen
        composable(Screen.ElectricityBill.route) {
            ElectricityBillScreen(
                user = currentUser,
                providers = electricityProviders,
                conversionConfig = conversionConfig,
                paymentHistory = billPaymentReceipts,
                onFetchBill = { providerId, consumerNumber ->
                    viewModel.fetchElectricityBill(providerId, consumerNumber)
                },
                onPayBill = { providerId, consumerNumber, billNumber, totalAmount, pointsToRedeem, onResult ->
                    viewModel.payElectricityBill(
                        providerId = providerId,
                        consumerNumber = consumerNumber,
                        billNumber = billNumber,
                        totalBillAmount = totalAmount,
                        pointsToRedeem = pointsToRedeem,
                        onResult = onResult
                    )
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Employee & Admin Dashboard
        composable(Screen.EmployeeDashboard.route) {
            EmployeeDashboardScreen(
                tasks = tasks,
                onAcceptTask = { viewModel.acceptTask(it) },
                onStartTask = { viewModel.startTask(it) },
                onCompleteTask = { viewModel.completeTask(it) },
                onNavigateBack = { navController.navigate(Screen.RoleSelection.route) },
                onNavigateLogWaste = { navController.navigate(Screen.LogWaste.route) },
                onNavigateHeatmap = { navController.navigate(Screen.Heatmap.route) },
                onNavigateAnalytics = { navController.navigate(Screen.Analytics.route) },
                onNavigateRoster = { navController.navigate(Screen.Roster.route) },
                onNavigateComplaints = { navController.navigate(Screen.Complaints.route) }
            )
        }

        // Log Waste
        composable(Screen.LogWaste.route) {
            LogWasteScreen(
                onLogSubmitted = { zone, type, weight, points ->
                    viewModel.logWaste(zone, type, weight, points)
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Heatmap
        composable(Screen.Heatmap.route) {
            HeatmapScreen(
                onNavigateBack = { navController.popBackStack() },
                onViewSectorDetails = { navController.navigate(Screen.Complaints.route) }
            )
        }

        // Analytics
        composable(Screen.Analytics.route) {
            AnalyticsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Roster
        composable(Screen.Roster.route) {
            RosterScreen(
                employees = employees,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Complaints
        composable(Screen.Complaints.route) {
            ComplaintsScreen(
                complaints = complaints,
                onResolveComplaint = { viewModel.resolveComplaint(it) },
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
