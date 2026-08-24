import { Complaint, Employee, Reward, ServiceTask, User } from '../types';

export const initialUser: User = {
  id: 'usr_001',
  name: 'Alex Vance',
  email: 'alex.vance@wastemail.org',
  role: 'CITIZEN',
  balancePoints: 1250,
  recycledKgYtd: 45.0,
  co2SavedKg: 112.5,
};

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
    title: 'GIVEAWAY PICKUP: DINING CHAIRS',
    type: 'GIVEAWAY_PICKUP',
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

export const initialRewards: Reward[] = [
  {
    id: 'rew_1',
    title: '$15 Organic Grocer Voucher',
    description: 'Valid at all city co-op and farmers market stalls.',
    costPoints: 500,
    category: 'GROCERY',
  },
  {
    id: 'rew_2',
    title: 'Free Monthly Transit Pass',
    description: 'Unlimited subway & municipal electric bus rides.',
    costPoints: 1000,
    category: 'TRANSIT',
  },
  {
    id: 'rew_3',
    title: 'EcoCycle Brutalist Steel Bottle',
    description: 'Laser engraved vacuum-insulated reusable container.',
    costPoints: 750,
    category: 'MERCH',
  },
  {
    id: 'rew_4',
    title: '$25 Sustainable Apparel Credit',
    description: 'Support local zero-waste and circular textile makers.',
    costPoints: 1200,
    category: 'APPAREL',
  },
];
