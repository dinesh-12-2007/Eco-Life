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

export type TaskType = 'ROUTINE' | 'SPECIAL_PICKUP' | 'MISSED_COLLECTION' | 'BIN_REPAIR';
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

export type TransactionType = 'EARN' | 'REDEEM';

export interface RewardTransaction {
  id: string;
  type: TransactionType;
  points: number;
  description: string;
  date: string;
  referenceType?: string;
  referenceId?: string;
}

export interface RewardWallet {
  currentPoints: number;
  totalEarned: number;
  totalUsed: number;
  transactions: RewardTransaction[];
}

export interface PointsConversionConfig {
  pointsPerUnit: number; // 10 points = 1 currency unit (100 pts = ₹10)
  currencySymbol: string;
  description: string;
}

export interface ElectricityProvider {
  id: string;
  name: string;
  state: string;
  code: string;
}

export interface ElectricityBill {
  id: string;
  providerId: string;
  providerName: string;
  consumerNumber: string;
  consumerName: string;
  billNumber: string;
  billingMonth: string;
  dueDate: string;
  billAmount: number;
  status: 'UNPAID' | 'PAID';
}

export interface BillPaymentReceipt {
  paymentId: string;
  billNumber: string;
  consumerNumber: string;
  providerName: string;
  totalBillAmount: number;
  pointsRedeemed: number;
  pointsDiscountAmount: number;
  amountPaid: number;
  transactionRef: string;
  timestamp: string;
  updatedWalletBalance: number;
  status: string;
}

export type ScreenId =
  | 'role_selection'
  | 'auth'
  | 'citizen_home'
  | 'live_tracking'
  | 'electricity_bill'
  | 'rewards'
  | 'employee_dashboard'
  | 'log_waste'
  | 'heatmap'
  | 'analytics'
  | 'roster'
  | 'complaints';
