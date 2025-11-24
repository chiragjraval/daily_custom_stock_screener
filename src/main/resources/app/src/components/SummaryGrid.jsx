import React, { useEffect, useState } from "react";

function SummaryGrid() {
  const [data, setData] = useState([]);

  useEffect(() => {
    // PUBLIC_URL ensures correct path in dev and production
    fetch(import.meta.env.BASE_URL + "/screener-data/prod/summary.json")
      .then((res) => res.json())
      .then((json) => setData(json.companies))
      .catch((err) => console.error("Error loading summary.json:", err));
  }, []);

  return (
    <div>
      <h2>Summary Grid</h2>
      <table border="1" cellPadding="8" style={{ borderCollapse: "collapse" }}>
        <thead>
          <tr>
            <th>ID</th>
            <th>Code</th>
            <th>Name</th>
            <th>Company Link</th>
            <th>Technical History Link</th>
            <th>Quarterly Results Link</th>
          </tr>
        </thead>
        <tbody>
          {data.map((item) => (
            <tr key={item.screener_company_id}>
              <td>{item.screener_company_id}</td>
              <td>{item.company_code}</td>
              <td>{item.company_name}</td>
              <td>{item.screener_company_link}</td>
              <td>{item.screener_price_history_link}</td>
              <td>{item.screener_quarterly_results_link}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default SummaryGrid;