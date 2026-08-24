import React, { useState } from 'react';
import { Reward, User } from '../types';
import { ArrowLeft, Gift, Check, Sparkles, CheckCircle2 } from 'lucide-react';

interface Props {
  user: User;
  rewards: Reward[];
  onRedeemReward: (reward: Reward) => void;
  onNavigateBack: () => void;
}

export const RewardsScreen: React.FC<Props> = ({
  user,
  rewards,
  onRedeemReward,
  onNavigateBack,
}) => {
  const [successMsg, setSuccessMsg] = useState<string | null>(null);

  const handleRedeem = (reward: Reward) => {
    if (user.balancePoints >= reward.costPoints) {
      onRedeemReward(reward);
      setSuccessMsg(`Redeemed ${reward.title}! Voucher voucher code sent.`);
      setTimeout(() => setSuccessMsg(null), 4000);
    }
  };

  return (
    <div className="flex flex-col h-full bg-[#F8FAFC] text-slate-800 p-4 overflow-y-auto">
      <div className="flex items-center space-x-2 mb-3">
        <button onClick={onNavigateBack} className="w-8 h-8 rounded-full bg-white border border-slate-200 flex items-center justify-center cursor-pointer hover:bg-slate-50 transition-colors">
          <ArrowLeft className="w-4 h-4 text-slate-700" />
        </button>
        <h1 className="text-sm font-bold text-slate-900">
          Eco Rewards Store
        </h1>
      </div>

      {/* Balance Box */}
      <div className="bg-gradient-to-br from-indigo-600 to-indigo-700 rounded-2xl p-4 text-white shadow-sm shadow-indigo-100 mb-4 flex items-center justify-between">
        <div>
          <div className="text-[10px] font-semibold text-indigo-200 uppercase tracking-wider">
            Available Balance
          </div>
          <div className="text-2xl font-bold">
            {user.balancePoints.toLocaleString()} <span className="text-sm font-normal text-indigo-200">PTS</span>
          </div>
        </div>
        <div className="w-10 h-10 bg-white/10 rounded-xl flex items-center justify-center">
          <Gift className="w-5 h-5 text-white" />
        </div>
      </div>

      {successMsg && (
        <div className="bg-emerald-50 border border-emerald-200 rounded-xl p-3 mb-4 flex items-center space-x-2 text-emerald-800 text-xs">
          <CheckCircle2 className="w-4 h-4 text-emerald-600 shrink-0" />
          <span className="font-medium">{successMsg}</span>
        </div>
      )}

      <div className="flex items-center justify-between mb-3 px-1">
        <h2 className="text-xs font-bold uppercase tracking-wider text-slate-400">
          Available Perks
        </h2>
        <span className="text-[11px] text-slate-500 font-medium">Instant Digital Delivery</span>
      </div>

      <div className="space-y-3 pb-4">
        {rewards.map((reward) => {
          const canAfford = user.balancePoints >= reward.costPoints;
          return (
            <div
              key={reward.id}
              className="bg-white border border-slate-200 rounded-2xl p-4 shadow-sm"
            >
              <div className="flex items-center justify-between mb-2">
                <span className="bg-slate-100 text-slate-600 text-[10px] font-semibold px-2 py-0.5 rounded-full">
                  {reward.category}
                </span>
                <span
                  className={`text-[11px] font-bold px-2 py-0.5 rounded-full ${
                    canAfford ? 'bg-emerald-50 text-emerald-700' : 'bg-slate-100 text-slate-400'
                  }`}
                >
                  {reward.costPoints} PTS
                </span>
              </div>

              <h3 className="text-sm font-bold text-slate-900 mb-1">
                {reward.title}
              </h3>
              <p className="text-xs text-slate-500 mb-3 leading-relaxed">
                {reward.description}
              </p>

              <button
                onClick={() => handleRedeem(reward)}
                disabled={!canAfford}
                className={`w-full py-2 rounded-xl text-xs font-semibold transition-all ${
                  canAfford
                    ? 'bg-indigo-600 hover:bg-indigo-700 text-white shadow-xs cursor-pointer'
                    : 'bg-slate-100 text-slate-400 cursor-not-allowed'
                }`}
              >
                {canAfford ? 'Redeem Voucher' : 'Insufficient Points'}
              </button>
            </div>
          );
        })}
      </div>
    </div>
  );
};
