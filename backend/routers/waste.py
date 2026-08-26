import uuid
from datetime import datetime
from typing import List
from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy.orm import Session
from ..database import get_db
from ..models import User, WasteLog, RewardWallet, RewardTransaction, TransactionType
from ..schemas import WasteLogCreate, WasteLogResponse
from ..auth import require_current_user

router = APIRouter(prefix="/api/waste", tags=["waste"])

WASTE_MULTIPLIERS = {
    "PLASTIC": 15,
    "PAPER": 5,
    "METAL": 20,
    "ORGANIC": 2,
}

@router.post("/log", response_model=WasteLogResponse)
def log_waste(
    waste_in: WasteLogCreate,
    db: Session = Depends(get_db),
    user: User = Depends(require_current_user)
):
    if waste_in.weight_kg <= 0:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail="Weight must be greater than 0 kg"
        )

    waste_type_key = waste_in.waste_type.strip().upper()
    if waste_type_key not in WASTE_MULTIPLIERS:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail=f"Unsupported waste type: {waste_in.waste_type}. Supported types are: {', '.join(WASTE_MULTIPLIERS.keys())}"
        )

    multiplier = WASTE_MULTIPLIERS[waste_type_key]
    calculated_points = int(waste_in.weight_kg * multiplier)

    log_id = f"wl-{uuid.uuid4().hex[:8]}"
    log = WasteLog(
        id=log_id,
        user_id=user.id,
        zone=waste_in.zone,
        waste_type=waste_in.waste_type,
        weight_kg=waste_in.weight_kg,
        points_awarded=calculated_points,
        verified_by=user.name
    )
    db.add(log)

    # Award points to citizen's wallet
    wallet = db.query(RewardWallet).filter(RewardWallet.user_id == user.id).first()
    if not wallet:
        wallet = RewardWallet(id=f"wal-{user.id}", user_id=user.id, current_balance=0, total_earned=0, total_used=0)
        db.add(wallet)
        db.flush()

    wallet.current_balance += calculated_points
    wallet.total_earned += calculated_points

    # Record EARN transaction in ledger
    tx = RewardTransaction(
        id=f"tx-{uuid.uuid4().hex[:8]}",
        wallet_id=wallet.id,
        user_id=user.id,
        type=TransactionType.EARN,
        points=calculated_points,
        description=f"Recycled {waste_in.weight_kg}kg of {waste_in.waste_type} in {waste_in.zone}",
        reference_type="WASTE_LOG",
        reference_id=log_id
    )
    db.add(tx)
    db.commit()
    db.refresh(log)

    return log

