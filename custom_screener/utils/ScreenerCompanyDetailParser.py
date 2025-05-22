import requests
import re
import pandas as pd
from bs4 import BeautifulSoup
from typing import List
from models.CompanyMetadata import CompanyMetadata
from models.CompanyResult import CompanyResult, SCRENEER_HEADER_TO_ATTR_MAP
from models.CompanyDetail import CompanyDetail

def safe_float(value):
    try:
        return float(value.replace(',', '').replace('%', '').strip() or '0')
    except Exception:
        return 0.0

def extract_quarterly_results(soup: BeautifulSoup) -> tuple[CompanyResult, List[CompanyResult]]:
    """
    Extracts the quarterly results from the given BeautifulSoup object.
    Args:
        soup (BeautifulSoup): The BeautifulSoup object containing the HTML content.
    Returns:
        tuple: A tuple containing the latest quarterly result and a list of past quarterly results.
    """
        
    # Find the tag with name 'section' and id attribute 'quarters'
    quarters_section = soup.find('section', id='quarters')
    
    # Find the table within the section
    table = quarters_section.find('table')

    # Extract headers which would be in the format "mmm yyyy"
    # Convert them to date objects
    headers = [header.text.strip() for header in table.find_all('th') if re.match(r'^\w{3} \d{4}$', header.text.strip())]
    headers = [pd.to_datetime(header, format='%b %Y') for header in headers]

    # Extract rows
    rows = table.find_all('tr')[1:]

    # First cell contians the name of the attribute like "Sales", "Expenses" etc.
    # The rest of the cells contain the values for each quarter
    # Last cell contains the value of latest quarter
    # Populate latest_quarterly_result and past_quarterly_results using SCRENEER_HEADER_TO_ATTR_MAP
    for row in rows:
        cells = row.find_all('td')
    
        # Create CompanyResult object for latest quarterly result
        latest_quarterly_result = CompanyResult()
        
        # Create a list of CompanyResult objects for past quarterly results
        # The length of the list is len(cells) - 2 because the first cell is the attribute name
        past_quarterly_results = [CompanyResult() for _ in range(len(cells) - 2)]

        if len(cells) > 1:
            # The first cell contains the name of the attribute
            # Remove any + or - signs and strip whitespace
            attr_name = cells[0].text.strip()
            attr_name = re.sub(r'[\+\-]', '', attr_name).strip()
            
            # Check if the attribute name is in the mapping
            if attr_name in SCRENEER_HEADER_TO_ATTR_MAP:
                # Get the corresponding attribute name
                attr = SCRENEER_HEADER_TO_ATTR_MAP[attr_name]
                
                # Extract values for each quarter
                for i, cell in enumerate(cells[1:]):
                    value = cell.text.strip()
                    if i == 0:
                        latest_quarterly_result.__setattr__(attr, safe_float(value))
                    else:
                        past_quarterly_results[i-1].__setattr__(attr, safe_float(value))

    return latest_quarterly_result, past_quarterly_results

def extract_yearly_results(soup: BeautifulSoup) -> tuple[CompanyResult, List[CompanyResult]]:
    """
    Extracts the yearly results from the given BeautifulSoup object.
    Args:
        soup (BeautifulSoup): The BeautifulSoup object containing the HTML content.
    Returns:
        tuple: A tuple containing the TTM yearly result and a list of past yearly results.
    """

    # Find the tag with name 'section' and id attribute 'profit-loss'
    quarters_section = soup.find('section', id='profit-loss')
    
    # Find the table within the section
    table = quarters_section.find('table')

    # Extract headers which would be in the format "mmm yyyy"
    # Convert them to date objects
    headers = [header.text.strip() for header in table.find_all('th') if re.match(r'^\w{3} \d{4}$', header.text.strip())]
    headers = [pd.to_datetime(header, format='%b %Y') for header in headers]

    # Extract rows
    rows = table.find_all('tr')[1:]

    # First cell contians the name of the attribute like "Sales", "Expenses" etc.
    # The rest of the cells contain the values for each quarter
    # Last cell contains the value of latest quarter
    # Populate latest_quarterly_result and past_quarterly_results using SCRENEER_HEADER_TO_ATTR_MAP
    for row in rows:
        cells = row.find_all('td')

        # Create CompanyResult object for latest quarterly result
        ttm_yearly_result = CompanyResult()
        
        # Create a list of CompanyResult objects for past quarterly results
        # The length of the list is len(cells) - 2 because the first cell is the attribute name
        past_yearly_results = [CompanyResult() for _ in range(len(cells) - 2)]
        
        if len(cells) > 1:
            # The first cell contains the name of the attribute
            # Remove any + or - signs and strip whitespace
            attr_name = cells[0].text.strip()
            attr_name = re.sub(r'[\+\-]', '', attr_name).strip()
            
            # Check if the attribute name is in the mapping
            if attr_name in SCRENEER_HEADER_TO_ATTR_MAP:
                # Get the corresponding attribute name
                attr = SCRENEER_HEADER_TO_ATTR_MAP[attr_name]
                
                # Extract values for each quarter
                for i, cell in enumerate(cells[1:]):
                    value = cell.text.strip()
                    if i == 0:
                        ttm_yearly_result.__setattr__(attr, safe_float(value))
                    else:
                        past_yearly_results[i-1].__setattr__(attr, safe_float(value))

    return ttm_yearly_result, past_yearly_results

def parse_company_detail(metadata: CompanyMetadata) -> CompanyDetail:
    """
    Receives a CompanyMetadata object and returns a CompanyDetail object.
    Args:
        metadata (CompanyMetadata): The CompanyMetadata object containing company details.
    Returns:
        CompanyDetail: A CompanyDetail object containing detailed financial results.
    """
    # Fetch the HTML content
    company_url = metadata.screener_company_link
    response = requests.get(company_url)    
    
    # Parse the HTML
    soup = BeautifulSoup(response.text, 'html.parser')

    # Initialize objects to be returned
    latest_quarterly_result, past_quarterly_results = extract_quarterly_results(soup)
    ttm_yearly_result, past_yearly_results = extract_yearly_results(soup)

    # Create a CompanyDetail object
    company_detail = CompanyDetail(
        metadata=metadata,
        past_quarterly_results=past_quarterly_results,
        latest_quarterly_result=latest_quarterly_result,
        past_yearly_result=past_yearly_results,
        ttm_yearly_result=ttm_yearly_result
    )

    return company_detail