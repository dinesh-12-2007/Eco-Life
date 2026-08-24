import React, { useState } from 'react';
import { ArrowLeft, Download, TrendingUp, CheckCircle2 } from 'lucide-react';

interface Props {
  onNavigateBack: () => void;
}

export const AnalyticsScreen: React.FC<Props> = ({ onNavigateBack }) => {
  const [timeframe, setTimeframe] = useState<'WEEKLY' | 'MONTHLY'>('MONTHLY');
  const [isExported, setIsExported] = useState(false);

  return (
    <div className="flex flex-col h-full bg-[#F8FAFC] text-slate-800 p-4 overflow-y-auto">
      {/* Header */}
      <div className="flex items-center space-x-2 mb-4">
        <button onClick={onNavigateBack} className="w-8 h-8 rounded-full bg-white border border-slate-200 flex items-center justify-center cursor-pointer hover:bg-slate-50 transition-colors">
          <ArrowLeft className="w-4 h-4 text-slate-700" />
        </button>
        <h1 className="text-sm font-bold text-slate-900">
          Municipal Analytics
        </h1>
      </div>

      <div className="space-y-4 pb-4">
        {/* Toggle */}
        <div className="flex bg-slate-100 p-1 rounded-xl border border-slate-200/60">
          <button
            onClick={() => setTimeframe('WEEKLY')}
            className={`flex-1 py-1.5 text-xs font-semibold rounded-lg transition-all ${
              timeframe === 'WEEKLY' ? 'bg-white text-slate-900 shadow-xs' : 'text-slate-500 hover:text-slate-900'
            }`}
          >
            Weekly
          </button>
          <button
            onClick={() => setTimeframe('MONTHLY')}
            className={`flex-1 py-1.5 text-xs font-semibold rounded-lg transition-all ${
              timeframe === 'MONTHLY' ? 'bg-white text-slate-900 shadow-xs' : 'text-slate-500 hover:text-slate-900'
            }`}
          >
            Monthly
          </button>
        </div>

        {/* Stats Row */}
        <div className="grid grid-cols-2 gap-3">
          <div className="bg-white border border-slate-200 rounded-2xl p-4 shadow-sm">
            <div className="text-[10px] font-semibold text-slate-400 uppercase tracking-wider">
              Total Collected
            </div>
            <div className="text-2xl font-bold text-slate-900 mt-1">
              14.2T
            </div>
          </div>

          <div className="bg-emerald-50 border border-emerald-200 rounded-2xl p-4 shadow-sm">
            <div className="text-[10px] font-semibold text-emerald-800 uppercase tracking-wider flex items-center gap-1">
              <span>MoM Growth</span>
              <TrendingUp className="w-3 h-3" />
            </div>
            <div className="text-2xl font-bold text-emerald-700 mt-1">
              +12.4%
            </div>
          </div>
        </div>

        {/* Waste Distribution */}
        <div className="bg-white border border-slate-200 rounded-2xl p-4 shadow-sm">
          <h2 className="text-xs font-bold uppercase tracking-wider text-slate-400 mb-3">
            Stream Breakdown
          </h2>

          {/* Minimal 5-Bar graphic */}
          <div className="h-28 bg-slate-50 rounded-xl p-3 flex items-end justify-between space-x-2 border border-slate-100">
            <div className="flex-1 flex flex-col items-center h-full justify-end">
              <div className="w-full bg-indigo-600 rounded-t-md h-[85%]"></div>
            </div>
            <div className="flex-1 flex flex-col items-center h-full justify-end">
              <div className="w-full bg-indigo-400 rounded-t-md h-[60%]"></div>
            </div>
            <div className="flex-1 flex flex-col items-center h-full justify-end">
              <div className="w-full bg-slate-300 rounded-t-md h-[40%]"></div>
            </div>
            <div className="flex-1 flex flex-col items-center h-full justify-end">
              <div className="w-full bg-emerald-500 rounded-t-md h-[70%]"></div>
            </div>
            <div className="flex-1 flex flex-col items-center h-full justify-end">
              <div className="w-full bg-amber-400 rounded-t-md h-[25%]"></div>
            </div>
          </div>

          <div className="flex justify-between text-[10px] font-medium text-slate-500 mt-2 px-1">
            <span>Plastic</span>
            <span>Paper</span>
            <span>Metal</span>
            <span>Organic</span>
            <span>Other</span>
          </div>
        </div>

        {/* Target Progress */}
        <div className="bg-white border border-slate-200 rounded-2xl p-4 shadow-sm">
          <div className="flex items-center justify-between mb-2">
            <h2 className="text-xs font-bold uppercase tracking-wider text-slate-400">
              City Diversion Target
            </h2>
            <span className="text-xs font-bold text-indigo-600">75% Achieved</span>
          </div>
          
          <div className="w-full h-3 bg-slate-100 rounded-full overflow-hidden">
            <div className="h-full bg-indigo-600 rounded-full w-[75%] transition-all"></div>
          </div>
          <p className="text-[11px] text-slate-400 mt-2">
            Target: 80% zero-landfill diversion by Q4 2026.
          </p>
        </div>

        {/* Export button */}
        <button
          onClick={() => {
            setIsExported(true);
            setTimeout(() => setIsExported(false), 3000);
          }}
          className="w-full py-2.5 bg-slate-900 hover:bg-slate-800 text-white font-medium text-xs rounded-xl shadow-sm transition-all flex items-center justify-center space-x-2 cursor-pointer"
        >
          {isExported ? <CheckCircle2 className="w-4 h-4 text-emerald-400" /> : <Download className="w-4 h-4" />}
          <span>{isExported ? 'Ledger Exported (CSV)' : 'Export CSV Dataset'}</span>
        </button>
      </div>
    </div>
  );
};
