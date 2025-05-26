from dataclasses import dataclass

@dataclass
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

    def __repr__(self):
        return (f"CompanyResult(sales={self.sales!r}, expenses={self.expenses!r}, "
                f"operating_profit={self.operating_profit!r}, opm_percent={self.opm_percent!r}, "
                f"other_income={self.other_income!r}, interest={self.interest!r}, "
                f"depreciation={self.depreciation!r}, profit_before_tax={self.profit_before_tax!r}, "
                f"tax_percent={self.tax_percent!r}, net_profit={self.net_profit!r}, "
                f"eps_in_rs={self.eps_in_rs!r}, dividend_payout_percent={self.dividend_payout_percent!r})")

