import json
from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from .database import engine, Base, SessionLocal
from .models import (
    User,
    UserRole,
    RewardWallet,
    RewardTransaction,
    TransactionType,
    ElectricityProvider,
    SystemConfig,
)
from .auth import get_password_hash
from .routers import auth, wallet, electricity, waste, tasks, complaints

# Create database tables
Base.metadata.create_all(bind=engine)

app = FastAPI(
    title="WasteFlow EcoCycle API",
    description="Smart Municipal Solid Waste Management & Electricity Bill Reward Redemption Backend",
    version="2.0.0"
)

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Mount API Routers
app.include_router(auth.router)
app.include_router(wallet.router)
app.include_router(electricity.router)
app.include_router(waste.router)
app.include_router(tasks.router)
app.include_router(complaints.router)

@app.on_event("startup")
def seed_initial_data():
    db = SessionLocal()
    try:
        # 1. Seed System Conversion Rate
        cfg = db.query(SystemConfig).filter(SystemConfig.key == "conversion_rate").first()
        if not cfg:
            conv_data = {
                "points_per_unit": 10,
                "currency_symbol": "₹",
                "description": "100 Reward Points = ₹10 Electricity Bill Credit"
            }
            db.add(SystemConfig(key="conversion_rate", value_json=json.dumps(conv_data), description="Points to electricity discount rate"))

        # 2. Seed Electricity Providers
        if db.query(ElectricityProvider).count() == 0:
            providers = [
                ElectricityProvider(id="prov-01", name="BESCOM (Bangalore Electricity Supply)", code="BESCOM", state="Karnataka"),
                ElectricityProvider(id="prov-02", name="TANGEDCO (Tamil Nadu Generation & Distribution)", code="TANGEDCO", state="Tamil Nadu"),
                ElectricityProvider(id="prov-03", name="MSEDCL (Mahavitaran Maharashtra)", code="MSEDCL", state="Maharashtra"),
                ElectricityProvider(id="prov-04", name="BSES Yamuna Power Limited", code="BSES-Y", state="Delhi"),
                ElectricityProvider(id="prov-05", name="APSPDCL (Southern Power Distribution AP)", code="APSPDCL", state="Andhra Pradesh"),
                ElectricityProvider(id="prov-06", name="Tata Power DDL", code="TATAPOWER", state="Delhi-NCR"),
            ]
            db.add_all(providers)

        # 3. Seed Mock Users (Citizen, Employee, Admin)
        if not db.query(User).filter(User.id == "u-01").first():
            citizen = User(
                id="u-01",
                email="alex@ecocycle.gov",
                hashed_password=get_password_hash("password123"),
                name="Alex Citizen",
                role=UserRole.CITIZEN,
                zone="Zone B - Green Enclave",
                phone="+91 98765 43210"
            )
            db.add(citizen)
            db.flush()

            # Create Wallet with initial balance
            wallet_u1 = RewardWallet(
                id="wal-u-01",
                user_id="u-01",
                current_balance=1250,
                total_earned=1850,
                total_used=600
            )
            db.add(wallet_u1)

            # Initial transaction history
            tx1 = RewardTransaction(
                id="tx-01",
                wallet_id="wal-u-01",
                user_id="u-01",
                type=TransactionType.EARN,
                points=500,
                description="Organic & Compost Waste Verification - Zone B",
                reference_type="WASTE_LOG",
                reference_id="wl-01"
            )
            tx2 = RewardTransaction(
                id="tx-02",
                wallet_id="wal-u-01",
                user_id="u-01",
                type=TransactionType.EARN,
                points=750,
                description="Dry Recyclables (Plastic & Metal) Drop-off",
                reference_type="WASTE_LOG",
                reference_id="wl-02"
            )
            tx3 = RewardTransaction(
                id="tx-03",
                wallet_id="wal-u-01",
                user_id="u-01",
                type=TransactionType.REDEEM,
                points=600,
                description="Electricity Bill Credit - BESCOM (Cons. #90283471)",
                reference_type="ELECTRICITY_BILL",
                reference_id="PAY-8829"
            )
            tx4 = RewardTransaction(
                id="tx-04",
                wallet_id="wal-u-01",
                user_id="u-01",
                type=TransactionType.EARN,
                points=600,
                description="Hazardous E-Waste Battery Drive Participation",
                reference_type="WASTE_LOG",
                reference_id="wl-03"
            )
            db.add_all([tx1, tx2, tx3, tx4])

        if not db.query(User).filter(User.id == "u-02").first():
            worker = User(
                id="u-02",
                email="marcus@ecocycle.gov",
                hashed_password=get_password_hash("password123"),
                name="Marcus Sanitation",
                role=UserRole.SERVICE_EMPLOYEE,
                zone="Zone A",
                phone="+91 98765 11223"
            )
            db.add(worker)

        if not db.query(User).filter(User.id == "u-03").first():
            admin = User(
                id="u-03",
                email="admin@ecocycle.gov",
                hashed_password=get_password_hash("admin123"),
                name="Elena Vance (Director)",
                role=UserRole.SYSTEM_MANAGEMENT,
                zone="Central Command",
                phone="+91 98765 99887"
            )
            db.add(admin)

        db.commit()
    finally:
        db.close()

@app.get("/api/health")
def health_check():
    return {
        "status": "healthy",
        "service": "WasteFlow EcoCycle API",
        "version": "2.0.0",
        "reward_system": "Electricity Bill Payment Active"
    }
