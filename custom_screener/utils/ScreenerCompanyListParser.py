import requests
import re
import pandas as pd
from bs4 import BeautifulSoup
from typing import List
from models.CompanyMetadata import CompanyMetadata
from Constants import SCREENER_BASE_URL

def extract_total_page_num(screener_link: str) -> int:
    """
    Extracts the total number of pages from the soup object.

    Args:
        soup (BeautifulSoup): The BeautifulSoup object containing the HTML content.

    Returns:
        int: The total number of pages.
    """
    # Fetch the HTML content
    response = requests.get(screener_link)

    # Parse the HTML
    soup = BeautifulSoup(response.text, 'html.parser')

    # Find the div containing the text "results found: Showing page"
    results_div = soup.find('div', string=lambda text: text and "results found: Showing page" in text)

    # Extract total pages
    if results_div:
        match = re.search(r"(\d+)\s+results found: Showing page\s+(\d+)\s+of\s+(\d+)", results_div.text)
        if match:
            return int(match.group(3))
    return 0

def parse_page_num(screener_link: str, page_num: int) -> List[CompanyMetadata]:
    """
    Receives a screener link and page number as parameters.

    Args:
        screener_link (str): The URL of the screener.
        page_num (int): The page number to parse.

    Returns:
        List[CompanyMetadata]: A list of CompanyMetadata objects.
    """
    # Construct the URL for the specific page
    page_url = f"{screener_link}?sort=name&order=asc&page={page_num}"
    
    # Fetch the HTML content
    response = requests.get(page_url)

    # Parse the HTML
    soup = BeautifulSoup(response.text, 'html.parser')

    # Find the table (adjust the selector based on the table structure)
    table = soup.find('table')

    # Extract table rows
    rows = table.find_all('tr')

    # Extract headers
    headers = [header.text.strip() for header in rows[0].find_all('th')]

    # Extract data rows with both text and hyperlink
    data = []
    for row in rows[1:]:
        # Get the value of data-row-company-id attribute
        data_row_company_id = row.get('data-row-company-id')
        cols = []
        for col in row.find_all('td'):
            # Check if the column contains an <a> tag
            link = col.find('a')
            if link:
                # Extract both text and hyperlink
                href_split = link['href'].split('/')
                company_code = href_split[2] if len(href_split) > 1 else None
                cols.append((link.text.strip(), SCREENER_BASE_URL + link['href'], company_code, data_row_company_id))
            else:
                cols.append(col.text.strip())
        data.append(cols)

    # Create a DataFrame
    df = pd.DataFrame(data, columns=headers)

    # If "Name" column contains tuples, split them into separate columns
    if 'Name' in df.columns:
        df[['Company_Name', 'Company_Link', 'Company_Code', 'Company_Id']] = pd.DataFrame(df['Name'].tolist(), index=df.index)
        df.drop(columns=['Name'], inplace=True)

    # Extract the company metadata
    # Create a list of CompanyMetadata objects
    company_list = []
    for _, row in df.iterrows():
        company = CompanyMetadata(
            screener_company_id=row.get('Company_Id'),
            screener_company_link=row.get('Company_Link'),
            company_name=row.get('Company_Name'),
            company_code=row.get('Company_Code')
        )
        company_list.append(company)
    return company_list

def parse_screener_link(screener_link: str) -> List[CompanyMetadata]:
    """
    Receives a screener link as parameter.

    Args:
        screener_link (str): The URL of the screener.

    Returns:
        List[CompanyMetadata]: A list of CompanyMetadata objects.
    """
    # Extract variables from the text
    total_page_num: int = extract_total_page_num(screener_link)
    
    # Initialize an empty list to store the company metadata
    company_list: List[CompanyMetadata] = []
    
    # Loop through each page and parse the data
    for page_num in range(1, total_page_num + 1):
        company_list.extend(parse_page_num(screener_link, page_num))
    
    # Return the list of company metadata
    return company_list