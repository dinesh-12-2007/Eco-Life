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
    val rewards by viewModel.rewards.collectAsState()

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
                onLoginSuccess = {
                    when (currentUser.role) {
                        UserRole.CITIZEN -> navController.navigate(Screen.CitizenHome.route) {
                            popUpTo(Screen.RoleSelection.route) { inclusive = true }
                        }
                        UserRole.SERVICE_EMPLOYEE, UserRole.SYSTEM_MANAGEMENT -> navController.navigate(Screen.EmployeeDashboard.route) {
                            popUpTo(Screen.RoleSelection.route) { inclusive = true }
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
                onNavigateGiveAway = { navController.navigate(Screen.GiveAway.route) },
                onNavigateRedeemPoints = { navController.navigate(Screen.Rewards.route) },
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

        // Give Away Screen
        composable(Screen.GiveAway.route) {
            GiveAwayScreen(
                onNavigateBack = { navController.popBackStack() },
                onSubmitSuccess = { navController.popBackStack() }
            )
        }

        // Rewards Screen
        composable(Screen.Rewards.route) {
            RewardsScreen(
                user = currentUser,
                rewards = rewards,
                onRedeemReward = { reward ->
                    viewModel.redeemReward(reward) {}
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
