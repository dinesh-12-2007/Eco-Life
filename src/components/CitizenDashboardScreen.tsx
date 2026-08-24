import React from 'react';
import { User } from '../types';
import { Trash2, User as UserIcon, Navigation, Calendar, ArrowRight, AlertTriangle, Gift, Award, Leaf, ChevronRight } from 'lucide-react';

interface Props {
  user: User;
  onNavigateLiveRoute: () => void;
  onNavigateReportIssue: () => void;
  onNavigateGiveAway: () => void;
  onNavigateRedeemPoints: () => void;
  onRoleSwitchClick: () => void;
}

export const CitizenDashboardScreen: React.FC<Props> = ({
  user,
  onNavigateLiveRoute,
  onNavigateReportIssue,
  onNavigateGiveAway,
  onNavigateRedeemPoints,
  onRoleSwitchClick,
}) => {
  return (
    <div className="flex flex-col h-full bg-[#F8FAFC] text-slate-800 overflow-y-auto">
      {/* Top Navbar */}
      <div className="bg-white/95 backdrop-blur-sm border-b border-slate-200/80 px-4 py-3 flex items-center justify-between sticky top-0 z-10">
        <div className="flex items-center space-x-2">
          <div className="w-7 h-7 bg-indigo-600 rounded-lg flex items-center justify-center text-white">
            <Trash2 className="w-4 h-4" />
          </div>
          <span className="font-bold text-sm text-slate-900 tracking-tight">
            WasteFlow
          </span>
        </div>

        <div className="flex items-center space-x-2">
          <span className="px-2 py-0.5 bg-slate-100 text-slate-600 rounded-full text-[10px] font-medium">
            Citizen
          </span>
          <button
            onClick={onRoleSwitchClick}
            className="w-7 h-7 rounded-full bg-slate-100 hover:bg-slate-200 border border-slate-200 flex items-center justify-center cursor-pointer text-slate-700 transition-colors"
          >
            <UserIcon className="w-3.5 h-3.5" />
          </button>
        </div>
      </div>

      <div className="p-4 space-y-4">
        {/* Hey Alex Header + Points Card */}
        <div className="bg-gradient-to-br from-indigo-600 to-indigo-700 rounded-2xl p-5 text-white shadow-sm shadow-indigo-100 relative overflow-hidden">
          <div className="absolute right-0 top-0 translate-x-4 -translate-y-4 w-32 h-32 bg-white/10 rounded-full blur-2xl pointer-events-none"></div>
          
          <div className="flex items-center justify-between mb-3">
            <div className="flex items-center space-x-2">
              <Leaf className="w-4 h-4 text-emerald-300" />
              <span className="text-xs font-medium text-indigo-100">
                Welcome back, {user.name.split(' ')[0]}
              </span>
            </div>
            <span className="text-[10px] uppercase font-semibold tracking-wider bg-white/15 px-2 py-0.5 rounded-full text-indigo-100">
              Active Tier
            </span>
          </div>

          <div className="flex items-baseline justify-between">
            <div>
              <div className="text-3xl font-extrabold tracking-tight">
                {user.balancePoints.toLocaleString()}
              </div>
              <div className="text-xs text-indigo-200 font-medium">Eco Reward Points</div>
            </div>
            <button
              onClick={onNavigateRedeemPoints}
              className="bg-white text-indigo-600 hover:bg-indigo-50 px-3 py-1.5 rounded-xl text-xs font-semibold shadow-xs transition-colors cursor-pointer"
            >
              Redeem
            </button>
          </div>
        </div>

        {/* Live Route Card */}
        <div className="bg-white border border-slate-200 rounded-2xl p-4 shadow-sm">
          <div className="flex items-center justify-between mb-3">
            <div className="flex items-center space-x-2">
              <span className="w-2 h-2 bg-emerald-500 rounded-full animate-ping"></span>
              <h2 className="text-xs font-bold uppercase tracking-wider text-slate-500">
                Live Route
              </h2>
            </div>
            <span className="bg-emerald-50 text-emerald-700 text-[10px] font-semibold px-2 py-0.5 rounded-full">
              Truck #402 En Route
            </span>
          </div>

          <div
            onClick={onNavigateLiveRoute}
            className="cursor-pointer group"
          >
            {/* Minimal Map Preview */}
            <div className="h-28 bg-slate-50 border border-slate-100 rounded-xl relative flex items-center justify-center overflow-hidden mb-3">
              <div className="absolute inset-0 opacity-30 bg-[radial-gradient(#94A3B8_1px,transparent_1px)] [background-size:12px_12px]"></div>
              <div className="relative z-10 flex flex-col items-center">
                <div className="w-8 h-8 bg-indigo-600 text-white rounded-full flex items-center justify-center shadow-md shadow-indigo-200">
                  <Navigation className="w-4 h-4 transform rotate-45" />
                </div>
                <span className="text-[10px] font-medium text-slate-600 mt-1.5 bg-white px-2 py-0.5 rounded-full shadow-xs border border-slate-100">
                  Downtown / Zone B Corridor
                </span>
              </div>
            </div>

            <div className="flex items-center justify-between text-xs">
              <div>
                <span className="text-slate-500">Estimated Arrival: </span>
                <strong className="text-slate-900 font-semibold">14 mins</strong>
              </div>
              <span className="text-indigo-600 font-medium group-hover:translate-x-0.5 transition-transform flex items-center gap-1">
                <span>View GPS</span>
                <ChevronRight className="w-3.5 h-3.5" />
              </span>
            </div>
          </div>
        </div>

        {/* 2-Column Bento: Impact & Next Pickup */}
        <div className="grid grid-cols-2 gap-3">
          {/* Impact */}
          <div className="bg-white border border-slate-200 rounded-2xl p-4 shadow-sm flex flex-col justify-between">
            <div>
              <div className="flex items-center justify-between text-[11px] font-semibold text-slate-400 uppercase tracking-wider mb-2">
                <span>Impact YTD</span>
                <Leaf className="w-3.5 h-3.5 text-emerald-500" />
              </div>
              <div className="text-2xl font-bold text-slate-900 mb-0.5">
                {user.recycledKgYtd}kg
              </div>
              <div className="text-[10px] font-medium text-emerald-600">
                +12% vs last month
              </div>
            </div>

            <div className="flex items-end space-x-1 h-6 mt-3">
              <div className="flex-1 bg-slate-100 rounded-t h-[40%]"></div>
              <div className="flex-1 bg-slate-100 rounded-t h-[65%]"></div>
              <div className="flex-1 bg-slate-100 rounded-t h-[30%]"></div>
              <div className="flex-1 bg-indigo-500 rounded-t h-[90%]"></div>
            </div>
          </div>

          {/* Next Pickup */}
          <div className="bg-white border border-slate-200 rounded-2xl p-4 shadow-sm flex flex-col justify-between">
            <div>
              <div className="flex items-center justify-between text-[11px] font-semibold text-slate-400 uppercase tracking-wider mb-2">
                <span>Next Pickup</span>
                <Calendar className="w-3.5 h-3.5 text-slate-400" />
              </div>
              <div className="text-2xl font-bold text-slate-900">
                Tue 14
              </div>
              <div className="text-xs text-slate-500">
                November • Morning
              </div>
            </div>

            <div className="flex space-x-1.5 mt-3">
              <span className="bg-slate-100 text-slate-600 text-[9px] font-semibold px-2 py-0.5 rounded-full">
                Plastic
              </span>
              <span className="bg-slate-100 text-slate-600 text-[9px] font-semibold px-2 py-0.5 rounded-full">
                Glass
              </span>
            </div>
          </div>
        </div>

        {/* Quick Actions Section */}
        <div className="space-y-2">
          <div className="text-[11px] font-semibold text-slate-400 uppercase tracking-wider px-1">
            Quick Actions
          </div>

          <div className="grid grid-cols-1 gap-2">
            <button
              onClick={onNavigateReportIssue}
              className="w-full bg-white border border-slate-200 rounded-xl p-3.5 flex items-center justify-between shadow-xs hover:border-slate-300 transition-all cursor-pointer"
            >
              <div className="flex items-center space-x-3">
                <div className="w-8 h-8 bg-rose-50 text-rose-600 rounded-lg flex items-center justify-center">
                  <AlertTriangle className="w-4 h-4" />
                </div>
                <div className="text-left">
                  <div className="text-xs font-semibold text-slate-900">Report Missed Pickup</div>
                  <div className="text-[10px] text-slate-400">Log an overflow or missed container</div>
                </div>
              </div>
              <ChevronRight className="w-4 h-4 text-slate-400" />
            </button>

            <button
              onClick={onNavigateGiveAway}
              className="w-full bg-white border border-slate-200 rounded-xl p-3.5 flex items-center justify-between shadow-xs hover:border-slate-300 transition-all cursor-pointer"
            >
              <div className="flex items-center space-x-3">
                <div className="w-8 h-8 bg-indigo-50 text-indigo-600 rounded-lg flex items-center justify-center">
                  <Gift className="w-4 h-4" />
                </div>
                <div className="text-left">
                  <div className="text-xs font-semibold text-slate-900">Give Away Reusable Item</div>
                  <div className="text-[10px] text-slate-400">Post items for community upcycling</div>
                </div>
              </div>
              <ChevronRight className="w-4 h-4 text-slate-400" />
            </button>

            <button
              onClick={onNavigateRedeemPoints}
              className="w-full bg-white border border-slate-200 rounded-xl p-3.5 flex items-center justify-between shadow-xs hover:border-slate-300 transition-all cursor-pointer"
            >
              <div className="flex items-center space-x-3">
                <div className="w-8 h-8 bg-emerald-50 text-emerald-600 rounded-lg flex items-center justify-center">
                  <Award className="w-4 h-4" />
                </div>
                <div className="text-left">
                  <div className="text-xs font-semibold text-slate-900">Eco Rewards Marketplace</div>
                  <div className="text-[10px] text-slate-400">Claim eco discounts & vouchers</div>
                </div>
              </div>
              <ChevronRight className="w-4 h-4 text-slate-400" />
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};
