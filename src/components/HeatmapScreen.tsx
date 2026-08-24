import React, { useState } from 'react';
import { ArrowLeft, Map, ArrowRight, AlertTriangle, Truck, Layers } from 'lucide-react';

interface Props {
  onNavigateBack: () => void;
  onViewSectorDetails: () => void;
}

export const HeatmapScreen: React.FC<Props> = ({
  onNavigateBack,
  onViewSectorDetails,
}) => {
  const [activeTab, setActiveTab] = useState<'COMPLAINTS' | 'COLLECTIONS'>('COMPLAINTS');

  return (
    <div className="flex flex-col h-full bg-[#F8FAFC] text-slate-800 overflow-y-auto">
      {/* Header */}
      <div className="bg-white border-b border-slate-200 px-4 py-3 flex items-center space-x-2 sticky top-0 z-10">
        <button onClick={onNavigateBack} className="w-7 h-7 rounded-full bg-slate-100 flex items-center justify-center cursor-pointer hover:bg-slate-200 transition-colors">
          <ArrowLeft className="w-4 h-4 text-slate-700" />
        </button>
        <span className="font-bold text-sm text-slate-900 tracking-tight">
          Metropolitan Heatmap
        </span>
      </div>

      {/* Map Section */}
      <div className="h-60 bg-slate-100 border-b border-slate-200 relative flex flex-col justify-between p-3">
        {/* Toggle overlay */}
        <div className="flex space-x-1.5 z-10 bg-white/90 backdrop-blur-xs p-1 rounded-xl border border-slate-200/80 self-start shadow-xs">
          <button
            onClick={() => setActiveTab('COMPLAINTS')}
            className={`px-3 py-1 rounded-lg text-[10px] font-semibold transition-all ${
              activeTab === 'COMPLAINTS' ? 'bg-indigo-600 text-white shadow-xs' : 'text-slate-600 hover:text-slate-900'
            }`}
          >
            Complaints
          </button>
          <button
            onClick={() => setActiveTab('COLLECTIONS')}
            className={`px-3 py-1 rounded-lg text-[10px] font-semibold transition-all ${
              activeTab === 'COLLECTIONS' ? 'bg-indigo-600 text-white shadow-xs' : 'text-slate-600 hover:text-slate-900'
            }`}
          >
            Collections
          </button>
        </div>

        {/* Heatmap Graphic Center */}
        <div className="flex flex-col items-center justify-center my-auto">
          <div className="w-12 h-12 bg-indigo-50 rounded-2xl flex items-center justify-center mb-1 text-indigo-600">
            <Map className="w-6 h-6" />
          </div>
          <span className="text-[10px] font-semibold tracking-wider text-slate-500 uppercase">
            Live Density Layer Active
          </span>
        </div>

        {/* Density Legend */}
        <div className="self-end bg-white/95 backdrop-blur-xs border border-slate-200 rounded-xl p-2 text-[9px] font-semibold space-y-1 shadow-xs">
          <div className="text-slate-400 uppercase text-[8px]">Density</div>
          <div className="flex items-center space-x-1.5">
            <div className="w-2 h-2 rounded-full bg-rose-500"></div>
            <span className="text-slate-700">High</span>
          </div>
          <div className="flex items-center space-x-1.5">
            <div className="w-2 h-2 rounded-full bg-amber-500"></div>
            <span className="text-slate-700">Med</span>
          </div>
          <div className="flex items-center space-x-1.5">
            <div className="w-2 h-2 rounded-full bg-emerald-500"></div>
            <span className="text-slate-700">Low</span>
          </div>
        </div>
      </div>

      {/* Details Body */}
      <div className="p-4 space-y-4">
        {/* Top Hotspot Card */}
        <div className="bg-white border border-slate-200 rounded-2xl p-4 shadow-sm">
          <div className="flex items-start justify-between mb-2">
            <div>
              <div className="text-[10px] font-semibold text-rose-600 uppercase tracking-wider">
                Priority Hotspot
              </div>
              <div className="text-xl font-bold text-slate-900">
                Sector 7 • East District
              </div>
            </div>
            <span className="bg-rose-50 text-rose-700 text-[10px] font-bold px-2 py-0.5 rounded-full">
              Action Required
            </span>
          </div>

          <div className="border-t border-slate-100 my-3"></div>

          <div className="flex items-center justify-between">
            <div>
              <div className="text-2xl font-extrabold text-slate-900">
                42
              </div>
              <div className="text-[10px] text-slate-400 font-medium">
                Pending Incidents Logged
              </div>
            </div>

            <button
              onClick={onViewSectorDetails}
              className="px-3 py-1.5 bg-slate-900 hover:bg-slate-800 text-white rounded-xl text-xs font-semibold flex items-center space-x-1 transition-colors cursor-pointer"
            >
              <span>View Queue</span>
              <ArrowRight className="w-3.5 h-3.5" />
            </button>
          </div>
        </div>

        {/* Bento Stats */}
        <div className="grid grid-cols-2 gap-3 pb-4">
          <div className="bg-white border border-slate-200 rounded-2xl p-4 shadow-sm">
            <AlertTriangle className="w-5 h-5 text-amber-500 mb-2" />
            <div className="text-xl font-bold text-slate-900">
              +15%
            </div>
            <div className="text-[10px] text-slate-400 font-medium uppercase tracking-wider">
              vs Previous Week
            </div>
          </div>

          <div className="bg-white border border-slate-200 rounded-2xl p-4 shadow-sm">
            <Truck className="w-5 h-5 text-indigo-600 mb-2" />
            <div className="text-xl font-bold text-slate-900">
              12 Units
            </div>
            <div className="text-[10px] text-slate-400 font-medium uppercase tracking-wider">
              Active in Field
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
