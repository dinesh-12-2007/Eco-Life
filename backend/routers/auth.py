from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy.orm import Session
from ..database import get_db
from ..models import User, RewardWallet, UserRole
from ..schemas import UserRegister, UserLogin, Token, UserResponse
from ..auth import get_password_hash, verify_password, create_access_token, require_current_user
import uuid

router = APIRouter(prefix="/api/auth", tags=["auth"])

@router.post("/register", response_model=Token)
def register(user_in: UserRegister, db: Session = Depends(get_db)):
    existing = db.query(User).filter(User.email == user_in.email).first()
    if existing:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail="Email already registered"
        )

    user_id = f"u-{uuid.uuid4().hex[:8]}"
    role_enum = UserRole.CITIZEN
    if user_in.role == "SERVICE_EMPLOYEE":
        role_enum = UserRole.SERVICE_EMPLOYEE
    elif user_in.role == "SYSTEM_MANAGEMENT":
        role_enum = UserRole.SYSTEM_MANAGEMENT

    new_user = User(
        id=user_id,
        email=user_in.email,
        hashed_password=get_password_hash(user_in.password),
        name=user_in.name,
        role=role_enum,
        zone=user_in.zone,
        phone=user_in.phone
    )
    db.add(new_user)
    db.flush()

    # Create associated Reward Wallet for citizen
    wallet = RewardWallet(
        id=f"wal-{user_id}",
        user_id=user_id,
        current_balance=250, # Initial sign-up bonus points
        total_earned=250,
        total_used=0
    )
    db.add(wallet)
    db.commit()
    db.refresh(new_user)

    token = create_access_token({"sub": new_user.id, "role": new_user.role.value})
    return Token(
        access_token=token,
        token_type="bearer",
        user_id=new_user.id,
        role=new_user.role.value,
        name=new_user.name
    )

@router.post("/login", response_model=Token)
def login(login_data: UserLogin, db: Session = Depends(get_db)):
    user = db.query(User).filter(User.email == login_data.email).first()
    if not user or not verify_password(login_data.password, user.hashed_password):
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Invalid email or password"
        )

    token = create_access_token({"sub": user.id, "role": user.role.value})
    return Token(
        access_token=token,
        token_type="bearer",
        user_id=user.id,
        role=user.role.value,
        name=user.name
    )

@router.get("/me", response_model=UserResponse)
def get_me(user: User = Depends(require_current_user), db: Session = Depends(get_db)):
    wallet = db.query(RewardWallet).filter(RewardWallet.user_id == user.id).first()
    balance = wallet.current_balance if wallet else 0
    return UserResponse(
        id=user.id,
        email=user.email,
        name=user.name,
        role=user.role.value,
        zone=user.zone,
        phone=user.phone,
        balance_points=balance,
        recycled_kg_ytd=45.0
    )
