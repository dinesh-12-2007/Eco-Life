import React, { useState } from 'react';
import { Complaint } from '../types';
import { ArrowLeft, MapPin, Check, AlertCircle, CheckCircle2 } from 'lucide-react';

interface Props {
  complaints: Complaint[];
  onResolveComplaint: (id: string) => void;
  onNavigateBack: () => void;
}

export const ComplaintsScreen: React.FC<Props> = ({
  complaints,
  onResolveComplaint,
  onNavigateBack,
}) => {
  const [filter, setFilter] = useState<'ALL' | 'PENDING' | 'ASSIGNED' | 'RESOLVED'>('ALL');

  const filtered = complaints.filter((c) => {
    if (filter === 'ALL') return true;
    return c.status === filter;
  });

  return (
    <div className="flex flex-col h-full bg-[#F8FAFC] text-slate-800 p-4 overflow-y-auto">
      {/* Header */}
      <div className="flex items-center justify-between mb-3">
        <div className="flex items-center space-x-2">
          <button onClick={onNavigateBack} className="w-8 h-8 rounded-full bg-white border border-slate-200 flex items-center justify-center cursor-pointer hover:bg-slate-50 transition-colors">
            <ArrowLeft className="w-4 h-4 text-slate-700" />
          </button>
          <h1 className="text-sm font-bold text-slate-900">
            Citizen Issues & Alerts
          </h1>
        </div>

        <span className="bg-rose-50 text-rose-700 text-[10px] font-bold px-2 py-0.5 rounded-full">
          4 Urgent
        </span>
      </div>

      {/* Filter Tabs */}
      <div className="grid grid-cols-4 gap-1 bg-slate-100 p-1 rounded-xl border border-slate-200/60 mb-4">
        {(['ALL', 'PENDING', 'ASSIGNED', 'RESOLVED'] as const).map((f) => (
          <button
            key={f}
            onClick={() => setFilter(f)}
            className={`py-1.5 rounded-lg text-[10px] font-semibold transition-all ${
              filter === f ? 'bg-white text-slate-900 shadow-xs' : 'text-slate-500 hover:text-slate-900'
            }`}
          >
            {f}
          </button>
        ))}
      </div>

      <div className="space-y-3 pb-4">
        {filtered.map((complaint) => {
          const isHigh = complaint.priority === 'HIGH';
          const isResolved = complaint.status === 'RESOLVED';

          return (
            <div
              key={complaint.id}
              className={`bg-white border rounded-2xl p-4 shadow-sm transition-all ${
                isHigh ? 'border-rose-200 ring-1 ring-rose-100' : 'border-slate-200'
              }`}
            >
              <div className="flex items-center justify-between mb-2">
                <span className="text-[10px] font-semibold text-slate-400">
                  Case #{complaint.id}
                </span>
                <span
                  className={`text-[10px] font-bold px-2 py-0.5 rounded-full ${
                    isHigh ? 'bg-rose-50 text-rose-700' : 'bg-slate-100 text-slate-600'
                  }`}
                >
                  {complaint.priority} Priority
                </span>
              </div>

              <h2 className="text-sm font-bold text-slate-900 mb-1">
                {complaint.title}
              </h2>

              <p className="text-xs text-slate-500 mb-3 leading-relaxed">
                {complaint.description}
              </p>

              <div className="flex items-center space-x-1.5 text-xs text-slate-500 mb-3">
                <MapPin className="w-3.5 h-3.5 text-slate-400" />
                <span>{complaint.location}</span>
              </div>

              <div className="border-t border-slate-100 pt-3 flex items-center justify-between">
                <div>
                  <div className="text-[10px] font-semibold text-slate-400 uppercase tracking-wider">
                    Assigned Unit
                  </div>
                  <div className="text-xs font-bold text-slate-800">
                    {complaint.assignedCrew}
                  </div>
                </div>

                {!isResolved ? (
                  <button
                    onClick={() => onResolveComplaint(complaint.id)}
                    className={`px-3 py-1.5 rounded-xl text-xs font-semibold shadow-xs transition-colors cursor-pointer ${
                      isHigh
                        ? 'bg-rose-600 hover:bg-rose-700 text-white'
                        : 'bg-indigo-600 hover:bg-indigo-700 text-white'
                    }`}
                  >
                    Resolve Case
                  </button>
                ) : (
                  <span className="bg-emerald-50 text-emerald-700 text-[11px] font-semibold px-2.5 py-1 rounded-full flex items-center gap-1">
                    <Check className="w-3.5 h-3.5" />
                    <span>Resolved</span>
                  </span>
                )}
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
};
