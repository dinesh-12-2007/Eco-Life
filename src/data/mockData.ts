import {
  Complaint,
  Employee,
  ServiceTask,
  User,
  RewardTransaction,
  ElectricityProvider,
  PointsConversionConfig,
  BillPaymentReceipt,
} from '../types';

export const initialUser: User = {
  id: 'usr_001',
  name: 'Alex Vance',
  email: 'alex.vance@wastemail.org',
  role: 'CITIZEN',
  balancePoints: 1250,
  recycledKgYtd: 45.0,
  co2SavedKg: 112.5,
};

export const initialConversionConfig: PointsConversionConfig = {
  pointsPerUnit: 10, // 10 pts = 1 INR (100 pts = ₹10)
  currencySymbol: '₹',
  description: '100 Reward Points = ₹10 Electricity Bill Credit',
};

export const initialElectricityProviders: ElectricityProvider[] = [
  { id: 'prov-01', name: 'BESCOM (Bangalore Electricity Supply)', state: 'Karnataka', code: 'BESCOM' },
  { id: 'prov-02', name: 'TANGEDCO (Tamil Nadu Generation & Distribution)', state: 'Tamil Nadu', code: 'TANGEDCO' },
  { id: 'prov-03', name: 'MSEDCL (Mahavitaran Maharashtra)', state: 'Maharashtra', code: 'MSEDCL' },
  { id: 'prov-04', name: 'BSES Yamuna Power Limited', state: 'Delhi', code: 'BSES-Y' },
  { id: 'prov-05', name: 'APSPDCL (Southern Power Distribution AP)', state: 'Andhra Pradesh', code: 'APSPDCL' },
  { id: 'prov-06', name: 'Tata Power DDL', state: 'Delhi-NCR', code: 'TATAPOWER' },
];

export const initialTransactions: RewardTransaction[] = [
  {
    id: 'tx-01',
    type: 'EARN',
    points: 500,
    description: 'Organic Compost Segregation & Drop-off',
    date: '2026-08-20 09:30',
    referenceType: 'WASTE_LOG',
    referenceId: 'WL-9821',
  },
  {
    id: 'tx-02',
    type: 'EARN',
    points: 750,
    description: 'Dry Recyclables (Plastic & Metal) Verification',
    date: '2026-08-22 14:15',
    referenceType: 'WASTE_LOG',
    referenceId: 'WL-9844',
  },
  {
    id: 'tx-03',
    type: 'REDEEM',
    points: 600,
    description: 'Electricity Bill Credit - BESCOM (Cons. #90283471)',
    date: '2026-08-23 11:20',
    referenceType: 'ELECTRICITY_BILL',
    referenceId: 'PAY-8829',
  },
  {
    id: 'tx-04',
    type: 'EARN',
    points: 600,
    description: 'Hazardous E-Waste Battery Drive Participation',
    date: '2026-08-24 08:45',
    referenceType: 'WASTE_LOG',
    referenceId: 'WL-9902',
  },
];

export const initialPaymentReceipts: BillPaymentReceipt[] = [
  {
    paymentId: 'PAY-8829',
    billNumber: 'BILL-JUL-48201',
    consumerNumber: '90283471',
    providerName: 'BESCOM (Bangalore Electricity Supply)',
    totalBillAmount: 850.0,
    pointsRedeemed: 600,
    pointsDiscountAmount: 60.0,
    amountPaid: 790.0,
    transactionRef: 'TXN_ELEC_1724410283921',
    timestamp: '2026-08-23 11:20',
    updatedWalletBalance: 1250,
    status: 'PAID & RECORDED',
  },
];

export const initialTasks: ServiceTask[] = [
  {
    id: 'TSK-892',
    title: 'WEEKLY COLLECTION ROUTE',
    type: 'ROUTINE',
    location: 'Zone B: 42-108 Oak St',
    time: '08:00 - 11:30',
    distance: '0.4 mi',
    progress: 65,
    priority: 'NORMAL',
    status: 'IN_PROGRESS',
  },
  {
    id: 'TSK-904',
    title: 'COMMERCIAL BIN DISPATCH: SECTOR 4',
    type: 'SPECIAL_PICKUP',
    location: '14 Elm Court, Apt 2B',
    time: '13:00 - 14:00',
    distance: '1.2 mi',
    progress: 0,
    priority: 'NORMAL',
    status: 'PENDING',
  },
  {
    id: 'TSK-912',
    title: 'URGENT: OVERFLOW BIN REPORT',
    type: 'MISSED_COLLECTION',
    location: 'Metro Station - North Plaza',
    time: 'IMMEDIATE',
    distance: '0.8 mi',
    progress: 0,
    priority: 'URGENT',
    status: 'ASSIGNED',
  },
];

export const initialComplaints: Complaint[] = [
  {
    id: '892',
    title: 'MISSED COLLECTION - RECYCLABLES',
    description: 'Recycling bin on the corner of 4th & Main was bypassed during the morning run. Blue bins overflowing onto walkway.',
    location: '402 Main St, Sector 4',
    priority: 'HIGH',
    status: 'PENDING',
    reportedAt: '10 Mins Ago',
    assignedCrew: 'CREW-04 (TRUCK #402)',
  },
  {
    id: '890',
    title: 'DAMAGED PUBLIC COMPOST BIN',
    description: 'Lid latch broken, animals accessing food waste container.',
    location: 'Highland Park, Sector 2',
    priority: 'MEDIUM',
    status: 'ASSIGNED',
    reportedAt: '2 Hours Ago',
    assignedCrew: 'CREW-02 (TRUCK #105)',
  },
  {
    id: '885',
    title: 'ILLEGAL DUMPING REPORTED',
    description: 'Bulk construction debris left in commercial alleyway.',
    location: '72 Industrial Way, Sector 7',
    priority: 'HIGH',
    status: 'RESOLVED',
    reportedAt: 'Yesterday',
    assignedCrew: 'CREW-01 (TRUCK #301)',
  },
];

export const initialEmployees: Employee[] = [
  {
    id: 'EMP-01',
    name: 'Sarah Jenkins',
    zone: 'ZONE B',
    status: 'ON-DUTY',
    performance: 4.8,
    tasksDone: 142,
  },
  {
    id: 'EMP-02',
    name: 'Marcus Cole',
    zone: 'ZONE A',
    status: 'ON-DUTY',
    performance: 4.2,
    tasksDone: 98,
  },
  {
    id: 'EMP-03',
    name: 'Elena Rostova',
    zone: 'ZONE C',
    status: 'BREAK',
    performance: 4.9,
    tasksDone: 165,
  },
  {
    id: 'EMP-04',
    name: 'David Kim',
    zone: 'ZONE B',
    status: 'OFF-DUTY',
    performance: 4.5,
    tasksDone: 110,
  },
];
