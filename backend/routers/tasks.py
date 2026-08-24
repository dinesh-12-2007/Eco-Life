from typing import List
from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy.orm import Session
from ..database import get_db
from ..models import ServiceTask, TaskStatus, TaskType
from pydantic import BaseModel

router = APIRouter(prefix="/api/tasks", tags=["tasks"])

class TaskResponse(BaseModel):
    id: str
    type: str
    title: str
    location: str
    scheduled_time: str
    distance: str
    priority: str
    status: str
    progress: int

    class Config:
        orm_mode = True

class TaskStatusUpdate(BaseModel):
    status: str
    progress: int = 0

@router.get("", response_model=List[TaskResponse])
def get_tasks(db: Session = Depends(get_db)):
    tasks = db.query(ServiceTask).all()
    if not tasks:
        # Seed initial tasks
        seed_tasks = [
            ServiceTask(id="tsk-01", type=TaskType.COMPLAINT, title="Overflowing Bin at MG Road", location="MG Road Sector 4", scheduled_time="09:30 AM", distance="1.2 km", priority="HIGH", status=TaskStatus.ASSIGNED, progress=0),
            ServiceTask(id="tsk-02", type=TaskType.MISSED_COLLECTION, title="Missed Residential Pickup", location="Indiranagar 100ft Rd", scheduled_time="10:45 AM", distance="2.8 km", priority="MEDIUM", status=TaskStatus.ASSIGNED, progress=0),
            ServiceTask(id="tsk-03", type=TaskType.ROUTINE, title="Sector Commercial Routine Sweep", location="Koramangala 4th Block", scheduled_time="02:00 PM", distance="4.5 km", priority="NORMAL", status=TaskStatus.ASSIGNED, progress=0),
        ]
        db.add_all(seed_tasks)
        db.commit()
        tasks = seed_tasks

    return [
        TaskResponse(
            id=t.id,
            type=t.type.value if hasattr(t.type, "value") else str(t.type),
            title=t.title,
            location=t.location,
            scheduled_time=t.scheduled_time,
            distance=t.distance,
            priority=t.priority,
            status=t.status.value if hasattr(t.status, "value") else str(t.status),
            progress=t.progress
        )
        for t in tasks
    ]

@router.patch("/{task_id}/status", response_model=TaskResponse)
def update_task_status(task_id: str, update_in: TaskStatusUpdate, db: Session = Depends(get_db)):
    task = db.query(ServiceTask).filter(ServiceTask.id == task_id).first()
    if not task:
        raise HTTPException(status_code=404, detail="Task not found")

    status_str = update_in.status.upper()
    if status_str in TaskStatus.__members__:
        task.status = TaskStatus[status_str]
    task.progress = update_in.progress
    db.commit()
    db.refresh(task)

    return TaskResponse(
        id=task.id,
        type=task.type.value if hasattr(task.type, "value") else str(task.type),
        title=task.title,
        location=task.location,
        scheduled_time=task.scheduled_time,
        distance=task.distance,
        priority=task.priority,
        status=task.status.value if hasattr(task.status, "value") else str(task.status),
        progress=task.progress
    )
