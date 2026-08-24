package com.wasteflow.ecocycle.navigation

sealed class Screen(val route: String) {
    object RoleSelection : Screen("role_selection")
    object Auth : Screen("auth")
    object CitizenHome : Screen("citizen_home")
    object LiveTracking : Screen("live_tracking")
    object Rewards : Screen("rewards")
    object ElectricityBill : Screen("electricity_bill")
    object EmployeeDashboard : Screen("employee_dashboard")
    object LogWaste : Screen("log_waste")
    object Heatmap : Screen("heatmap")
    object Analytics : Screen("analytics")
    object Roster : Screen("roster")
    object Complaints : Screen("complaints")
}
