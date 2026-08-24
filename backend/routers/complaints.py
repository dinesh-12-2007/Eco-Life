import uuid
from typing import List
from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy.orm import Session
from ..database import get_db
from ..models import Complaint, ComplaintPriority, ComplaintStatus, User
from ..schemas import ComplaintCreate, ComplaintResponse
from ..auth import require_current_user

router = APIRouter(prefix="/api/complaints", tags=["complaints"])

@router.get("", response_model=List[ComplaintResponse])
def get_complaints(db: Session = Depends(get_db)):
    complaints = db.query(Complaint).order_by(Complaint.created_at.desc()).all()
    if not complaints:
        # Seed initial complaints
        seed_complaints = [
            Complaint(
                id="cmp-01",
                user_id="u-01",
                title="Overflowing Community Bin",
                description="The waste bin at Sector 4 has not been cleared for 2 days.",
                location="Sector 4 Main Market",
                priority=ComplaintPriority.HIGH,
                status=ComplaintStatus.ASSIGNED,
                assigned_crew="Crew Charlie-2"
            ),
            Complaint(
                id="cmp-02",
                user_id="u-01",
                title="Hazardous Glass Waste Dumped",
                description="Broken bottles and medical syringes dumped on sidewalk.",
                location="8th Cross Road Corner",
                priority=ComplaintPriority.HIGH,
                status=ComplaintStatus.PENDING,
                assigned_crew="Unassigned"
            ),
            Complaint(
                id="cmp-03",
                user_id="u-01",
                title="Missed Dry Waste Collection",
                description="Weekly dry recyclable pickup missed on our street.",
                location="Lane 3, Greenview Enclave",
                priority=ComplaintPriority.MEDIUM,
                status=ComplaintStatus.RESOLVED,
                assigned_crew="Crew Alpha-1"
            )
        ]
        db.add_all(seed_complaints)
        db.commit()
        complaints = seed_complaints

    return [
        ComplaintResponse(
            id=c.id,
            title=c.title,
            description=c.description,
            location=c.location,
            priority=c.priority.value if hasattr(c.priority, "value") else str(c.priority),
            status=c.status.value if hasattr(c.status, "value") else str(c.status),
            assigned_crew=c.assigned_crew,
            created_at=c.created_at
        )
        for c in complaints
    ]

@router.post("", response_model=ComplaintResponse)
def create_complaint(
    complaint_in: ComplaintCreate,
    db: Session = Depends(get_db),
    user: User = Depends(require_current_user)
):
    prio = ComplaintPriority.MEDIUM
    if complaint_in.priority.upper() == "HIGH":
        prio = ComplaintPriority.HIGH
    elif complaint_in.priority.upper() == "LOW":
        prio = ComplaintPriority.LOW

    cmp_id = f"cmp-{uuid.uuid4().hex[:8]}"
    new_cmp = Complaint(
        id=cmp_id,
        user_id=user.id,
        title=complaint_in.title,
        description=complaint_in.description,
        location=complaint_in.location,
        priority=prio,
        status=ComplaintStatus.PENDING,
        assigned_crew="Auto-Dispatch Pending"
    )
    db.add(new_cmp)
    db.commit()
    db.refresh(new_cmp)

    return ComplaintResponse(
        id=new_cmp.id,
        title=new_cmp.title,
        description=new_cmp.description,
        location=new_cmp.location,
        priority=new_cmp.priority.value,
        status=new_cmp.status.value,
        assigned_crew=new_cmp.assigned_crew,
        created_at=new_cmp.created_at
    )

@router.patch("/{complaint_id}/resolve", response_model=ComplaintResponse)
def resolve_complaint(complaint_id: str, db: Session = Depends(get_db)):
    cmp = db.query(Complaint).filter(Complaint.id == complaint_id).first()
    if not cmp:
        raise HTTPException(status_code=404, detail="Complaint not found")

    cmp.status = ComplaintStatus.RESOLVED
    db.commit()
    db.refresh(cmp)

    return ComplaintResponse(
        id=cmp.id,
        title=cmp.title,
        description=cmp.description,
        location=cmp.location,
        priority=cmp.priority.value,
        status=cmp.status.value,
        assigned_crew=cmp.assigned_crew,
        created_at=cmp.created_at
    )
