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

@router.post("/log", response_model=WasteLogResponse)
def log_waste(
    waste_in: WasteLogCreate,
    db: Session = Depends(get_db),
    user: User = Depends(require_current_user)
):
    log_id = f"wl-{uuid.uuid4().hex[:8]}"
    log = WasteLog(
        id=log_id,
        user_id=user.id,
        zone=waste_in.zone,
        waste_type=waste_in.waste_type,
        weight_kg=waste_in.weight_kg,
        points_awarded=waste_in.points,
        verified_by=user.name
    )
    db.add(log)

    # Award points to citizen's wallet
    wallet = db.query(RewardWallet).filter(RewardWallet.user_id == user.id).first()
    if not wallet:
        wallet = RewardWallet(id=f"wal-{user.id}", user_id=user.id, current_balance=0, total_earned=0, total_used=0)
        db.add(wallet)
        db.flush()

    wallet.current_balance += waste_in.points
    wallet.total_earned += waste_in.points

    # Record EARN transaction in ledger
    tx = RewardTransaction(
        id=f"tx-{uuid.uuid4().hex[:8]}",
        wallet_id=wallet.id,
        user_id=user.id,
        type=TransactionType.EARN,
        points=waste_in.points,
        description=f"Recycled {waste_in.weight_kg}kg of {waste_in.waste_type} in {waste_in.zone}",
        reference_type="WASTE_LOG",
        reference_id=log_id
    )
    db.add(tx)
    db.commit()
    db.refresh(log)

    return log
