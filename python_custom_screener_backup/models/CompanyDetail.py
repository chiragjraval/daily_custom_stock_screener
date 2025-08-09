from dataclasses import dataclass
from typing import List
from models.CompanyMetadata import CompanyMetadata
from models.CompanyResult import CompanyResult

@dataclass
class CompanyDetail:
    def __init__(
        self,
        metadata: CompanyMetadata,
        past_quarterly_results: List[CompanyResult],
        latest_quarterly_result: CompanyResult,
        past_yearly_result: List[CompanyResult],
        ttm_yearly_result: CompanyResult
    ):
        self.metadata = metadata
        self.past_quarterly_results = past_quarterly_results
        self.latest_quarterly_result = latest_quarterly_result
        self.past_yearly_result = past_yearly_result
        self.ttm_yearly_result = ttm_yearly_result

    def __repr__(self):
        return (
            f"CompanyDetail(metadata={self.metadata}, "
            f"past_quarterly_results={self.past_quarterly_results}, "
            f"latest_quarterly_result={self.latest_quarterly_result}, "
            f"past_yearly_result={self.past_yearly_result}, "
            f"ttm_yearly_result={self.ttm_yearly_result})"
        )
