import React, { useEffect, useState, useMemo } from "react";

function SummaryGrid() {
  const [data, setData] = useState([]);
  const [sortConfig, setSortConfig] = useState({ key: "totalScore", direction: "desc" });

  useEffect(() => {
    fetch(import.meta.env.BASE_URL + "/screener-data/prod/summary.json")
      .then((res) => res.json())
      .then((json) => setData(json.companies))
      .catch((err) => console.error("Error loading summary.json:", err));
  }, []);

  const getValue = (item, key) => {
    switch (key) {
      case "marketCap": return item.attributes.marketCap;
      case "peRatio": return item.attributes.peRatio;
      case "technicalScore": return item.companyScore.totalTechnicalScore;
      case "fundamentalScore": return item.companyScore.totalFundamentalScore;
      case "totalScore": return item.companyScore.totalScore;
      default: return item[key];
    }
  };

  const sortedData = useMemo(() => {
    let sortableData = [...data];
    if (sortConfig.key !== null) {
      sortableData.sort((a, b) => {
        const aVal = getValue(a, sortConfig.key);
        const bVal = getValue(b, sortConfig.key);

        if (aVal < bVal) return sortConfig.direction === "asc" ? -1 : 1;
        if (aVal > bVal) return sortConfig.direction === "asc" ? 1 : -1;
        return 0;
      });
    }
    return sortableData;
  }, [data, sortConfig]);

  const requestSort = (key) => {
    let direction = "asc";
    if (sortConfig.key === key && sortConfig.direction === "asc") {
      direction = "desc";
    }
    setSortConfig({ key, direction });
  };

  const getSortIcon = (key) => {
    if (sortConfig.key !== key) return "";
    return sortConfig.direction === "asc" ? "▲" : "▼";
  };

  return (
    <div className="container mt-4">
      <h2 className="mb-3">Summary Grid</h2>
      <table className="table table-striped table-hover table-bordered">
        <thead className="table-dark">
          <tr>
            <th className="text-center" onClick={() => requestSort("screenerCompanyId")}>
              Screener ID {getSortIcon("screenerCompanyId")}
            </th>
            <th className="text-center" onClick={() => requestSort("companyCode")}>
              Screener Link {getSortIcon("companyCode")}
            </th>
            <th className="text-center" onClick={() => requestSort("companyName")}>
              Name {getSortIcon("companyName")}
            </th>
            <th className="text-center" onClick={() => requestSort("marketCap")}>
              Market Cap (Cr.) {getSortIcon("marketCap")}
            </th>
            <th className="text-center" onClick={() => requestSort("peRatio")}>
              PE Ratio {getSortIcon("peRatio")}
            </th>
            <th className="text-center" onClick={() => requestSort("technicalScore")}>
              Technical Score {getSortIcon("technicalScore")}
            </th>
            <th className="text-center" onClick={() => requestSort("fundamentalScore")}>
              Fundamental Score {getSortIcon("fundamentalScore")}
            </th>
            <th className="text-center" onClick={() => requestSort("totalScore")}>
              Total Score {getSortIcon("totalScore")}
            </th>
          </tr>
        </thead>
        <tbody>
          {sortedData.map((item) => (
            <tr key={item.screenerCompanyId}>
              <td className="text-end">{item.screenerCompanyId}</td>
              <td>
                <a href={item.screenerCompanyLink} target="_blank" rel="noreferrer">
                  {item.companyCode}
                </a>
              </td>
              <td>{item.companyName}</td>
              <td className="text-end">{item.attributes.marketCap.toLocaleString()}</td>
              <td className="text-end">{item.attributes.peRatio.toFixed(2)}</td>
              <td className="text-end">{item.companyScore.totalTechnicalScore}</td>
              <td className="text-end">{item.companyScore.totalFundamentalScore}</td>
              <td className="text-end fw-bold">{item.companyScore.totalScore}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default SummaryGrid;