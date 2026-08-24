package com.wasteflow.ecocycle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.wasteflow.ecocycle.navigation.WasteFlowNavGraph
import com.wasteflow.ecocycle.ui.theme.EcoSurface
import com.wasteflow.ecocycle.ui.theme.WasteFlowTheme
import com.wasteflow.ecocycle.viewmodel.WasteFlowViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: WasteFlowViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WasteFlowTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = EcoSurface
                ) {
                    val navController = rememberNavController()
                    WasteFlowNavGraph(
                        navController = navController,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}
