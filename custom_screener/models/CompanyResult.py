# Mapping of human readable names to attribute names
SCRENEER_HEADER_TO_ATTR_MAP = {
    "Sales": "sales",
    "Expenses": "expenses",
    "Operating Profit": "operating_profit",
    "OPM %": "opm_percent",
    "Other Income": "other_income",
    "Interest": "interest",
    "Depreciation": "depreciation",
    "Profit before tax": "profit_before_tax",
    "Tax %": "tax_percent",
    "Net Profit": "net_profit",
    "EPS in Rs": "eps_in_rs",
    "Dividend Payout %": "dividend_payout_percent"
}

class CompanyResult:
    def __init__(
        self,
        sales: float = 0.0,
        expenses: float = 0.0,
        operating_profit: float = 0.0,
        opm_percent: float = 0.0,
        other_income: float = 0.0,
        interest: float = 0.0,
        depreciation: float = 0.0,
        profit_before_tax: float = 0.0,
        tax_percent: float = 0.0,
        net_profit: float = 0.0,
        eps_in_rs: float = 0.0,
        dividend_payout_percent: float = 0.0
    ):
        self.sales = sales
        self.expenses = expenses
        self.operating_profit = operating_profit
        self.opm_percent = opm_percent
        self.other_income = other_income
        self.interest = interest
        self.depreciation = depreciation
        self.profit_before_tax = profit_before_tax
        self.tax_percent = tax_percent
        self.net_profit = net_profit
        self.eps_in_rs = eps_in_rs
        self.dividend_payout_percent = dividend_payout_percent

