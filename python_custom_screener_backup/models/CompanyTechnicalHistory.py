from dataclasses import dataclass
from datetime import date
from typing import Optional

@dataclass
class CompanyTechnicalHistory:
    date: date
    price: float
    ema50: Optional[float] = None
    ema100: Optional[float] = None
    ema200: Optional[float] = None
    supertrend_1d: Optional[float] = None
    supertrend_2d: Optional[float] = None
    volume: Optional[int] = None
    delivery_volume: Optional[int] = None