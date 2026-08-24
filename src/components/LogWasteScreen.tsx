import React, { useState } from 'react';
import { ArrowLeft, QrCode, CheckCircle2, ArrowRight, Sparkles } from 'lucide-react';

interface Props {
  onLogSubmitted: (zone: string, wasteType: string, weightKg: number, points: number) => void;
  onNavigateBack: () => void;
}

export const LogWasteScreen: React.FC<Props> = ({
  onLogSubmitted,
  onNavigateBack,
}) => {
  const [selectedZone, setSelectedZone] = useState('Zone B - Residential');
  const [wasteType, setWasteType] = useState('PLASTIC');
  const [weightKg, setWeightKg] = useState('14.5');
  const [isSuccess, setIsSuccess] = useState(false);

  const multipliers: Record<string, number> = {
    PLASTIC: 15,
    PAPER: 5,
    METAL: 20,
    ORGANIC: 2,
  };

  const parsedWeight = parseFloat(weightKg) || 0;
  const calculatedPoints = Math.round(parsedWeight * (multipliers[wasteType] || 10));

  const handleSubmit = () => {
    if (parsedWeight > 0) {
      onLogSubmitted(selectedZone, wasteType, parsedWeight, calculatedPoints);
      setIsSuccess(true);
    }
  };

  return (
    <div className="flex flex-col h-full bg-[#F8FAFC] text-slate-800 p-4 overflow-y-auto">
      <div className="flex items-center space-x-2 mb-3">
        <button onClick={onNavigateBack} className="w-8 h-8 rounded-full bg-white border border-slate-200 flex items-center justify-center cursor-pointer hover:bg-slate-50 transition-colors">
          <ArrowLeft className="w-4 h-4 text-slate-700" />
        </button>
        <h1 className="text-sm font-bold text-slate-900">
          Record Waste Collection
        </h1>
      </div>

      {isSuccess ? (
        <div className="bg-white border border-slate-200 rounded-2xl p-6 shadow-sm text-center my-auto">
          <div className="w-14 h-14 bg-emerald-50 text-emerald-600 rounded-2xl flex items-center justify-center mx-auto mb-3">
            <CheckCircle2 className="w-8 h-8" />
          </div>
          <h2 className="text-lg font-bold mb-1 text-slate-900">
            Payload Logged Successfully
          </h2>
          <p className="text-xs text-slate-500 mb-5 leading-relaxed">
            Synchronized with central municipal grid ledger. <br />
            <strong className="text-emerald-600 font-semibold">+{calculatedPoints} Eco Points</strong> assigned to local sector.
          </p>
          <button
            onClick={() => {
              setIsSuccess(false);
              setWeightKg('');
            }}
            className="w-full py-2.5 bg-indigo-600 hover:bg-indigo-700 text-white rounded-xl font-medium text-xs shadow-sm transition-colors cursor-pointer"
          >
            Log Another Payload
          </button>
        </div>
      ) : (
        <div className="space-y-4">
          {/* 1. Select Area */}
          <div>
            <label className="block text-xs font-semibold text-slate-700 mb-1.5">
              1. Collection Sector
            </label>
            <div className="grid grid-cols-3 gap-1.5">
              {['Zone A - Downtown', 'Zone B - Residential', 'Zone C - Industrial'].map((zone) => {
                const isSelected = selectedZone === zone;
                return (
                  <button
                    key={zone}
                    onClick={() => setSelectedZone(zone)}
                    className={`py-2 px-1 rounded-xl border text-[10px] font-semibold transition-all ${
                      isSelected
                        ? 'bg-indigo-600 text-white border-indigo-600 shadow-xs'
                        : 'bg-white text-slate-700 border-slate-200 hover:border-slate-300'
                    }`}
                  >
                    {zone.split(' - ')[0]}
                  </button>
                );
              })}
            </div>
          </div>

          {/* 2. Waste Type */}
          <div>
            <label className="block text-xs font-semibold text-slate-700 mb-1.5">
              2. Material Stream
            </label>
            <div className="grid grid-cols-2 gap-2">
              {['PLASTIC', 'PAPER', 'METAL', 'ORGANIC'].map((type) => {
                const isSelected = wasteType === type;
                return (
                  <button
                    key={type}
                    onClick={() => setWasteType(type)}
                    className={`py-2.5 rounded-xl border text-xs font-semibold transition-all ${
                      isSelected
                        ? 'bg-indigo-600 text-white border-indigo-600 shadow-xs'
                        : 'bg-white text-slate-700 border-slate-200 hover:border-slate-300'
                    }`}
                  >
                    {type}
                  </button>
                );
              })}
            </div>
          </div>

          {/* 3. Weight */}
          <div>
            <label className="block text-xs font-semibold text-slate-700 mb-1.5">
              3. Weight Payload (kg)
            </label>
            <div className="flex space-x-2">
              <input
                type="number"
                step="0.1"
                value={weightKg}
                onChange={(e) => setWeightKg(e.target.value)}
                placeholder="0.0"
                className="flex-1 p-2.5 bg-white border border-slate-200 rounded-xl text-lg font-bold text-slate-900 focus:outline-none focus:border-indigo-500 focus:ring-2 focus:ring-indigo-100 transition-all"
              />
              <button
                onClick={() => {
                  const simulated = (Math.random() * 25 + 5).toFixed(1);
                  setWeightKg(simulated);
                }}
                className="px-3.5 bg-slate-100 hover:bg-slate-200 border border-slate-200 rounded-xl flex flex-col items-center justify-center text-slate-700 transition-colors"
              >
                <QrCode className="w-4 h-4" />
                <span className="text-[9px] font-semibold mt-0.5">Scale</span>
              </button>
            </div>
          </div>

          {/* Impact Value */}
          <div className="bg-emerald-50 border border-emerald-200 rounded-xl p-3.5 flex items-center justify-between">
            <span className="text-xs font-semibold text-emerald-800">
              Calculated Impact
            </span>
            <span className="text-lg font-bold text-emerald-700">
              +{calculatedPoints} PTS
            </span>
          </div>

          <button
            onClick={handleSubmit}
            className="w-full mt-2 py-3 bg-indigo-600 hover:bg-indigo-700 text-white font-medium text-xs rounded-xl shadow-sm shadow-indigo-100 transition-all flex items-center justify-center space-x-2 cursor-pointer"
          >
            <span>Confirm & Synchronize Ledger</span>
            <ArrowRight className="w-4 h-4" />
          </button>
        </div>
      )}
    </div>
  );
};
