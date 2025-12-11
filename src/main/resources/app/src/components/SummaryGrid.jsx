import React, { useEffect, useState, useMemo } from "react";

function SummaryGrid() {
  const [generatedAt, setGeneratedAt] = useState(new Date());
  const [data, setData] = useState([]);
  const [sortConfig, setSortConfig] = useState({ key: "totalScore", direction: "desc" });
  const [filters, setFilters] = useState({
    marketCapMin: "",
    marketCapMax: "",
    peRatioMin: "",
    peRatioMax: "",
    technicalScoreMin: "",
    technicalScoreMax: "",
    totalScoreMin: "",
    totalScoreMax: "",
    companyName: ""
  });

  useEffect(() => {
    fetch(import.meta.env.BASE_URL + "/screener-data/prod/summary.json")
      .then((res) => res.json())
      .then((json) => { setData(json.companies); setGeneratedAt(json.generatedAt); })
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

  const filteredData = useMemo(() => {
    return data.filter((item) => {
      const marketCap = item.attributes.marketCap;
      const peRatio = item.attributes.peRatio;
      const techScore = item.companyScore.totalTechnicalScore;
      const fundScore = item.companyScore.totalFundamentalScore;
      const totalScore = item.companyScore.totalScore;

      if (filters.marketCapMin && marketCap < parseFloat(filters.marketCapMin)) return false;
      if (filters.marketCapMax && marketCap > parseFloat(filters.marketCapMax)) return false;
      if (filters.peRatioMin && peRatio < parseFloat(filters.peRatioMin)) return false;
      if (filters.peRatioMax && peRatio > parseFloat(filters.peRatioMax)) return false;
      if (filters.technicalScoreMin && techScore < parseFloat(filters.technicalScoreMin)) return false;
      if (filters.technicalScoreMax && techScore > parseFloat(filters.technicalScoreMax)) return false;
      if (filters.fundamentalScoreMin && fundScore < parseFloat(filters.fundamentalScoreMin)) return false;
      if (filters.fundamentalScoreMax && fundScore > parseFloat(filters.fundamentalScoreMax)) return false;
      if (filters.totalScoreMin && totalScore < parseFloat(filters.totalScoreMin)) return false;
      if (filters.totalScoreMax && totalScore > parseFloat(filters.totalScoreMax)) return false;
      if (filters.companyName && !item.companyName.toLowerCase().includes(filters.companyName.toLowerCase())) return false;

      return true;
    });
  }, [data, filters]);

  const sortedData = useMemo(() => {
    let sortableData = [...filteredData];
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
  }, [filteredData, sortConfig]);

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

  const handleFilterChange = (e) => {
    const { name, value } = e.target;
    setFilters(prev => ({ ...prev, [name]: value }));
  };

  const resetFilters = () => {
    setFilters({
      marketCapMin: "", marketCapMax: "",
      peRatioMin: "", peRatioMax: "",
      technicalScoreMin: "", technicalScoreMax: "",
      fundamentalScoreMin: "", fundamentalScoreMax: "",
      totalScoreMin: "", totalScoreMax: "",
      companyName: ""
    });
  };

  return (
    <div className="container-fluid mt-4">
      <div className="row mb-3">
        <div className="col-md-7">
          <h2>{data.length} Total Stocks <small className="text-body-secondary">({sortedData.length} Filtered Stocks)</small></h2>
        </div>
        <div className="col-md-5 text-end text-nowrap">
          <span className="align-middle">Data Last Refreshed at {generatedAt.toLocaleString().split('.')[0].replace('T', ' ')}</span>
        </div>
      </div>

      <table className="table table-striped table-hover table-bordered">
        <thead className="table-dark">
          <tr>
            <th colSpan="2">
              <div className="d-grid gap-2">
                <button className="btn btn-sm btn-secondary" onClick={resetFilters}>Reset Filters</button>
              </div>
            </th>
            <th className="text-center">
              <input type="text" className="form-control form-control-sm" name="companyName" value={filters.companyName} onChange={handleFilterChange} placeholder="Filter by name" />
            </th>
            <th className="text-center" >
              <div className="container gx-0">
                <div className="row gx-2">
                  <div className="col"><input type="number" className="form-control form-control-sm" name="marketCapMin" value={filters.marketCapMin} onChange={handleFilterChange} placeholder="Min" /></div>
                  <div className="col"><input type="number" className="form-control form-control-sm" name="marketCapMax" value={filters.marketCapMax} onChange={handleFilterChange} placeholder="Max" /></div>
                </div>
              </div>
            </th>
            <th className="text-center">
              <div className="container gx-0">
                <div className="row gx-2">
                  <div className="col"><input type="number" className="form-control form-control-sm" name="peRatioMin" value={filters.peRatioMin} onChange={handleFilterChange} placeholder="Min" /></div>
                  <div className="col"><input type="number" className="form-control form-control-sm" name="peRatioMax" value={filters.peRatioMax} onChange={handleFilterChange} placeholder="Max" /></div>
                </div>
              </div>
            </th>
            <th className="text-center">
              <div className="container gx-0">
                <div className="row gx-2">
                  <div className="col"><input type="number" className="form-control form-control-sm" name="technicalScoreMin" value={filters.technicalScoreMin} onChange={handleFilterChange} placeholder="Min" /></div>
                  <div className="col"><input type="number" className="form-control form-control-sm" name="technicalScoreMax" value={filters.technicalScoreMax} onChange={handleFilterChange} placeholder="Max" /></div>
                </div>
              </div>
            </th>
            <th className="text-center">
              <div className="container gx-0">
                <div className="row gx-2">
                  <div className="col"><input type="number" className="form-control form-control-sm" name="fundamentalScoreMin" value={filters.fundamentalScoreMin} onChange={handleFilterChange} placeholder="Min" /></div>
                  <div className="col"><input type="number" className="form-control form-control-sm" name="fundamentalScoreMax" value={filters.fundamentalScoreMax} onChange={handleFilterChange} placeholder="Max" /></div>
                </div>
              </div>
            </th>
            <th className="text-center">
              <div className="container gx-0">
                <div className="row gx-2">
                  <div className="col"><input type="number" className="form-control form-control-sm" name="totalScoreMin" value={filters.totalScoreMin} onChange={handleFilterChange} placeholder="Min" /></div>
                  <div className="col"><input type="number" className="form-control form-control-sm" name="totalScoreMax" value={filters.totalScoreMax} onChange={handleFilterChange} placeholder="Max" /></div>
                </div>
              </div>
            </th>
          </tr>
        </thead>
        <thead className="table-dark">
          <tr>
            <th className="text-center align-middle" onClick={() => requestSort("screenerCompanyId")}>Screener ID {getSortIcon("screenerCompanyId")}</th>
            <th className="text-center align-middle" onClick={() => requestSort("companyCode")}>Screener Link {getSortIcon("companyCode")}</th>
            <th className="text-center align-middle" onClick={() => requestSort("companyName")}>Name {getSortIcon("companyName")}</th>
            <th className="text-center align-middle" onClick={() => requestSort("marketCap")}>Market Cap (Cr.) {getSortIcon("marketCap")}</th>
            <th className="text-center align-middle" onClick={() => requestSort("peRatio")}>PE Ratio {getSortIcon("peRatio")}</th>
            <th className="text-center align-middle" onClick={() => requestSort("technicalScore")}>Technical Score {getSortIcon("technicalScore")}</th>
            <th className="text-center align-middle" onClick={() => requestSort("fundamentalScore")}>Fundamental Score {getSortIcon("fundamentalScore")}</th>
            <th className="text-center align-middle" onClick={() => requestSort("totalScore")}>Total Score {getSortIcon("totalScore")}</th>
          </tr>
        </thead>
        <tbody>
          {sortedData.map((item) => (
            <tr key={item.screenerCompanyId}>
              <td className="text-end">{item.screenerCompanyId}</td>
              <td><a href={item.screenerCompanyLink} target="_blank" rel="noreferrer">{item.companyCode}</a></td>
              <td>{item.companyName}</td>
              <td className="text-end">{item.attributes.marketCap.toLocaleString()}</td>
              <td className="text-end">{item.attributes.peRatio}</td>
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
