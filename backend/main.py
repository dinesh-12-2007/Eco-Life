import json
from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from .database import engine, Base, SessionLocal
from .models import (
    ElectricityProvider,
    SystemConfig,
)
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
