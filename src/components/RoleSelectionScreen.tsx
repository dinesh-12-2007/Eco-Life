import React from 'react';
import { UserRole } from '../types';
import { Leaf, Truck, Settings, Sparkles, CheckCircle2 } from 'lucide-react';

interface Props {
  selectedRole: UserRole;
  onSelectRole: (role: UserRole) => void;
  onGetStarted: () => void;
}

export const RoleSelectionScreen: React.FC<Props> = ({
  selectedRole,
  onSelectRole,
  onGetStarted,
}) => {
  return (
    <div className="flex flex-col h-full bg-[#F8FAFC] text-slate-800 p-5 justify-between select-none">
      <div className="flex flex-col items-center pt-3">
        {/* Modern App Icon */}
        <div className="w-20 h-20 bg-indigo-600 rounded-2xl flex items-center justify-center mb-4 shadow-lg shadow-indigo-200">
          <Leaf className="w-10 h-10 text-white" strokeWidth={2} />
        </div>

        <h1 className="text-2xl font-bold tracking-tight text-slate-900 mb-1">
          EcoCycle
        </h1>

        <p className="text-xs text-slate-500 text-center max-w-xs mb-6">
          Intelligent circular waste tracking & municipal recycling grid
        </p>

        {/* Role 1: Citizen User */}
        <div
          onClick={() => onSelectRole('CITIZEN')}
          className={`w-full p-4 mb-3 rounded-2xl border transition-all cursor-pointer ${
            selectedRole === 'CITIZEN'
              ? 'bg-indigo-50/80 border-indigo-500 shadow-sm ring-1 ring-indigo-500/30'
              : 'bg-white border-slate-200 hover:border-slate-300 shadow-xs'
          }`}
        >
          <div className="flex items-center justify-between mb-1">
            <div className="flex items-center gap-3">
              <div className={`w-8 h-8 rounded-lg flex items-center justify-center ${
                selectedRole === 'CITIZEN' ? 'bg-indigo-600 text-white' : 'bg-slate-100 text-slate-600'
              }`}>
                <Leaf className="w-4 h-4" />
              </div>
              <h2 className="text-sm font-semibold text-slate-900">Citizen User</h2>
            </div>
            {selectedRole === 'CITIZEN' && (
              <CheckCircle2 className="w-4 h-4 text-indigo-600" />
            )}
          </div>
          <p className="text-xs text-slate-500 pl-11">
            Track daily recycling, claim karma rewards & monitor pickups.
          </p>
        </div>

        {/* Role 2: Service Emp. */}
        <div
          onClick={() => onSelectRole('SERVICE_EMPLOYEE')}
          className={`w-full p-4 mb-3 rounded-2xl border transition-all cursor-pointer ${
            selectedRole === 'SERVICE_EMPLOYEE'
              ? 'bg-indigo-50/80 border-indigo-500 shadow-sm ring-1 ring-indigo-500/30'
              : 'bg-white border-slate-200 hover:border-slate-300 shadow-xs'
          }`}
        >
          <div className="flex items-center justify-between mb-1">
            <div className="flex items-center gap-3">
              <div className={`w-8 h-8 rounded-lg flex items-center justify-center ${
                selectedRole === 'SERVICE_EMPLOYEE' ? 'bg-indigo-600 text-white' : 'bg-slate-100 text-slate-600'
              }`}>
                <Truck className="w-4 h-4" />
              </div>
              <h2 className="text-sm font-semibold text-slate-900">Service Operator</h2>
            </div>
            {selectedRole === 'SERVICE_EMPLOYEE' && (
              <CheckCircle2 className="w-4 h-4 text-indigo-600" />
            )}
          </div>
          <p className="text-xs text-slate-500 pl-11">
            Manage live collection routes, log weight payloads & complete tasks.
          </p>
        </div>

        {/* Role 3: System Mngt. */}
        <div
          onClick={() => onSelectRole('SYSTEM_MANAGEMENT')}
          className={`w-full p-4 mb-3 rounded-2xl border transition-all cursor-pointer ${
            selectedRole === 'SYSTEM_MANAGEMENT'
              ? 'bg-indigo-50/80 border-indigo-500 shadow-sm ring-1 ring-indigo-500/30'
              : 'bg-white border-slate-200 hover:border-slate-300 shadow-xs'
          }`}
        >
          <div className="flex items-center justify-between mb-1">
            <div className="flex items-center gap-3">
              <div className={`w-8 h-8 rounded-lg flex items-center justify-center ${
                selectedRole === 'SYSTEM_MANAGEMENT' ? 'bg-indigo-600 text-white' : 'bg-slate-100 text-slate-600'
              }`}>
                <Settings className="w-4 h-4" />
              </div>
              <h2 className="text-sm font-semibold text-slate-900">System Admin</h2>
            </div>
            {selectedRole === 'SYSTEM_MANAGEMENT' && (
              <CheckCircle2 className="w-4 h-4 text-indigo-600" />
            )}
          </div>
          <p className="text-xs text-slate-500 pl-11">
            City heatmap analytics, complaints dispatch & crew roster control.
          </p>
        </div>
      </div>

      {/* Action Button */}
      <button
        onClick={onGetStarted}
        className="w-full py-3.5 bg-indigo-600 hover:bg-indigo-700 text-white font-medium text-sm rounded-xl shadow-sm shadow-indigo-200 transition-all cursor-pointer mb-2 active:scale-[0.99]"
      >
        Get Started
      </button>
    </div>
  );
};
