import React, { useState } from 'react';
import { Download, FileCode, Folder, Terminal, Check, Copy, ChevronRight, ChevronDown, Smartphone, ShieldCheck, Sparkles } from 'lucide-react';

interface Props {
  onClose?: () => void;
}

export const AndroidProjectExplorer: React.FC<Props> = () => {
  const [selectedFile, setSelectedFile] = useState<string>('app/src/main/java/com/wasteflow/ecocycle/MainActivity.kt');
  const [copied, setCopied] = useState(false);

  const fileContents: Record<string, string> = {
    'app/src/main/java/com/wasteflow/ecocycle/MainActivity.kt': `package com.wasteflow.ecocycle

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
}`,
    'app/src/main/java/com/wasteflow/ecocycle/ui/components/MinimalCard.kt': `package com.wasteflow.ecocycle.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val CleanRadius = RoundedCornerShape(16.dp)

@Composable
fun MinimalCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.White,
    borderColor: Color = Color(0xFFE2E8F0),
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier
            .border(1.dp, borderColor, CleanRadius)
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        shape = CleanRadius,
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            content = content
        )
    }
}`,
    'app/src/main/java/com/wasteflow/ecocycle/navigation/NavGraph.kt': `package com.wasteflow.ecocycle.navigation

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
        composable(Screen.RoleSelection.route) {
            RoleSelectionScreen(
                onRoleSelected = { viewModel.selectRole(it) },
                onGetStarted = { navController.navigate(Screen.Auth.route) }
            )
        }
        // ... (All 12 Native Jetpack Compose Screens)
    }
}`,
    'app/build.gradle.kts': `plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.wasteflow.ecocycle"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.wasteflow.ecocycle"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
}`,
  };

  const copyToClipboard = (text: string) => {
    navigator.clipboard.writeText(text);
    setCopied(true);
    setTimeout(() => setCopied(false), 2000);
  };

  return (
    <div className="bg-white border border-slate-200 rounded-2xl shadow-sm p-6 flex flex-col h-full overflow-hidden">
      {/* Top Banner with Download APK/ZIP CTA */}
      <div className="flex flex-col sm:flex-row items-start sm:items-center justify-between pb-5 border-b border-slate-100 gap-4">
        <div>
          <div className="flex items-center space-x-2.5">
            <div className="w-8 h-8 bg-indigo-50 text-indigo-600 rounded-lg flex items-center justify-center">
              <Smartphone className="w-4 h-4" />
            </div>
            <h2 className="text-lg font-bold text-slate-900">
              Native Android Kotlin & Gradle Project
            </h2>
          </div>
          <p className="text-xs text-slate-500 mt-1">
            Complete Android Studio ready project with Jetpack Compose, Material 3, and Coroutines.
          </p>
        </div>

        <div className="flex items-center space-x-2 w-full sm:w-auto">
          <a
            href="/wasteflow-android-project.zip"
            download="wasteflow-android-project.zip"
            className="flex-1 sm:flex-none px-4 py-2 bg-indigo-600 hover:bg-indigo-700 text-white rounded-xl font-medium text-xs shadow-sm shadow-indigo-100 flex items-center justify-center space-x-2 transition-colors"
          >
            <Download className="w-4 h-4" />
            <span>Download Project ZIP</span>
          </a>
        </div>
      </div>

      {/* Terminal Command Bar */}
      <div className="bg-slate-900 text-slate-200 px-4 py-3 my-4 rounded-xl flex items-center justify-between font-mono text-xs border border-slate-800 shadow-xs">
        <div className="flex items-center space-x-2 overflow-x-auto">
          <Terminal className="w-4 h-4 text-emerald-400 shrink-0" />
          <span className="text-emerald-400 font-bold">$</span>
          <span>./gradlew assembleDebug</span>
        </div>
        <button
          onClick={() => copyToClipboard('./gradlew assembleDebug')}
          className="ml-3 px-2.5 py-1 bg-slate-800 hover:bg-slate-700 text-slate-300 hover:text-white rounded-md text-[11px] font-sans transition-colors cursor-pointer"
        >
          {copied ? 'Copied' : 'Copy'}
        </button>
      </div>

      {/* Two Column Layout: File Tree & Source Code Viewer */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4 flex-1 min-h-0 overflow-hidden">
        {/* File Tree */}
        <div className="bg-slate-50 border border-slate-200 rounded-xl p-3 overflow-y-auto">
          <div className="text-[10px] font-bold text-slate-400 uppercase tracking-wider mb-2.5 px-1">
            Project Structure
          </div>

          <div className="space-y-1 text-xs">
            <div
              onClick={() => setSelectedFile('app/build.gradle.kts')}
              className={`p-2 rounded-lg flex items-center space-x-2 cursor-pointer transition-colors ${
                selectedFile === 'app/build.gradle.kts' ? 'bg-indigo-50 text-indigo-700 font-semibold' : 'text-slate-700 hover:bg-slate-100'
              }`}
            >
              <FileCode className="w-4 h-4 text-indigo-600 shrink-0" />
              <span className="truncate font-mono">build.gradle.kts</span>
            </div>

            <div
              onClick={() => setSelectedFile('app/src/main/java/com/wasteflow/ecocycle/MainActivity.kt')}
              className={`p-2 rounded-lg flex items-center space-x-2 cursor-pointer transition-colors ${
                selectedFile === 'app/src/main/java/com/wasteflow/ecocycle/MainActivity.kt' ? 'bg-indigo-50 text-indigo-700 font-semibold' : 'text-slate-700 hover:bg-slate-100'
              }`}
            >
              <FileCode className="w-4 h-4 text-indigo-600 shrink-0" />
              <span className="truncate font-mono">MainActivity.kt</span>
            </div>

            <div
              onClick={() => setSelectedFile('app/src/main/java/com/wasteflow/ecocycle/navigation/NavGraph.kt')}
              className={`p-2 rounded-lg flex items-center space-x-2 cursor-pointer transition-colors ${
                selectedFile === 'app/src/main/java/com/wasteflow/ecocycle/navigation/NavGraph.kt' ? 'bg-indigo-50 text-indigo-700 font-semibold' : 'text-slate-700 hover:bg-slate-100'
              }`}
            >
              <FileCode className="w-4 h-4 text-indigo-600 shrink-0" />
              <span className="truncate font-mono">NavGraph.kt</span>
            </div>

            <div
              onClick={() => setSelectedFile('app/src/main/java/com/wasteflow/ecocycle/ui/components/MinimalCard.kt')}
              className={`p-2 rounded-lg flex items-center space-x-2 cursor-pointer transition-colors ${
                selectedFile === 'app/src/main/java/com/wasteflow/ecocycle/ui/components/MinimalCard.kt' ? 'bg-indigo-50 text-indigo-700 font-semibold' : 'text-slate-700 hover:bg-slate-100'
              }`}
            >
              <FileCode className="w-4 h-4 text-indigo-600 shrink-0" />
              <span className="truncate font-mono">MinimalCard.kt</span>
            </div>
          </div>
        </div>

        {/* Code Content */}
        <div className="md:col-span-2 bg-slate-900 text-slate-100 rounded-xl border border-slate-800 flex flex-col min-h-0 overflow-hidden shadow-sm">
          <div className="bg-slate-800/80 px-4 py-2.5 border-b border-slate-700 flex items-center justify-between text-xs font-mono">
            <span className="text-indigo-300 truncate">{selectedFile}</span>
            <button
              onClick={() => copyToClipboard(fileContents[selectedFile] || '')}
              className="text-slate-400 hover:text-white flex items-center space-x-1 transition-colors cursor-pointer"
            >
              <Copy className="w-3.5 h-3.5" />
              <span>Copy</span>
            </button>
          </div>

          <pre className="p-4 text-xs font-mono overflow-auto flex-1 text-slate-200 leading-relaxed select-text">
            <code>{fileContents[selectedFile] || '// Source file contents...'}</code>
          </pre>
        </div>
      </div>
    </div>
  );
};
