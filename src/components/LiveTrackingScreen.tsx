import React from 'react';
import { ArrowLeft, Truck, Phone, AlertTriangle, User, Navigation } from 'lucide-react';

interface Props {
  onNavigateBack: () => void;
  onReportIssue: () => void;
}

export const LiveTrackingScreen: React.FC<Props> = ({
  onNavigateBack,
  onReportIssue,
}) => {
  return (
    <div className="flex flex-col h-full bg-slate-100 text-slate-800 relative justify-between overflow-hidden">
      {/* Top Floating Card */}
      <div className="p-4 z-10">
        <button
          onClick={onNavigateBack}
          className="w-8 h-8 bg-white border border-slate-200 rounded-full flex items-center justify-center mb-3 shadow-sm hover:bg-slate-50 transition-colors cursor-pointer"
        >
          <ArrowLeft className="w-4 h-4 text-slate-700" />
        </button>

        <div className="bg-white border border-slate-200 rounded-2xl p-4 shadow-sm flex items-center justify-between">
          <div>
            <div className="text-[10px] font-semibold text-slate-400 uppercase tracking-wider">
              Live GPS Tracking
            </div>
            <h1 className="text-lg font-bold text-slate-900">
              Collection Truck #402
            </h1>
          </div>

          <div className="bg-emerald-50 border border-emerald-200 text-emerald-700 px-3 py-1.5 rounded-xl text-center">
            <div className="text-lg font-extrabold leading-none">12</div>
            <div className="text-[8px] font-bold tracking-widest uppercase">MIN ETA</div>
          </div>
        </div>
      </div>

      {/* Simulated Map View Canvas */}
      <div className="absolute inset-0 flex items-center justify-center pointer-events-none">
        <div className="relative flex flex-col items-center">
          {/* Animated Truck on Map */}
          <div className="w-14 h-14 bg-indigo-600 rounded-2xl flex items-center justify-center shadow-lg shadow-indigo-200 animate-bounce">
            <Truck className="w-7 h-7 text-white" />
          </div>
          <div className="mt-2 bg-slate-900 text-white text-[10px] font-medium px-2.5 py-0.5 rounded-full shadow-sm">
            Truck #402 (Active Sensor)
          </div>
          <div className="text-[10px] text-slate-500 mt-1 bg-white/90 backdrop-blur-xs px-2.5 py-0.5 rounded-full shadow-xs border border-slate-200">
            Downtown Los Angeles → Zone B Sector
          </div>
        </div>
      </div>

      {/* Bottom Driver Sheet */}
      <div className="bg-white border-t border-slate-200 rounded-t-3xl p-5 z-10 shadow-lg">
        <div className="w-10 h-1 bg-slate-200 rounded-full mx-auto mb-4"></div>

        <div className="flex items-center space-x-3 mb-4">
          <div className="w-12 h-12 bg-indigo-50 border border-indigo-100 rounded-full flex items-center justify-center text-indigo-600">
            <User className="w-6 h-6" />
          </div>
          <div>
            <h2 className="text-sm font-bold text-slate-900">
              Sarah Jenkins
            </h2>
            <div className="flex items-center space-x-2 mt-0.5">
              <span className="bg-emerald-50 text-emerald-700 text-[10px] font-semibold px-2 py-0.5 rounded-full">
                En Route
              </span>
              <span className="text-slate-400 text-xs">•</span>
              <span className="text-slate-500 text-xs">
                Zone B Corridor
              </span>
            </div>
          </div>
        </div>

        <div className="flex space-x-2.5">
          <button
            onClick={onReportIssue}
            className="flex-1 py-2.5 bg-slate-50 hover:bg-slate-100 text-slate-700 border border-slate-200 rounded-xl text-xs font-semibold flex items-center justify-center space-x-1.5 transition-colors cursor-pointer"
          >
            <AlertTriangle className="w-3.5 h-3.5 text-slate-500" />
            <span>Report Issue</span>
          </button>
          <button
            onClick={() => alert('Dialing Driver Dispatch for Truck #402...')}
            className="flex-2 py-2.5 bg-indigo-600 hover:bg-indigo-700 text-white rounded-xl text-xs font-semibold flex items-center justify-center space-x-1.5 shadow-sm shadow-indigo-100 transition-colors cursor-pointer"
          >
            <Phone className="w-3.5 h-3.5" />
            <span>Contact Driver</span>
          </button>
        </div>
      </div>
    </div>
  );
};
