import React, { useState } from 'react';
import {
  Complaint,
  Employee,
  ScreenId,
  ServiceTask,
  User,
  UserRole,
  RewardTransaction,
  ElectricityProvider,
  PointsConversionConfig,
  BillPaymentReceipt,
  ElectricityBill,
} from './types';
import {
  initialComplaints,
  initialEmployees,
  initialTasks,
  initialUser,
  initialTransactions,
  initialElectricityProviders,
  initialConversionConfig,
  initialPaymentReceipts,
} from './data/mockData';

// Screens
import { RoleSelectionScreen } from './components/RoleSelectionScreen';
import { AuthScreen } from './components/AuthScreen';
import { CitizenDashboardScreen } from './components/CitizenDashboardScreen';
import { LiveTrackingScreen } from './components/LiveTrackingScreen';
import { ElectricityBillScreen } from './components/ElectricityBillScreen';
import { RewardsScreen } from './components/RewardsScreen';
import { EmployeeDashboardScreen } from './components/EmployeeDashboardScreen';
import { LogWasteScreen } from './components/LogWasteScreen';
import { HeatmapScreen } from './components/HeatmapScreen';
import { AnalyticsScreen } from './components/AnalyticsScreen';
import { RosterScreen } from './components/RosterScreen';
import { ComplaintsScreen } from './components/ComplaintsScreen';
import { AndroidProjectExplorer } from './components/AndroidProjectExplorer';

// Icons
import {
  Smartphone,
  Code2,
  Download,
  RotateCcw,
  Layers,
  Zap,
} from 'lucide-react';

export function App() {
  const [activeTab, setActiveTab] = useState<'simulator' | 'project'>('simulator');
  const [currentScreen, setCurrentScreen] = useState<ScreenId>('role_selection');
  const [user, setUser] = useState<User>(initialUser);
  const [tasks, setTasks] = useState<ServiceTask[]>(initialTasks);
  const [complaints, setComplaints] = useState<Complaint[]>(initialComplaints);
  const [employees, setEmployees] = useState<Employee[]>(initialEmployees);
  const [transactions, setTransactions] = useState<RewardTransaction[]>(initialTransactions);
  const [conversionConfig, setConversionConfig] = useState<PointsConversionConfig>(initialConversionConfig);
  const [providers] = useState<ElectricityProvider[]>(initialElectricityProviders);
  const [paymentHistory, setPaymentHistory] = useState<BillPaymentReceipt[]>(initialPaymentReceipts);

  // Handlers for state updates
  const handleRoleSelect = (role: UserRole) => {
    setUser((prev) => ({ ...prev, role }));
  };

  const handleLogWaste = (zone: string, type: string, weightKg: number, points: number) => {
    const newTx: RewardTransaction = {
      id: `tx-${Date.now()}`,
      type: 'EARN',
      points: points,
      description: `Recycled ${weightKg}kg of ${type} in ${zone}`,
      date: new Date().toISOString().replace('T', ' ').slice(0, 16),
      referenceType: 'WASTE_LOG',
      referenceId: `WL-${Math.floor(1000 + Math.random() * 9000)}`,
    };

    setUser((prev) => ({
      ...prev,
      balancePoints: prev.balancePoints + points,
      recycledKgYtd: +(prev.recycledKgYtd + weightKg).toFixed(1),
    }));

    setTransactions((prev) => [newTx, ...prev]);
  };

  const handleFetchBill = (providerId: string, consumerNumber: string): ElectricityBill => {
    const prov = providers.find((p) => p.id === providerId) || providers[0];
    const hash = Math.abs(
      consumerNumber.split('').reduce((acc, c) => (acc << 5) - acc + c.charCodeAt(0), 0)
    );
    const amount = 450.0 + (hash % 1150);

    return {
      id: `eb-${Date.now()}`,
      providerId: prov.id,
      providerName: prov.name,
      consumerNumber: consumerNumber,
      consumerName: user.name,
      billNumber: `BILL-AUG-${10000 + (hash % 90000)}`,
      billingMonth: 'August 2026',
      dueDate: '2026-09-10',
      billAmount: Math.round(amount * 100) / 100,
      status: 'UNPAID',
    };
  };

  const handlePayBill = (
    providerId: string,
    consumerNumber: string,
    billNumber: string,
    totalAmount: number,
    pointsToRedeem: number,
    onResult: (res: { success: boolean; receipt?: BillPaymentReceipt; error?: string }) => void
  ) => {
    // 1. Server-side validation simulation
    if (pointsToRedeem < 0) {
      onResult({ success: false, error: 'Points to redeem cannot be negative.' });
      return;
    }
    if (pointsToRedeem > user.balancePoints) {
      onResult({
        success: false,
        error: `Insufficient points. Available: ${user.balancePoints} PTS, requested: ${pointsToRedeem} PTS.`,
      });
      return;
    }

    const discountAmount = pointsToRedeem / conversionConfig.pointsPerUnit;
    if (discountAmount > totalAmount) {
      onResult({
        success: false,
        error: 'Points discount cannot exceed the total bill amount.',
      });
      return;
    }

    const amountPaid = Math.max(0, totalAmount - discountAmount);
    const newBalance = user.balancePoints - pointsToRedeem;
    const prov = providers.find((p) => p.id === providerId) || providers[0];
    const txnRef = `TXN_ELEC_${Date.now()}`;
    const timestamp = new Date().toISOString().replace('T', ' ').slice(0, 16);

    const receipt: BillPaymentReceipt = {
      paymentId: `PAY-${Math.floor(1000 + Math.random() * 9000)}`,
      billNumber: billNumber,
      consumerNumber: consumerNumber,
      providerName: prov.name,
      totalBillAmount: totalAmount,
      pointsRedeemed: pointsToRedeem,
      pointsDiscountAmount: discountAmount,
      amountPaid: amountPaid,
      transactionRef: txnRef,
      timestamp: timestamp,
      updatedWalletBalance: newBalance,
      status: 'PAID & RECORDED',
    };

    // Atomic updates
    setUser((prev) => ({
      ...prev,
      balancePoints: newBalance,
    }));

    if (pointsToRedeem > 0) {
      const newTx: RewardTransaction = {
        id: `tx-${Date.now()}`,
        type: 'REDEEM',
        points: pointsToRedeem,
        description: `Electricity Bill Credit - ${prov.code} (Cons. #${consumerNumber})`,
        date: timestamp,
        referenceType: 'ELECTRICITY_BILL',
        referenceId: receipt.paymentId,
      };
      setTransactions((prev) => [newTx, ...prev]);
    }

    setPaymentHistory((prev) => [receipt, ...prev]);

    onResult({ success: true, receipt });
  };

  const handleCompleteTask = (taskId: string) => {
    setTasks((prev) =>
      prev.map((t) => (t.id === taskId ? { ...t, status: 'COMPLETED', progress: 100 } : t))
    );
  };

  const handleStartTask = (taskId: string) => {
    setTasks((prev) =>
      prev.map((t) => (t.id === taskId ? { ...t, status: 'IN_PROGRESS', progress: 30 } : t))
    );
  };

  const handleAcceptTask = (taskId: string) => {
    setTasks((prev) =>
      prev.map((t) => (t.id === taskId ? { ...t, status: 'IN_PROGRESS', progress: 15 } : t))
    );
  };

  const handleResolveComplaint = (complaintId: string) => {
    setComplaints((prev) =>
      prev.map((c) => (c.id === complaintId ? { ...c, status: 'RESOLVED' } : c))
    );
  };

  return (
    <div className="min-h-screen bg-[#F8FAFC] text-[#1E293B] flex flex-col font-sans antialiased">
      {/* Clean Minimalist Top Navbar */}
      <header className="h-16 bg-white border-b border-slate-200 sticky top-0 z-30 px-4 sm:px-6 flex items-center justify-between">
        <div className="max-w-7xl w-full mx-auto flex items-center justify-between">
          <div className="flex items-center gap-3">
            <div className="w-8 h-8 bg-indigo-600 rounded-lg flex items-center justify-center shadow-sm shadow-indigo-200">
              <div className="w-4 h-4 border-2 border-white rotate-45"></div>
            </div>
            <div>
              <div className="flex items-center gap-2">
                <span className="font-bold text-base sm:text-lg text-slate-900 tracking-tight">
                  WasteFlow <span className="text-slate-400 font-normal text-sm">v2.5.0</span>
                </span>
                <span className="hidden sm:inline-flex items-center gap-1.5 px-2.5 py-0.5 bg-emerald-50 text-emerald-700 rounded-full text-xs font-semibold">
                  <span className="w-1.5 h-1.5 bg-emerald-500 rounded-full"></span>
                  Native Android + FastAPI
                </span>
              </div>
            </div>
          </div>

          <div className="flex items-center gap-2 sm:gap-3">
            <div className="flex bg-slate-100 p-1 rounded-lg border border-slate-200/60">
              <button
                onClick={() => setActiveTab('simulator')}
                className={`px-3 py-1.5 rounded-md text-xs font-medium transition-all flex items-center gap-1.5 ${
                  activeTab === 'simulator'
                    ? 'bg-white text-slate-900 shadow-sm font-semibold'
                    : 'text-slate-600 hover:text-slate-900'
                }`}
              >
                <Smartphone className="w-3.5 h-3.5" />
                <span>App Simulator</span>
              </button>

              <button
                onClick={() => setActiveTab('project')}
                className={`px-3 py-1.5 rounded-md text-xs font-medium transition-all flex items-center gap-1.5 ${
                  activeTab === 'project'
                    ? 'bg-white text-slate-900 shadow-sm font-semibold'
                    : 'text-slate-600 hover:text-slate-900'
                }`}
              >
                <Code2 className="w-3.5 h-3.5" />
                <span>Kotlin Source</span>
              </button>
            </div>

            <a
              href="/wasteflow-android-project.zip"
              download="wasteflow-android-project.zip"
              className="bg-slate-900 text-white px-3.5 py-1.5 sm:px-4 sm:py-2 rounded-lg text-xs sm:text-sm font-medium hover:bg-slate-800 transition-colors flex items-center gap-1.5 shadow-sm"
            >
              <Download className="w-4 h-4" />
              <span className="hidden md:inline">Export ZIP</span>
            </a>
          </div>
        </div>
      </header>

      {/* Main Content Area */}
      <main className="flex-1 max-w-7xl w-full mx-auto p-4 sm:p-6 flex flex-col justify-center">
        {activeTab === 'project' ? (
          <div className="h-[760px]">
            <AndroidProjectExplorer />
          </div>
        ) : (
          <div className="grid grid-cols-1 lg:grid-cols-12 gap-8 items-start">
            {/* Screen Direct Jump Controls (Left Panel) */}
            <div className="lg:col-span-4 space-y-5">
              <div className="bg-white border border-slate-200 rounded-2xl p-5 shadow-sm">
                <div className="flex items-center justify-between mb-4 pb-3 border-b border-slate-100">
                  <div className="flex items-center gap-2">
                    <Layers className="w-4 h-4 text-indigo-600" />
                    <span className="text-xs font-bold uppercase tracking-wider text-slate-500">
                      Screen Explorer
                    </span>
                  </div>
                  <button
                    onClick={() => setCurrentScreen('role_selection')}
                    className="text-xs text-indigo-600 hover:text-indigo-800 font-medium flex items-center gap-1 transition-colors"
                  >
                    <RotateCcw className="w-3 h-3" />
                    <span>Reset</span>
                  </button>
                </div>

                <div className="space-y-4">
                  <div>
                    <span className="text-[11px] font-semibold text-slate-400 uppercase tracking-wider block mb-2">
                      Citizen Flow
                    </span>
                    <div className="grid grid-cols-2 gap-1.5">
                      <button
                        onClick={() => setCurrentScreen('role_selection')}
                        className={`p-2.5 rounded-lg text-xs font-medium text-left truncate transition-colors border ${
                          currentScreen === 'role_selection'
                            ? 'bg-indigo-50 border-indigo-200 text-indigo-700 font-semibold'
                            : 'bg-slate-50/70 border-slate-200/70 text-slate-700 hover:bg-slate-100'
                        }`}
                      >
                        1. Splash / Role
                      </button>
                      <button
                        onClick={() => setCurrentScreen('auth')}
                        className={`p-2.5 rounded-lg text-xs font-medium text-left truncate transition-colors border ${
                          currentScreen === 'auth'
                            ? 'bg-indigo-50 border-indigo-200 text-indigo-700 font-semibold'
                            : 'bg-slate-50/70 border-slate-200/70 text-slate-700 hover:bg-slate-100'
                        }`}
                      >
                        2. Auth / Login
                      </button>
                      <button
                        onClick={() => setCurrentScreen('citizen_home')}
                        className={`p-2.5 rounded-lg text-xs font-medium text-left truncate transition-colors border ${
                          currentScreen === 'citizen_home'
                            ? 'bg-indigo-50 border-indigo-200 text-indigo-700 font-semibold'
                            : 'bg-slate-50/70 border-slate-200/70 text-slate-700 hover:bg-slate-100'
                        }`}
                      >
                        3. Citizen Home
                      </button>
                      <button
                        onClick={() => setCurrentScreen('live_tracking')}
                        className={`p-2.5 rounded-lg text-xs font-medium text-left truncate transition-colors border ${
                          currentScreen === 'live_tracking'
                            ? 'bg-indigo-50 border-indigo-200 text-indigo-700 font-semibold'
                            : 'bg-slate-50/70 border-slate-200/70 text-slate-700 hover:bg-slate-100'
                        }`}
                      >
                        4. Live Truck GPS
                      </button>
                      <button
                        onClick={() => setCurrentScreen('electricity_bill')}
                        className={`p-2.5 rounded-lg text-xs font-bold text-left truncate transition-colors border flex items-center gap-1 ${
                          currentScreen === 'electricity_bill'
                            ? 'bg-amber-50 border-amber-300 text-amber-900 font-extrabold'
                            : 'bg-amber-50/50 border-amber-200/60 text-amber-950 hover:bg-amber-100/60'
                        }`}
                      >
                        <Zap className="w-3 h-3 text-amber-600 fill-current shrink-0" />
                        <span>5. Pay Power Bill</span>
                      </button>
                      <button
                        onClick={() => setCurrentScreen('rewards')}
                        className={`p-2.5 rounded-lg text-xs font-medium text-left truncate transition-colors border ${
                          currentScreen === 'rewards'
                            ? 'bg-indigo-50 border-indigo-200 text-indigo-700 font-semibold'
                            : 'bg-slate-50/70 border-slate-200/70 text-slate-700 hover:bg-slate-100'
                        }`}
                      >
                        6. Points Wallet
                      </button>
                    </div>
                  </div>

                  <div>
                    <span className="text-[11px] font-semibold text-slate-400 uppercase tracking-wider block mb-2">
                      Service & Admin Flow
                    </span>
                    <div className="grid grid-cols-2 gap-1.5">
                      <button
                        onClick={() => setCurrentScreen('employee_dashboard')}
                        className={`p-2.5 rounded-lg text-xs font-medium text-left truncate transition-colors border ${
                          currentScreen === 'employee_dashboard'
                            ? 'bg-indigo-50 border-indigo-200 text-indigo-700 font-semibold'
                            : 'bg-slate-50/70 border-slate-200/70 text-slate-700 hover:bg-slate-100'
                        }`}
                      >
                        7. Admin Dashboard
                      </button>
                      <button
                        onClick={() => setCurrentScreen('log_waste')}
                        className={`p-2.5 rounded-lg text-xs font-medium text-left truncate transition-colors border ${
                          currentScreen === 'log_waste'
                            ? 'bg-indigo-50 border-indigo-200 text-indigo-700 font-semibold'
                            : 'bg-slate-50/70 border-slate-200/70 text-slate-700 hover:bg-slate-100'
                        }`}
                      >
                        8. Log Waste
                      </button>
                      <button
                        onClick={() => setCurrentScreen('heatmap')}
                        className={`p-2.5 rounded-lg text-xs font-medium text-left truncate transition-colors border ${
                          currentScreen === 'heatmap'
                            ? 'bg-indigo-50 border-indigo-200 text-indigo-700 font-semibold'
                            : 'bg-slate-50/70 border-slate-200/70 text-slate-700 hover:bg-slate-100'
                        }`}
                      >
                        9. City Heatmap
                      </button>
                      <button
                        onClick={() => setCurrentScreen('analytics')}
                        className={`p-2.5 rounded-lg text-xs font-medium text-left truncate transition-colors border ${
                          currentScreen === 'analytics'
                            ? 'bg-indigo-50 border-indigo-200 text-indigo-700 font-semibold'
                            : 'bg-slate-50/70 border-slate-200/70 text-slate-700 hover:bg-slate-100'
                        }`}
                      >
                        10. Analytics
                      </button>
                      <button
                        onClick={() => setCurrentScreen('roster')}
                        className={`p-2.5 rounded-lg text-xs font-medium text-left truncate transition-colors border ${
                          currentScreen === 'roster'
                            ? 'bg-indigo-50 border-indigo-200 text-indigo-700 font-semibold'
                            : 'bg-slate-50/70 border-slate-200/70 text-slate-700 hover:bg-slate-100'
                        }`}
                      >
                        11. Crew Roster
                      </button>
                      <button
                        onClick={() => setCurrentScreen('complaints')}
                        className={`p-2.5 rounded-lg text-xs font-medium text-left truncate transition-colors border ${
                          currentScreen === 'complaints'
                            ? 'bg-indigo-50 border-indigo-200 text-indigo-700 font-semibold'
                            : 'bg-slate-50/70 border-slate-200/70 text-slate-700 hover:bg-slate-100'
                        }`}
                      >
                        12. Complaints
                      </button>
                    </div>
                  </div>
                </div>
              </div>

              {/* State Summary Panel */}
              <div className="bg-white border border-slate-200 rounded-2xl p-5 shadow-sm hidden md:block">
                <div className="text-xs font-bold uppercase tracking-wider text-slate-400 mb-3">
                  Live Runtime Ledger & State
                </div>
                <div className="space-y-2.5 text-xs">
                  <div className="flex justify-between items-center py-1 border-b border-slate-100">
                    <span className="text-slate-500">Active Role</span>
                    <span className="font-semibold text-slate-800 bg-slate-100 px-2 py-0.5 rounded">{user.role}</span>
                  </div>
                  <div className="flex justify-between items-center py-1 border-b border-slate-100">
                    <span className="text-slate-500">Reward Wallet</span>
                    <span className="text-emerald-600 font-bold bg-emerald-50 px-2 py-0.5 rounded">
                      {user.balancePoints.toLocaleString()} PTS (≈ ₹{(user.balancePoints / 10).toFixed(2)})
                    </span>
                  </div>
                  <div className="flex justify-between items-center py-1 border-b border-slate-100">
                    <span className="text-slate-500">Electricity Payments</span>
                    <span className="font-semibold text-slate-800">{paymentHistory.length} Settled</span>
                  </div>
                  <div className="flex justify-between items-center py-1 border-b border-slate-100">
                    <span className="text-slate-500">Active Tasks</span>
                    <span className="font-semibold text-slate-800">{tasks.filter((t) => t.status !== 'COMPLETED').length}</span>
                  </div>
                  <div className="flex justify-between items-center py-1">
                    <span className="text-slate-500">Pending Complaints</span>
                    <span className="text-rose-600 font-semibold bg-rose-50 px-2 py-0.5 rounded">
                      {complaints.filter((c) => c.status !== 'RESOLVED').length}
                    </span>
                  </div>
                </div>
              </div>
            </div>

            {/* Android Device Frame Simulator (Center Column) */}
            <div className="lg:col-span-8 flex justify-center">
              <div className="relative">
                {/* Subtle Ambient Glow */}
                <div className="absolute -inset-2 bg-gradient-to-tr from-indigo-500/20 via-purple-500/10 to-emerald-500/20 rounded-[48px] blur-xl"></div>

                {/* Device Frame */}
                <div className="relative w-full max-w-[390px] h-[780px] bg-slate-900 p-3 rounded-[44px] border-4 border-slate-800 shadow-2xl flex flex-col">
                  {/* Dynamic Island / Speaker */}
                  <div className="absolute top-5 left-1/2 transform -translate-x-1/2 w-24 h-4 bg-black rounded-full z-30 flex items-center justify-center">
                    <div className="w-2.5 h-2.5 bg-slate-800 rounded-full"></div>
                  </div>

                  {/* Device Screen Body */}
                  <div className="w-full h-full bg-[#F8FAFC] rounded-[32px] overflow-hidden flex flex-col relative border border-slate-800/20">
                    {/* Status Bar */}
                    <div className="h-8 bg-white/90 backdrop-blur-sm px-6 flex items-center justify-between text-[11px] font-semibold font-mono text-slate-800 border-b border-slate-100 select-none z-20">
                      <span>09:41</span>
                      <div className="flex items-center space-x-1.5 text-[10px]">
                        <span>5G</span>
                        <span>100%</span>
                      </div>
                    </div>

                    {/* Active Screen Content */}
                    <div className="flex-1 overflow-hidden">
                      {currentScreen === 'role_selection' && (
                        <RoleSelectionScreen
                          selectedRole={user.role}
                          onSelectRole={handleRoleSelect}
                          onGetStarted={() => setCurrentScreen('auth')}
                        />
                      )}
                      {currentScreen === 'auth' && (
                        <AuthScreen
                          onLoginSuccess={() => {
                            if (user.role === 'CITIZEN') {
                              setCurrentScreen('citizen_home');
                            } else {
                              setCurrentScreen('employee_dashboard');
                            }
                          }}
                        />
                      )}
                      {currentScreen === 'citizen_home' && (
                        <CitizenDashboardScreen
                          user={user}
                          onNavigateLiveRoute={() => setCurrentScreen('live_tracking')}
                          onNavigateReportIssue={() => setCurrentScreen('complaints')}
                          onNavigateElectricityBill={() => setCurrentScreen('electricity_bill')}
                          onNavigateRedeemPoints={() => setCurrentScreen('rewards')}
                          onRoleSwitchClick={() => setCurrentScreen('role_selection')}
                        />
                      )}
                      {currentScreen === 'live_tracking' && (
                        <LiveTrackingScreen
                          onNavigateBack={() => setCurrentScreen('citizen_home')}
                          onReportIssue={() => setCurrentScreen('complaints')}
                        />
                      )}
                      {currentScreen === 'electricity_bill' && (
                        <ElectricityBillScreen
                          user={user}
                          providers={providers}
                          conversionConfig={conversionConfig}
                          paymentHistory={paymentHistory}
                          onFetchBill={handleFetchBill}
                          onPayBill={handlePayBill}
                          onNavigateBack={() => setCurrentScreen('citizen_home')}
                        />
                      )}
                      {currentScreen === 'rewards' && (
                        <RewardsScreen
                          user={user}
                          transactions={transactions}
                          conversionConfig={conversionConfig}
                          onNavigateElectricityBill={() => setCurrentScreen('electricity_bill')}
                          onNavigateBack={() => setCurrentScreen('citizen_home')}
                        />
                      )}
                      {currentScreen === 'employee_dashboard' && (
                        <EmployeeDashboardScreen
                          tasks={tasks}
                          onAcceptTask={handleAcceptTask}
                          onStartTask={handleStartTask}
                          onCompleteTask={handleCompleteTask}
                          onNavigateBack={() => setCurrentScreen('role_selection')}
                          onNavigateLogWaste={() => setCurrentScreen('log_waste')}
                          onNavigateHeatmap={() => setCurrentScreen('heatmap')}
                          onNavigateAnalytics={() => setCurrentScreen('analytics')}
                          onNavigateRoster={() => setCurrentScreen('roster')}
                          onNavigateComplaints={() => setCurrentScreen('complaints')}
                        />
                      )}
                      {currentScreen === 'log_waste' && (
                        <LogWasteScreen
                          onLogSubmitted={handleLogWaste}
                          onNavigateBack={() => setCurrentScreen('employee_dashboard')}
                        />
                      )}
                      {currentScreen === 'heatmap' && (
                        <HeatmapScreen
                          onNavigateBack={() => setCurrentScreen('employee_dashboard')}
                          onViewSectorDetails={() => setCurrentScreen('complaints')}
                        />
                      )}
                      {currentScreen === 'analytics' && (
                        <AnalyticsScreen
                          onNavigateBack={() => setCurrentScreen('employee_dashboard')}
                        />
                      )}
                      {currentScreen === 'roster' && (
                        <RosterScreen
                          employees={employees}
                          onNavigateBack={() => setCurrentScreen('employee_dashboard')}
                        />
                      )}
                      {currentScreen === 'complaints' && (
                        <ComplaintsScreen
                          complaints={complaints}
                          onResolveComplaint={handleResolveComplaint}
                          onNavigateBack={() => {
                            if (user.role === 'CITIZEN') {
                              setCurrentScreen('citizen_home');
                            } else {
                              setCurrentScreen('employee_dashboard');
                            }
                          }}
                        />
                      )}
                    </div>

                    {/* Android Bottom Navigation Bar */}
                    <div className="h-5 bg-white flex items-center justify-center border-t border-slate-100">
                      <div className="w-24 h-1 bg-slate-300 rounded-full"></div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        )}
      </main>
    </div>
  );
}

export default App;
