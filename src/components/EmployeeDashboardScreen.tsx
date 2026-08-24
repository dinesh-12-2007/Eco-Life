import React from 'react';
import { ServiceTask } from '../types';
import { ArrowLeft, PlusSquare, Clock, MapPin, CheckCircle, Navigation, ChevronRight, BarChart3, Users, AlertCircle, FilePlus } from 'lucide-react';

interface Props {
  tasks: ServiceTask[];
  onAcceptTask: (taskId: string) => void;
  onStartTask: (taskId: string) => void;
  onCompleteTask: (taskId: string) => void;
  onNavigateBack: () => void;
  onNavigateLogWaste: () => void;
  onNavigateHeatmap: () => void;
  onNavigateAnalytics: () => void;
  onNavigateRoster: () => void;
  onNavigateComplaints: () => void;
}

export const EmployeeDashboardScreen: React.FC<Props> = ({
  tasks,
  onAcceptTask,
  onStartTask,
  onCompleteTask,
  onNavigateBack,
  onNavigateLogWaste,
  onNavigateHeatmap,
  onNavigateAnalytics,
  onNavigateRoster,
  onNavigateComplaints,
}) => {
  return (
    <div className="flex flex-col h-full bg-[#F8FAFC] text-slate-800 overflow-y-auto">
      {/* Header */}
      <div className="bg-white border-b border-slate-200 px-4 py-3 flex items-center justify-between sticky top-0 z-10">
        <div className="flex items-center space-x-2">
          <button onClick={onNavigateBack} className="w-7 h-7 rounded-full bg-slate-100 flex items-center justify-center cursor-pointer hover:bg-slate-200 transition-colors">
            <ArrowLeft className="w-4 h-4 text-slate-700" />
          </button>
          <span className="font-bold text-sm text-slate-900 tracking-tight">
            Admin Operations
          </span>
        </div>

        <button
          onClick={onNavigateLogWaste}
          className="bg-indigo-600 hover:bg-indigo-700 text-white px-2.5 py-1 rounded-lg text-xs font-medium flex items-center gap-1 shadow-xs transition-colors cursor-pointer"
        >
          <FilePlus className="w-3.5 h-3.5" />
          <span>Log</span>
        </button>
      </div>

      <div className="p-4 space-y-4">
        {/* Navigation Quick Strip */}
        <div className="grid grid-cols-5 gap-1 bg-slate-100 p-1 rounded-xl border border-slate-200/60">
          <button
            onClick={onNavigateLogWaste}
            className="py-1.5 rounded-lg text-[10px] font-semibold transition-all bg-white text-slate-900 shadow-xs"
          >
            Log
          </button>
          <button
            onClick={onNavigateHeatmap}
            className="py-1.5 rounded-lg text-[10px] font-semibold text-slate-600 hover:text-slate-900 transition-all"
          >
            Map
          </button>
          <button
            onClick={onNavigateAnalytics}
            className="py-1.5 rounded-lg text-[10px] font-semibold text-slate-600 hover:text-slate-900 transition-all"
          >
            Stats
          </button>
          <button
            onClick={onNavigateRoster}
            className="py-1.5 rounded-lg text-[10px] font-semibold text-slate-600 hover:text-slate-900 transition-all"
          >
            Crew
          </button>
          <button
            onClick={onNavigateComplaints}
            className="py-1.5 rounded-lg text-[10px] font-semibold text-slate-600 hover:text-slate-900 transition-all"
          >
            Alerts
          </button>
        </div>

        {/* Shift Impact Header */}
        <div className="bg-white border border-slate-200 rounded-2xl p-4 shadow-sm flex items-center justify-between">
          <div>
            <div className="text-[10px] font-semibold text-slate-400 uppercase tracking-wider">
              Shift Status
            </div>
            <div className="text-xl font-bold text-slate-900">
              Active Dispatch
            </div>
          </div>
          <div className="text-right">
            <div className="text-2xl font-bold text-indigo-600">
              {tasks.length}
            </div>
            <div className="text-[10px] font-medium text-slate-400 uppercase tracking-wider">
              Assigned Tasks
            </div>
          </div>
        </div>

        {/* Task Cards */}
        <div className="space-y-3 pb-4">
          <div className="text-xs font-bold uppercase tracking-wider text-slate-400 px-1">
            Task Queue
          </div>

          {tasks.map((task) => {
            const isRoutine = task.type === 'ROUTINE';
            return (
              <div
                key={task.id}
                className="bg-white border border-slate-200 rounded-2xl p-4 shadow-sm"
              >
                {/* Badges */}
                <div className="flex items-center justify-between mb-2">
                  <div className="flex items-center space-x-1.5">
                    <span className="bg-slate-100 text-slate-700 text-[10px] font-semibold px-2 py-0.5 rounded-full">
                      {task.type.replace('_', ' ')}
                    </span>
                    <span
                      className={`text-[10px] font-semibold px-2 py-0.5 rounded-full ${
                        task.status === 'ASSIGNED'
                          ? 'bg-rose-50 text-rose-700'
                          : task.status === 'IN_PROGRESS'
                          ? 'bg-amber-50 text-amber-700'
                          : 'bg-emerald-50 text-emerald-700'
                      }`}
                    >
                      {task.status.replace('_', ' ')}
                    </span>
                  </div>

                  {task.priority === 'URGENT' && (
                    <span className="bg-rose-50 text-rose-700 text-[10px] font-bold px-2 py-0.5 rounded-full">
                      Urgent
                    </span>
                  )}
                </div>

                <h3 className="text-sm font-bold text-slate-900 mb-0.5">
                  {task.title}
                </h3>
                <p className="text-xs text-slate-500 mb-2">
                  {task.location}
                </p>

                <div className="flex items-center space-x-3 text-xs text-slate-400 mb-3">
                  <div className="flex items-center space-x-1">
                    <Clock className="w-3.5 h-3.5" />
                    <span>{task.time}</span>
                  </div>
                  <div className="flex items-center space-x-1">
                    <MapPin className="w-3.5 h-3.5" />
                    <span>{task.distance}</span>
                  </div>
                </div>

                {/* Progress bar for routine */}
                {isRoutine && (
                  <div className="mb-3">
                    <div className="flex justify-between text-[10px] font-medium text-slate-500 mb-1">
                      <span>Route Progress</span>
                      <span>{task.progress}%</span>
                    </div>
                    <div className="w-full h-2 bg-slate-100 rounded-full overflow-hidden">
                      <div
                        className="h-full bg-indigo-600 rounded-full transition-all"
                        style={{ width: `${task.progress}%` }}
                      ></div>
                    </div>
                  </div>
                )}

                <div className="border-t border-slate-100 pt-3 flex space-x-2">
                  {task.status === 'ASSIGNED' && (
                    <>
                      <button
                        onClick={() => onAcceptTask(task.id)}
                        className="flex-1 py-2 bg-indigo-600 hover:bg-indigo-700 text-white rounded-xl text-xs font-semibold shadow-xs transition-colors cursor-pointer"
                      >
                        Accept Task
                      </button>
                      <button className="w-8 h-8 rounded-xl bg-slate-100 hover:bg-slate-200 flex items-center justify-center text-slate-700 transition-colors">
                        <Navigation className="w-4 h-4" />
                      </button>
                    </>
                  )}
                  {task.status === 'PENDING' && (
                    <>
                      <button
                        onClick={() => onStartTask(task.id)}
                        className="flex-1 py-2 bg-indigo-600 hover:bg-indigo-700 text-white rounded-xl text-xs font-semibold shadow-xs transition-colors cursor-pointer"
                      >
                        Start Task
                      </button>
                      <button className="w-8 h-8 rounded-xl bg-slate-100 hover:bg-slate-200 flex items-center justify-center text-slate-700 transition-colors">
                        <Navigation className="w-4 h-4" />
                      </button>
                    </>
                  )}
                  {task.status === 'IN_PROGRESS' && (
                    <button
                      onClick={() => onCompleteTask(task.id)}
                      className="w-full py-2 bg-emerald-50 hover:bg-emerald-100 text-emerald-700 rounded-xl text-xs font-semibold flex items-center justify-center space-x-1.5 transition-colors cursor-pointer"
                    >
                      <CheckCircle className="w-4 h-4 text-emerald-600" />
                      <span>Mark Complete</span>
                    </button>
                  )}
                  {task.status === 'COMPLETED' && (
                    <div className="w-full py-1.5 bg-slate-50 text-slate-500 rounded-xl text-center text-xs font-medium">
                      Task Finished
                    </div>
                  )}
                </div>
              </div>
            );
          })}
        </div>
      </div>
    </div>
  );
};
