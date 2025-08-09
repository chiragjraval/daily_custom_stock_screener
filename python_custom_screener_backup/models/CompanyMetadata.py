from dataclasses import dataclass
from Constants import SCREENER_BASE_URL

@dataclass
class CompanyMetadata:
    def __init__(self, screener_company_id: str, company_code: str, company_name: str):
        self.screener_company_id = screener_company_id
        self.company_code = company_code
        self.company_name = company_name
        self.screener_company_link = SCREENER_BASE_URL + "/company/" + company_code
        self.screener_price_history_link = SCREENER_BASE_URL + "/api/company/" + screener_company_id + "/chart/?q=Price-DMA50-DMA200-Volume&days=365"

    def __repr__(self):
        return (f"CompanyMetadata(screener_company_id={self.screener_company_id!r}, "
                f"company_code={self.company_code!r}, company_name={self.company_name!r}, "
                f"screener_company_link={self.screener_company_link!r}, "
                f"screener_price_history_link={self.screener_price_history_link!r})")
