# This file is a daily screener which will go through stocks coming up in 
# https://www.screener.in/screens/2618573/companies-with-growth-salesprofitmargin/ 
# and identify additional information for those stocks for easy investment decisions.

from utils.ScreenerCompanyListParser import parse_screener_link
from utils.ScreenerCompanyDetailParser import parse_company_detail

# screener link
screener_link = "https://www.screener.in/screens/2618573/companies-with-growth-salesprofitmargin/"

# Parse the screener link to get the list of companies
companies_metadata = parse_screener_link(screener_link)
print(f"Total companies found: {len(companies_metadata)}")

# Iterate through the list of companies and fetch their details
for company_metadata in companies_metadata[:1]:
    try:
        # Parse the company detail page
        company_detail = parse_company_detail(company_metadata)
        print(f"Company: {company_metadata.company_name}, Details: {company_detail}")
    except Exception as e:
        print(f"Error parsing company {company_metadata.company_name}: {e.with_traceback()}")