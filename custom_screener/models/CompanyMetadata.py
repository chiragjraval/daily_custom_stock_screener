class CompanyMetadata:
    def __init__(self, screener_company_id: str, company_code: str, company_name: str, screener_company_link: str):
        self.screener_company_id = screener_company_id
        self.company_code = company_code
        self.company_name = company_name
        self.screener_company_link = screener_company_link

    def __repr__(self):
        return (f"CompanyMetadata(screener_company_id={self.screener_company_id!r}, "
                f"company_code={self.company_code!r}, company_name={self.company_name!r}, "
                f"screener_company_link={self.screener_company_link!r})")
