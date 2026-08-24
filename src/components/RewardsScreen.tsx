import React, { useState } from 'react';
import { User, RewardTransaction, PointsConversionConfig } from '../types';
import { ArrowLeft, Wallet, Zap, Plus, Minus, ArrowRight } from 'lucide-react';

interface Props {
  user: User;
  transactions: RewardTransaction[];
  conversionConfig: PointsConversionConfig;
  onNavigateElectricityBill: () => void;
  onNavigateBack: () => void;
}

export const RewardsScreen: React.FC<Props> = ({
  user,
  transactions,
  conversionConfig,
  onNavigateElectricityBill,
  onNavigateBack,
}) => {
  const [filter, setFilter] = useState<'ALL' | 'EARN' | 'REDEEM'>('ALL');

  const totalEarned = transactions
    .filter((t) => t.type === 'EARN')
    .reduce((sum, t) => sum + t.points, 0);

  const totalUsed = transactions
    .filter((t) => t.type === 'REDEEM')
    .reduce((sum, t) => sum + t.points, 0);

  const filteredTransactions = transactions.filter((t) => {
    if (filter === 'EARN') return t.type === 'EARN';
    if (filter === 'REDEEM') return t.type === 'REDEEM';
    return true;
  });

  return (
    <div className="flex flex-col h-full bg-[#F8FAFC] text-slate-800 p-4 overflow-y-auto">
      {/* Header */}
      <div className="flex items-center space-x-3 mb-4">
        <button
          onClick={onNavigateBack}
          className="w-8 h-8 rounded-full bg-white border border-slate-200 flex items-center justify-center cursor-pointer hover:bg-slate-50 transition-colors text-slate-700"
        >
          <ArrowLeft className="w-4 h-4" />
        </button>
        <div>
          <h1 className="text-sm font-bold text-slate-900 leading-none">
            Reward Points Wallet
          </h1>
          <p className="text-[10px] text-slate-500 mt-0.5">
            Recycling credits & utility bill redemption ledger
          </p>
        </div>
      </div>

      {/* Main Wallet Balance Card */}
      <div className="bg-gradient-to-br from-indigo-900 via-indigo-800 to-indigo-950 rounded-2xl p-5 text-white shadow-md shadow-indigo-200 mb-4 relative overflow-hidden">
        <div className="absolute right-0 top-0 translate-x-6 -translate-y-6 w-36 h-36 bg-amber-400/20 rounded-full blur-2xl pointer-events-none"></div>

        <div className="flex items-center justify-between mb-3">
          <div className="flex items-center space-x-2">
            <Wallet className="w-4 h-4 text-indigo-300" />
            <span className="text-[10px] font-bold uppercase tracking-wider text-indigo-200">
              Active Wallet Balance
            </span>
          </div>
          <span className="bg-emerald-500/20 text-emerald-300 text-[10px] font-bold px-2.5 py-0.5 rounded-full border border-emerald-500/30">
            Live Account
          </span>
        </div>

        <div className="flex items-baseline justify-between">
          <div>
            <div className="text-3xl font-black tracking-tight">
              {user.balancePoints.toLocaleString()} <span className="text-sm font-bold text-indigo-300">PTS</span>
            </div>
            <div className="text-xs font-semibold text-emerald-400 mt-0.5">
              ≈ {conversionConfig.currencySymbol}{(user.balancePoints / conversionConfig.pointsPerUnit).toFixed(2)} Electricity Bill Discount
            </div>
          </div>
        </div>

        <div className="mt-4 pt-3 border-t border-indigo-700/60 flex items-center justify-between text-xs">
          <span className="text-indigo-200 text-[11px]">
            ⚡ Rate: {conversionConfig.description}
          </span>
        </div>
      </div>

      {/* Bento 2-Col Stats: Earned vs Used */}
      <div className="grid grid-cols-2 gap-3 mb-4">
        <div className="bg-white border border-slate-200 rounded-2xl p-3.5 shadow-xs">
          <div className="text-[10px] font-bold text-slate-400 uppercase tracking-wider mb-1">
            Total Earned
          </div>
          <div className="text-lg font-black text-emerald-600">
            +{totalEarned.toLocaleString()} PTS
          </div>
          <div className="text-[10px] text-slate-500 mt-0.5">Via verified waste drop-offs</div>
        </div>

        <div className="bg-white border border-slate-200 rounded-2xl p-3.5 shadow-xs">
          <div className="text-[10px] font-bold text-slate-400 uppercase tracking-wider mb-1">
            Total Redeemed
          </div>
          <div className="text-lg font-black text-slate-900">
            -{totalUsed.toLocaleString()} PTS
          </div>
          <div className="text-[10px] text-slate-500 mt-0.5">For electricity bill credits</div>
        </div>
      </div>

      {/* Main Action Banner: Pay Electricity Bill */}
      <div
        onClick={onNavigateElectricityBill}
        className="bg-gradient-to-r from-amber-500 to-amber-600 rounded-2xl p-4 text-slate-950 shadow-sm mb-5 cursor-pointer hover:shadow-md transition-all flex items-center justify-between group"
      >
        <div className="flex items-center space-x-3">
          <div className="w-10 h-10 rounded-xl bg-slate-950 text-amber-400 flex items-center justify-center font-bold">
            <Zap className="w-5 h-5 fill-current" />
          </div>
          <div>
            <div className="text-xs font-black uppercase tracking-wider">
              Pay Electricity Bill
            </div>
            <div className="text-[11px] font-medium text-amber-950">
              Apply up to {user.balancePoints} PTS for instant power credit
            </div>
          </div>
        </div>
        <div className="w-7 h-7 rounded-full bg-slate-950/10 flex items-center justify-center group-hover:translate-x-1 transition-transform">
          <ArrowRight className="w-4 h-4 text-slate-950" />
        </div>
      </div>

      {/* Transaction History Ledger */}
      <div className="flex items-center justify-between mb-3 px-1">
        <h2 className="text-xs font-bold uppercase tracking-wider text-slate-700">
          Transaction Ledger
        </h2>

        {/* Filter Pills */}
        <div className="flex bg-slate-200/70 p-0.5 rounded-lg text-[10px] font-bold">
          <button
            onClick={() => setFilter('ALL')}
            className={`px-2 py-0.5 rounded-md transition-all ${
              filter === 'ALL' ? 'bg-white text-slate-900 shadow-xs' : 'text-slate-500'
            }`}
          >
            All
          </button>
          <button
            onClick={() => setFilter('EARN')}
            className={`px-2 py-0.5 rounded-md transition-all ${
              filter === 'EARN' ? 'bg-white text-slate-900 shadow-xs' : 'text-slate-500'
            }`}
          >
            Earned
          </button>
          <button
            onClick={() => setFilter('REDEEM')}
            className={`px-2 py-0.5 rounded-md transition-all ${
              filter === 'REDEEM' ? 'bg-white text-slate-900 shadow-xs' : 'text-slate-500'
            }`}
          >
            Redeemed
          </button>
        </div>
      </div>

      <div className="space-y-2 pb-6">
        {filteredTransactions.length === 0 ? (
          <div className="bg-white border border-slate-200 rounded-2xl p-6 text-center text-xs text-slate-400">
            No transactions found in this filter.
          </div>
        ) : (
          filteredTransactions.map((tx) => {
            const isEarn = tx.type === 'EARN';
            return (
              <div
                key={tx.id}
                className="bg-white border border-slate-200 rounded-2xl p-3.5 shadow-xs flex items-center justify-between"
              >
                <div className="flex items-center space-x-3">
                  <div
                    className={`w-8 h-8 rounded-xl flex items-center justify-center ${
                      isEarn ? 'bg-emerald-50 text-emerald-600' : 'bg-amber-50 text-amber-600'
                    }`}
                  >
                    {isEarn ? <Plus className="w-4 h-4" /> : <Minus className="w-4 h-4" />}
                  </div>
                  <div>
                    <div className="text-xs font-bold text-slate-900 leading-tight">
                      {tx.description}
                    </div>
                    <div className="text-[10px] text-slate-400 mt-0.5 font-mono">
                      {tx.date} {tx.referenceId && `• Ref: ${tx.referenceId}`}
                    </div>
                  </div>
                </div>

                <div
                  className={`text-xs font-black ${
                    isEarn ? 'text-emerald-600' : 'text-slate-900'
                  }`}
                >
                  {isEarn ? `+${tx.points}` : `-${tx.points}`} PTS
                </div>
              </div>
            );
          })
        )}
      </div>
    </div>
  );
};
