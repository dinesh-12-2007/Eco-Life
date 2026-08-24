from pydantic import BaseModel, EmailStr, Field
from typing import Optional, List
from datetime import datetime

class UserBase(BaseModel):
    email: EmailStr
    name: str
    role: str = "CITIZEN"
    zone: str = "Zone B"
    phone: Optional[str] = None

class UserRegister(UserBase):
    password: str

class UserLogin(BaseModel):
    email: EmailStr
    password: str

class Token(BaseModel):
    access_token: str
    token_type: str = "bearer"
    user_id: str
    role: str
    name: str

class UserResponse(UserBase):
    id: str
    balance_points: int = 0
    recycled_kg_ytd: float = 0.0

    class Config:
        orm_mode = True

class RewardTransactionResponse(BaseModel):
    id: str
    type: str # EARN or REDEEM
    points: int
    description: str
    date: str
    reference_type: Optional[str] = None
    reference_id: Optional[str] = None

    class Config:
        orm_mode = True

class RewardWalletResponse(BaseModel):
    current_points: int
    total_earned: int
    total_used: int
    transactions: List[RewardTransactionResponse] = []

class ConversionConfigResponse(BaseModel):
    points_per_unit: int = 10 # 10 points = 1.0 currency (100 pts = ₹10)
    currency_symbol: str = "₹"
    description: str = "100 Reward Points = ₹10 Electricity Bill Credit"

class ConversionConfigUpdate(BaseModel):
    points_per_unit: int = Field(gt=0, description="Points needed per currency unit credit")
    currency_symbol: str = "₹"
    description: Optional[str] = None

class ElectricityProviderResponse(BaseModel):
    id: str
    name: str
    code: str
    state: str

    class Config:
        orm_mode = True

class ElectricityBillResponse(BaseModel):
    id: str
    provider_id: str
    provider_name: str
    consumer_number: str
    consumer_name: str
    bill_number: str
    billing_month: str
    due_date: str
    bill_amount: float
    status: str

    class Config:
        orm_mode = True

class BillPaymentRequest(BaseModel):
    provider_id: str
    consumer_number: str
    bill_number: str
    total_bill_amount: float
    points_to_redeem: int = Field(ge=0, description="Points citizen chooses to use")

class BillPaymentResponse(BaseModel):
    payment_id: str
    bill_number: str
    consumer_number: str
    provider_name: str
    total_bill_amount: float
    points_redeemed: int
    points_discount_amount: float
    amount_paid: float
    transaction_ref: str
    timestamp: str
    updated_wallet_balance: int
    status: str = "SUCCESS"

class WasteLogCreate(BaseModel):
    zone: str
    waste_type: str
    weight_kg: float
    points: int

class WasteLogResponse(BaseModel):
    id: str
    zone: str
    waste_type: str
    weight_kg: float
    points_awarded: int
    created_at: datetime

    class Config:
        orm_mode = True

class ComplaintCreate(BaseModel):
    title: str
    description: str
    location: str
    priority: str = "MEDIUM"

class ComplaintResponse(BaseModel):
    id: str
    title: str
    description: str
    location: str
    priority: str
    status: str
    assigned_crew: str
    created_at: datetime

    class Config:
        orm_mode = True
