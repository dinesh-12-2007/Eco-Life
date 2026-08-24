import React, { useState } from 'react';
import { Employee } from '../types';
import { ArrowLeft, Search, User, Trophy, Star } from 'lucide-react';

interface Props {
  employees: Employee[];
  onNavigateBack: () => void;
}

export const RosterScreen: React.FC<Props> = ({
  employees,
  onNavigateBack,
}) => {
  const [search, setSearch] = useState('');

  const filtered = employees.filter((e) =>
    e.name.toLowerCase().includes(search.toLowerCase()) ||
    e.zone.toLowerCase().includes(search.toLowerCase())
  );

  return (
    <div className="flex flex-col h-full bg-[#F8FAFC] text-slate-800 p-4 overflow-y-auto">
      {/* Header */}
      <div className="flex items-center space-x-2 mb-3">
        <button onClick={onNavigateBack} className="w-8 h-8 rounded-full bg-white border border-slate-200 flex items-center justify-center cursor-pointer hover:bg-slate-50 transition-colors">
          <ArrowLeft className="w-4 h-4 text-slate-700" />
        </button>
        <h1 className="text-sm font-bold text-slate-900">
          Field Crew Roster
        </h1>
      </div>

      {/* Search Input */}
      <div className="relative mb-3">
        <input
          type="text"
          value={search}
          onChange={(e) => setSearch(e.target.value)}
          placeholder="Search operator name or sector..."
          className="w-full py-2.5 pl-9 pr-3.5 bg-white border border-slate-200 rounded-xl text-xs font-medium text-slate-900 focus:outline-none focus:border-indigo-500 focus:ring-2 focus:ring-indigo-100 transition-all placeholder:text-slate-400"
        />
        <Search className="w-4 h-4 text-slate-400 absolute left-3 top-3" />
      </div>

      {/* Leaderboard Pod */}
      <div className="bg-gradient-to-br from-indigo-600 to-indigo-700 rounded-2xl p-3.5 text-white shadow-sm shadow-indigo-100 mb-4 flex items-center justify-between">
        <div className="flex items-center space-x-3">
          <div className="w-9 h-9 bg-white/10 rounded-xl flex items-center justify-center text-amber-300">
            <Trophy className="w-5 h-5" />
          </div>
          <div>
            <div className="text-[10px] text-indigo-200 uppercase font-semibold">
              Top Operator This Week
            </div>
            <div className="text-sm font-bold">
              Sarah Jenkins • 142 Tasks
            </div>
          </div>
        </div>
        <span className="text-xs bg-white/20 px-2 py-0.5 rounded-full font-medium">#1</span>
      </div>

      <div className="text-xs font-bold uppercase tracking-wider text-slate-400 mb-2 px-1">
        Active Personnel
      </div>

      <div className="space-y-3 pb-4">
        {filtered.map((emp) => (
          <div
            key={emp.id}
            className="bg-white border border-slate-200 rounded-2xl p-4 shadow-sm"
          >
            <div className="flex items-center space-x-3 mb-3">
              <div className="w-10 h-10 bg-slate-100 rounded-full flex items-center justify-center text-slate-700">
                <User className="w-5 h-5" />
              </div>
              <div className="flex-1">
                <h3 className="text-sm font-bold text-slate-900">
                  {emp.name}
                </h3>
                <div className="flex space-x-1.5 mt-0.5">
                  <span className="bg-slate-100 text-slate-600 text-[10px] font-semibold px-2 py-0.5 rounded-full">
                    {emp.zone}
                  </span>
                  <span
                    className={`text-[10px] font-semibold px-2 py-0.5 rounded-full ${
                      emp.status === 'ON-DUTY'
                        ? 'bg-emerald-50 text-emerald-700'
                        : 'bg-slate-100 text-slate-500'
                    }`}
                  >
                    {emp.status}
                  </span>
                </div>
              </div>
            </div>

            <div className="border-t border-slate-100 pt-2.5 flex justify-between items-center mb-3 text-xs">
              <div>
                <span className="text-slate-400 text-[11px] block">Rating</span>
                <span className="font-bold text-slate-900 flex items-center gap-1">
                  <span>{emp.performance}</span>
                  <Star className="w-3 h-3 fill-amber-400 text-amber-400" />
                </span>
              </div>
              <div className="text-right">
                <span className="text-slate-400 text-[11px] block">Completed</span>
                <span className="font-bold text-slate-900">{emp.tasksDone} tasks</span>
              </div>
            </div>

            <button className="w-full py-2 bg-slate-50 hover:bg-slate-100 text-slate-700 border border-slate-200 rounded-xl text-xs font-semibold transition-colors cursor-pointer">
              Assign Dispatch Task
            </button>
          </div>
        ))}
      </div>
    </div>
  );
};
