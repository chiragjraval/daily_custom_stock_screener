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
            <th>Market Cap</th>
            <th>PE Ratio</th>
            <th>Technical Score</th>
            <th>Fundamental Score</th>
            <th>Total Score</th>
          </tr>
        </thead>
        <tbody>
          {data.map((item) => (
            <tr key={item.screenerCompanyId}>
              <td>{item.screenerCompanyId}</td>
              <td>{item.companyCode}</td>
              <td>{item.companyName}</td>
              <td>{item.screenerCompanyLink}</td>
              <td>{item.attributes.marketCap}</td>
              <td>{item.attributes.peRatio}</td>
              <td>{item.companyScore.totalTechnicalScore}</td>
              <td>{item.companyScore.totalFundamentalScore}</td>
              <td>{item.companyScore.totalScore}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default SummaryGrid;