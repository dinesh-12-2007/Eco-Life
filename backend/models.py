import enum
from datetime import datetime
from sqlalchemy import (
    Column,
    String,
    Integer,
    Float,
    Boolean,
    DateTime,
    ForeignKey,
    Enum as SqlEnum,
    Text,
)
from sqlalchemy.orm import relationship
from .database import Base

class UserRole(str, enum.Enum):
    CITIZEN = "CITIZEN"
    SERVICE_EMPLOYEE = "SERVICE_EMPLOYEE"
    SYSTEM_MANAGEMENT = "SYSTEM_MANAGEMENT"

class TransactionType(str, enum.Enum):
    EARN = "EARN"
    REDEEM = "REDEEM"

class BillStatus(str, enum.Enum):
    UNPAID = "UNPAID"
    PAID = "PAID"
    PARTIAL = "PARTIAL"

class ComplaintPriority(str, enum.Enum):
    HIGH = "HIGH"
    MEDIUM = "MEDIUM"
    LOW = "LOW"

class ComplaintStatus(str, enum.Enum):
    PENDING = "PENDING"
    ASSIGNED = "ASSIGNED"
    RESOLVED = "RESOLVED"

class TaskType(str, enum.Enum):
    COMPLAINT = "COMPLAINT"
    MISSED_COLLECTION = "MISSED_COLLECTION"
    ROUTINE = "ROUTINE"

class TaskStatus(str, enum.Enum):
    ASSIGNED = "ASSIGNED"
    PENDING = "PENDING"
    IN_PROGRESS = "IN_PROGRESS"
    COMPLETED = "COMPLETED"

class User(Base):
    __tablename__ = "users"

    id = Column(String(64), primary_key=True, index=True)
    email = Column(String(255), unique=True, index=True, nullable=False)
    hashed_password = Column(String(255), nullable=False)
    name = Column(String(255), nullable=False)
    role = Column(SqlEnum(UserRole), default=UserRole.CITIZEN, nullable=False)
    zone = Column(String(64), default="Zone B")
    phone = Column(String(32), nullable=True)
    is_active = Column(Boolean, default=True)
    created_at = Column(DateTime, default=datetime.utcnow)

    # Relationships
    wallet = relationship("RewardWallet", back_populates="user", uselist=False, cascade="all, delete-orphan")
    reward_transactions = relationship("RewardTransaction", back_populates="user", cascade="all, delete-orphan")
    electricity_bills = relationship("ElectricityBill", back_populates="user", cascade="all, delete-orphan")
    bill_payments = relationship("BillPayment", back_populates="user", cascade="all, delete-orphan")
    waste_logs = relationship("WasteLog", back_populates="user", cascade="all, delete-orphan")
    complaints = relationship("Complaint", back_populates="user", cascade="all, delete-orphan")

class RewardWallet(Base):
    __tablename__ = "reward_wallets"

    id = Column(String(64), primary_key=True, index=True)
    user_id = Column(String(64), ForeignKey("users.id", ondelete="CASCADE"), unique=True, nullable=False)
    current_balance = Column(Integer, default=0, nullable=False)
    total_earned = Column(Integer, default=0, nullable=False)
    total_used = Column(Integer, default=0, nullable=False)
    updated_at = Column(DateTime, default=datetime.utcnow, onupdate=datetime.utcnow)

    user = relationship("User", back_populates="wallet")
    transactions = relationship("RewardTransaction", back_populates="wallet", cascade="all, delete-orphan")

class RewardTransaction(Base):
    __tablename__ = "reward_transactions"

    id = Column(String(64), primary_key=True, index=True)
    wallet_id = Column(String(64), ForeignKey("reward_wallets.id", ondelete="CASCADE"), nullable=False)
    user_id = Column(String(64), ForeignKey("users.id", ondelete="CASCADE"), nullable=False)
    type = Column(SqlEnum(TransactionType), nullable=False) # EARN or REDEEM
    points = Column(Integer, nullable=False)
    description = Column(String(255), nullable=False)
    reference_type = Column(String(64), nullable=True) # e.g. 'WASTE_LOG', 'ELECTRICITY_BILL'
    reference_id = Column(String(64), nullable=True)
    created_at = Column(DateTime, default=datetime.utcnow)

    user = relationship("User", back_populates="reward_transactions")
    wallet = relationship("RewardWallet", back_populates="transactions")

class ElectricityProvider(Base):
    __tablename__ = "electricity_providers"

    id = Column(String(64), primary_key=True, index=True)
    name = Column(String(255), nullable=False)
    code = Column(String(64), unique=True, nullable=False)
    state = Column(String(128), nullable=False)
    is_active = Column(Boolean, default=True)

class ElectricityBill(Base):
    __tablename__ = "electricity_bills"

    id = Column(String(64), primary_key=True, index=True)
    user_id = Column(String(64), ForeignKey("users.id", ondelete="SET NULL"), nullable=True)
    provider_id = Column(String(64), ForeignKey("electricity_providers.id"), nullable=False)
    consumer_number = Column(String(64), index=True, nullable=False)
    consumer_name = Column(String(255), nullable=False)
    bill_number = Column(String(64), unique=True, nullable=False)
    billing_month = Column(String(64), nullable=False)
    due_date = Column(String(64), nullable=False)
    bill_amount = Column(Float, nullable=False)
    status = Column(SqlEnum(BillStatus), default=BillStatus.UNPAID, nullable=False)
    created_at = Column(DateTime, default=datetime.utcnow)

    user = relationship("User", back_populates="electricity_bills")
    provider = relationship("ElectricityProvider")
    payments = relationship("BillPayment", back_populates="bill")

class BillPayment(Base):
    __tablename__ = "bill_payments"

    id = Column(String(64), primary_key=True, index=True)
    bill_id = Column(String(64), ForeignKey("electricity_bills.id"), nullable=False)
    user_id = Column(String(64), ForeignKey("users.id"), nullable=False)
    total_bill_amount = Column(Float, nullable=False)
    points_redeemed = Column(Integer, default=0, nullable=False)
    points_discount_amount = Column(Float, default=0.0, nullable=False)
    amount_paid = Column(Float, nullable=False)
    payment_method = Column(String(64), default="REWARD_POINTS_AND_MUNICIPAL_CREDIT")
    transaction_reference = Column(String(128), unique=True, nullable=False)
    payment_status = Column(String(32), default="SUCCESS")
    paid_at = Column(DateTime, default=datetime.utcnow)

    user = relationship("User", back_populates="bill_payments")
    bill = relationship("ElectricityBill", back_populates="payments")

class WasteLog(Base):
    __tablename__ = "waste_logs"

    id = Column(String(64), primary_key=True, index=True)
    user_id = Column(String(64), ForeignKey("users.id"), nullable=False)
    zone = Column(String(64), nullable=False)
    waste_type = Column(String(64), nullable=False)
    weight_kg = Column(Float, nullable=False)
    points_awarded = Column(Integer, nullable=False)
    verified_by = Column(String(64), nullable=True)
    created_at = Column(DateTime, default=datetime.utcnow)

    user = relationship("User", back_populates="waste_logs")

class Complaint(Base):
    __tablename__ = "complaints"

    id = Column(String(64), primary_key=True, index=True)
    user_id = Column(String(64), ForeignKey("users.id"), nullable=False)
    title = Column(String(255), nullable=False)
    description = Column(Text, nullable=False)
    location = Column(String(255), nullable=False)
    priority = Column(SqlEnum(ComplaintPriority), default=ComplaintPriority.MEDIUM)
    status = Column(SqlEnum(ComplaintStatus), default=ComplaintStatus.PENDING)
    assigned_crew = Column(String(128), default="Unassigned")
    image_url = Column(String(512), nullable=True)
    is_verified = Column(Boolean, default=False)
    created_at = Column(DateTime, default=datetime.utcnow)

    user = relationship("User", back_populates="complaints")

class ServiceTask(Base):
    __tablename__ = "service_tasks"

    id = Column(String(64), primary_key=True, index=True)
    type = Column(SqlEnum(TaskType), default=TaskType.ROUTINE)
    title = Column(String(255), nullable=False)
    location = Column(String(255), nullable=False)
    scheduled_time = Column(String(64), nullable=False)
    distance = Column(String(64), nullable=False)
    priority = Column(String(32), default="NORMAL")
    status = Column(SqlEnum(TaskStatus), default=TaskStatus.ASSIGNED)
    progress = Column(Integer, default=0)
    assigned_to = Column(String(64), nullable=True)
    created_at = Column(DateTime, default=datetime.utcnow)

class SystemConfig(Base):
    __tablename__ = "system_configs"

    key = Column(String(64), primary_key=True, index=True)
    value_json = Column(Text, nullable=False) # JSON encoded configuration string
    description = Column(String(255), nullable=True)
    updated_at = Column(DateTime, default=datetime.utcnow, onupdate=datetime.utcnow)
