export type UserRole = 'CITIZEN' | 'SERVICE_EMPLOYEE' | 'SYSTEM_MANAGEMENT';

export interface User {
  id: string;
  name: string;
  email: string;
  role: UserRole;
  balancePoints: number;
  recycledKgYtd: number;
  co2SavedKg: number;
}

export type TaskType = 'ROUTINE' | 'GIVEAWAY_PICKUP' | 'MISSED_COLLECTION' | 'BIN_REPAIR';
export type TaskStatus = 'ASSIGNED' | 'PENDING' | 'IN_PROGRESS' | 'COMPLETED';

export interface ServiceTask {
  id: string;
  title: string;
  type: TaskType;
  location: string;
  time: string;
  distance: string;
  progress: number;
  priority: string;
  status: TaskStatus;
}

export type ComplaintStatus = 'PENDING' | 'ASSIGNED' | 'RESOLVED';
export type ComplaintPriority = 'HIGH' | 'MEDIUM' | 'LOW';

export interface Complaint {
  id: string;
  title: string;
  description: string;
  location: string;
  priority: ComplaintPriority;
  status: ComplaintStatus;
  reportedAt: string;
  assignedCrew: string;
}

export interface Employee {
  id: string;
  name: string;
  zone: string;
  status: string;
  performance: number;
  tasksDone: number;
}

export interface Reward {
  id: string;
  title: string;
  description: string;
  costPoints: number;
  category: string;
}

export type ScreenId =
  | 'role_selection'
  | 'auth'
  | 'citizen_home'
  | 'live_tracking'
  | 'give_away'
  | 'rewards'
  | 'employee_dashboard'
  | 'log_waste'
  | 'heatmap'
  | 'analytics'
  | 'roster'
  | 'complaints';
