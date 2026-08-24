import json
from typing import List
from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy.orm import Session
from ..database import get_db
from ..models import User, RewardWallet, RewardTransaction, SystemConfig, UserRole
from ..schemas import (
    RewardWalletResponse,
    RewardTransactionResponse,
    ConversionConfigResponse,
    ConversionConfigUpdate,
)
from ..auth import get_current_user, require_current_user

router = APIRouter(prefix="/api", tags=["wallet"])

DEFAULT_POINTS_PER_UNIT = 10 # 10 pts = 1 INR (100 pts = ₹10)

def get_db_conversion_config(db: Session) -> dict:
    cfg = db.query(SystemConfig).filter(SystemConfig.key == "conversion_rate").first()
    if cfg:
        try:
            return json.loads(cfg.value_json)
        except Exception:
            pass
    return {
        "points_per_unit": 10,
        "currency_symbol": "₹",
        "description": "100 Reward Points = ₹10 Electricity Bill Credit"
    }

@router.get("/config/conversion-rate", response_model=ConversionConfigResponse)
def get_conversion_rate(db: Session = Depends(get_db)):
    config_dict = get_db_conversion_config(db)
    return ConversionConfigResponse(
        points_per_unit=config_dict.get("points_per_unit", 10),
        currency_symbol=config_dict.get("currency_symbol", "₹"),
        description=config_dict.get("description", "100 Reward Points = ₹10 Electricity Bill Credit")
    )

@router.put("/config/conversion-rate", response_model=ConversionConfigResponse)
def update_conversion_rate(
    update_data: ConversionConfigUpdate,
    user: User = Depends(require_current_user),
    db: Session = Depends(get_db)
):
    # Only Admin/Management can change system conversion rates
    if user.role != UserRole.SYSTEM_MANAGEMENT and user.role != UserRole.SERVICE_EMPLOYEE:
        raise HTTPException(
            status_code=status.HTTP_403_FORBIDDEN,
            detail="Only administrators and municipality managers can update conversion rates."
        )

    rate_text = update_data.description or f"{update_data.points_per_unit * 10} Reward Points = {update_data.currency_symbol}10 Electricity Bill Credit"
    val = {
        "points_per_unit": update_data.points_per_unit,
        "currency_symbol": update_data.currency_symbol,
        "description": rate_text
    }

    cfg = db.query(SystemConfig).filter(SystemConfig.key == "conversion_rate").first()
    if not cfg:
        cfg = SystemConfig(key="conversion_rate", value_json=json.dumps(val), description="Points to electricity credit rate")
        db.add(cfg)
    else:
        cfg.value_json = json.dumps(val)
        cfg.description = rate_text

    db.commit()
    return ConversionConfigResponse(
        points_per_unit=update_data.points_per_unit,
        currency_symbol=update_data.currency_symbol,
        description=rate_text
    )

@router.get("/wallet/{user_id}", response_model=RewardWalletResponse)
def get_user_wallet(user_id: str, db: Session = Depends(get_db)):
    wallet = db.query(RewardWallet).filter(RewardWallet.user_id == user_id).first()
    if not wallet:
        # Create if not found for demo consistency
        wallet = RewardWallet(
            id=f"wal-{user_id}",
            user_id=user_id,
            current_balance=1250,
            total_earned=1850,
            total_used=600
        )
        db.add(wallet)
        db.commit()
        db.refresh(wallet)

    txs = (
        db.query(RewardTransaction)
        .filter(RewardTransaction.user_id == user_id)
        .order_by(RewardTransaction.created_at.desc())
        .all()
    )

    tx_responses = [
        RewardTransactionResponse(
            id=t.id,
            type=t.type.value if hasattr(t.type, "value") else str(t.type),
            points=t.points,
            description=t.description,
            date=t.created_at.strftime("%Y-%m-%d %H:%M") if t.created_at else "Recent",
            reference_type=t.reference_type,
            reference_id=t.reference_id
        )
        for t in txs
    ]

    return RewardWalletResponse(
        current_points=wallet.current_balance,
        total_earned=wallet.total_earned,
        total_used=wallet.total_used,
        transactions=tx_responses
    )

@router.get("/wallet/{user_id}/transactions", response_model=List[RewardTransactionResponse])
def get_user_transactions(user_id: str, db: Session = Depends(get_db)):
    txs = (
        db.query(RewardTransaction)
        .filter(RewardTransaction.user_id == user_id)
        .order_by(RewardTransaction.created_at.desc())
        .all()
    )

    return [
        RewardTransactionResponse(
            id=t.id,
            type=t.type.value if hasattr(t.type, "value") else str(t.type),
            points=t.points,
            description=t.description,
            date=t.created_at.strftime("%Y-%m-%d %H:%M") if t.created_at else "Recent",
            reference_type=t.reference_type,
            reference_id=t.reference_id
        )
        for t in txs
    ]
