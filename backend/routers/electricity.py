import uuid
from datetime import datetime
from typing import List
from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy.orm import Session
from ..database import get_db
from ..models import (
    User,
    RewardWallet,
    RewardTransaction,
    TransactionType,
    ElectricityProvider,
    ElectricityBill,
    BillStatus,
    BillPayment,
)
from ..schemas import (
    ElectricityProviderResponse,
    ElectricityBillResponse,
    BillPaymentRequest,
    BillPaymentResponse,
)
from ..auth import require_current_user, get_current_user
from .wallet import get_db_conversion_config

router = APIRouter(prefix="/api/electricity", tags=["electricity"])

@router.get("/providers", response_model=List[ElectricityProviderResponse])
def get_providers(db: Session = Depends(get_db)):
    providers = db.query(ElectricityProvider).filter(ElectricityProvider.is_active == True).all()
    if not providers:
        # Seed initial providers
        seed_providers = [
            ElectricityProvider(id="prov-01", name="BESCOM (Bangalore Electricity Supply)", code="BESCOM", state="Karnataka"),
            ElectricityProvider(id="prov-02", name="TANGEDCO (Tamil Nadu Generation & Distribution)", code="TANGEDCO", state="Tamil Nadu"),
            ElectricityProvider(id="prov-03", name="MSEDCL (Mahavitaran Maharashtra)", code="MSEDCL", state="Maharashtra"),
            ElectricityProvider(id="prov-04", name="BSES Yamuna Power Limited", code="BSES-Y", state="Delhi"),
            ElectricityProvider(id="prov-05", name="APSPDCL (Southern Power Distribution AP)", code="APSPDCL", state="Andhra Pradesh"),
            ElectricityProvider(id="prov-06", name="Tata Power DDL", code="TATAPOWER", state="Delhi-NCR"),
        ]
        db.add_all(seed_providers)
        db.commit()
        providers = seed_providers

    return providers

@router.get("/bills/fetch", response_model=ElectricityBillResponse)
def fetch_bill(
    providerId: str,
    consumerNumber: str,
    db: Session = Depends(get_db),
    user: User = Depends(require_current_user)
):
    provider = db.query(ElectricityProvider).filter(ElectricityProvider.id == providerId).first()
    if not provider:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Electricity provider not found.")

    # Check if bill exists in database
    bill = (
        db.query(ElectricityBill)
        .filter(ElectricityBill.provider_id == providerId, ElectricityBill.consumer_number == consumerNumber)
        .first()
    )

    if not bill:
        # Deterministically generate realistic bill for demo grid integration
        hash_val = abs(hash(consumerNumber)) % 100000
        amount = 450.0 + (hash_val % 1200)
        bill_id = f"eb-{uuid.uuid4().hex[:8]}"
        bill = ElectricityBill(
            id=bill_id,
            user_id=user.id,
            provider_id=provider.id,
            consumer_number=consumerNumber,
            consumer_name=user.name,
            bill_number=f"BILL-AUG-{10000 + (hash_val % 90000)}",
            billing_month="August 2026",
            due_date="2026-09-10",
            bill_amount=amount,
            status=BillStatus.UNPAID
        )
        db.add(bill)
        db.commit()
        db.refresh(bill)

    return ElectricityBillResponse(
        id=bill.id,
        provider_id=provider.id,
        provider_name=provider.name,
        consumer_number=bill.consumer_number,
        consumer_name=bill.consumer_name,
        bill_number=bill.bill_number,
        billing_month=bill.billing_month,
        due_date=bill.due_date,
        bill_amount=bill.bill_amount,
        status=bill.status.value if hasattr(bill.status, "value") else str(bill.status)
    )

@router.post("/bills/pay", response_model=BillPaymentResponse)
def pay_electricity_bill(
    request: BillPaymentRequest,
    db: Session = Depends(get_db),
    user: User = Depends(require_current_user)
):
    # 1. Fetch wallet & lock/validate server-side
    wallet = db.query(RewardWallet).filter(RewardWallet.user_id == user.id).first()
    if not wallet:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail="User reward wallet not found."
        )

    # 2. Server-side validation of points balance
    if request.points_to_redeem < 0:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail="Points to redeem cannot be negative."
        )
    if request.points_to_redeem > wallet.current_balance:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail=f"Insufficient points. Available balance: {wallet.current_balance} PTS, Requested: {request.points_to_redeem} PTS."
        )

    # 3. Fetch provider & bill
    provider = db.query(ElectricityProvider).filter(ElectricityProvider.id == request.provider_id).first()
    provider_name = provider.name if provider else "Electricity Board"

    bill = (
        db.query(ElectricityBill)
        .filter(ElectricityBill.bill_number == request.bill_number)
        .first()
    )
    if not bill:
        # Create persistent bill record if first time paying
        bill = ElectricityBill(
            id=f"eb-{uuid.uuid4().hex[:8]}",
            user_id=user.id,
            provider_id=request.provider_id,
            consumer_number=request.consumer_number,
            consumer_name=user.name,
            bill_number=request.bill_number,
            billing_month="August 2026",
            due_date="2026-09-10",
            bill_amount=request.total_bill_amount,
            status=BillStatus.UNPAID
        )
        db.add(bill)
        db.flush()

    # 4. Calculate discount from server configuration
    cfg = get_db_conversion_config(db)
    points_per_unit = cfg.get("points_per_unit", 10)
    discount_amount = float(request.points_to_redeem) / points_per_unit

    if discount_amount > request.total_bill_amount:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail="Discount from reward points cannot exceed total bill amount."
        )

    amount_paid = request.total_bill_amount - discount_amount
    payment_id = f"PAY-{uuid.uuid4().hex[:8]}"
    txn_ref = f"TXN_ELEC_{int(datetime.utcnow().timestamp() * 1000)}"

    # 5. ATOMIC DB UPDATES: Deduct points from wallet, create REDEEM transaction, create payment record, mark bill paid
    wallet.current_balance -= request.points_to_redeem
    wallet.total_used += request.points_to_redeem

    if request.points_to_redeem > 0:
        tx = RewardTransaction(
            id=f"tx-{uuid.uuid4().hex[:8]}",
            wallet_id=wallet.id,
            user_id=user.id,
            type=TransactionType.REDEEM,
            points=request.points_to_redeem,
            description=f"Electricity Bill Credit - {provider_name} (Cons. #{request.consumer_number})",
            reference_type="ELECTRICITY_BILL",
            reference_id=payment_id
        )
        db.add(tx)

    bill.status = BillStatus.PAID

    payment = BillPayment(
        id=payment_id,
        bill_id=bill.id,
        user_id=user.id,
        total_bill_amount=request.total_bill_amount,
        points_redeemed=request.points_to_redeem,
        points_discount_amount=discount_amount,
        amount_paid=amount_paid,
        transaction_reference=txn_ref,
        payment_status="SUCCESS",
        paid_at=datetime.utcnow()
    )
    db.add(payment)

    db.commit()

    return BillPaymentResponse(
        payment_id=payment_id,
        bill_number=bill.bill_number,
        consumer_number=request.consumer_number,
        provider_name=provider_name,
        total_bill_amount=request.total_bill_amount,
        points_redeemed=request.points_to_redeem,
        points_discount_amount=discount_amount,
        amount_paid=amount_paid,
        transaction_ref=txn_ref,
        timestamp=datetime.utcnow().strftime("%Y-%m-%d %H:%M"),
        updated_wallet_balance=wallet.current_balance,
        status="SUCCESS"
    )

@router.get("/bills/history/{user_id}", response_model=List[BillPaymentResponse])
def get_payment_history(user_id: str, db: Session = Depends(get_db)):
    payments = (
        db.query(BillPayment)
        .filter(BillPayment.user_id == user_id)
        .order_by(BillPayment.paid_at.desc())
        .all()
    )

    results = []
    for p in payments:
        bill = db.query(ElectricityBill).filter(ElectricityBill.id == p.bill_id).first()
        provider_name = bill.provider.name if bill and bill.provider else "Electricity Board"
        consumer_num = bill.consumer_number if bill else "Unknown"
        bill_num = bill.bill_number if bill else "Unknown"

        results.append(
            BillPaymentResponse(
                payment_id=p.id,
                bill_number=bill_num,
                consumer_number=consumer_num,
                provider_name=provider_name,
                total_bill_amount=p.total_bill_amount,
                points_redeemed=p.points_redeemed,
                points_discount_amount=p.points_discount_amount,
                amount_paid=p.amount_paid,
                transaction_ref=p.transaction_reference,
                timestamp=p.paid_at.strftime("%Y-%m-%d %H:%M") if p.paid_at else "Recent",
                updated_wallet_balance=0,
                status=p.payment_status
            )
        )
    return results
