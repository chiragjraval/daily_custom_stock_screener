import React, { useEffect, useState } from "react";
import ScoreProgressBar from "./ScoreProgressBar.jsx";

function CompanyDetails({ companyCode }) {
  const [details, setDetails] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetch(import.meta.env.BASE_URL + `/screener-data/prod/details/${companyCode}.json`)
      .then((res) => res.json())
      .then((json) => {
        setDetails(json);
        setLoading(false);
      })
      .catch((err) => {
        console.error(`Error loading details for ${companyCode}:`, err);
        setError(err.message);
        setLoading(false);
      });
  }, [companyCode]);

  const getPriceHigherThanDmaTotalScore = (companyScore) => {
    return companyScore.priceHigherThanDma50 + companyScore.priceHigherThanDma100 + companyScore.priceHigherThanDma200
      + companyScore.dma50HigherThanDma100 + companyScore.dma50HigherThanDma200 + companyScore.dma100HigherThanDma200;
  }

  const getPricePercentageAboveDmaTotalScore = (companyScore) => {
    return companyScore.pricePercentageAboveDma50 + companyScore.pricePercentageAboveDma100 + companyScore.pricePercentageAboveDma200;
  }

  if (loading) return <tr><td colSpan="8" className="text-center">Loading details...</td></tr>;
  if (error) return <tr><td colSpan="8" className="text-center text-danger">Error: {error}</td></tr>;
  if (!details) return <tr><td colSpan="8" className="text-center">No details available</td></tr>;

  return (
    <tr className="table-light">
      <td colSpan="9">
        <div className="container">
          <div className="row mb-3">
            <div className="col">
              <ScoreProgressBar label="Total Score" labelLevel="h4"
                score={details.companyScore.totalScore} maxScore="100"
                displayWidth="100%" />
            </div>
          </div>
          <div className="row mb-3">
            <div className="col">
              <ScoreProgressBar label="Fundamental Score" labelLevel="h5"
                score={details.companyScore.totalFundamentalScore} maxScore="50"
                displayWidth="100%" />
            </div>
            <div className="col">
              <ScoreProgressBar label="Technical Score" labelLevel="h5"
                score={details.companyScore.totalTechnicalScore} maxScore="50"
                displayWidth="100%" />
            </div>
          </div>
          <div className="row mb-3">
            <div className="col">
              <ScoreProgressBar label="Sales Growth Score" labelLevel="h6"
                score={details.companyScore.salesGrowthPercentile} maxScore="20"
                displayWidth="40%" />
            </div>
            <div className="col">
              <ScoreProgressBar label="Price Above DMA Score" labelLevel="h6"
                score={getPriceHigherThanDmaTotalScore(details.companyScore)} maxScore="30"
                displayWidth="60%" />
            </div>
          </div>
          <div className="row mb-3">
            <div className="col">
              <ScoreProgressBar label="OPM Score" labelLevel="h6"
                score={details.companyScore.operatingProfitMarginPercentile} maxScore="20"
                displayWidth="40%" />
            </div>
            <div className="col">
              <ScoreProgressBar label="Price Percentage Above DMA Score" labelLevel="h6"
                score={getPricePercentageAboveDmaTotalScore(details.companyScore)} maxScore="15"
                displayWidth="30%" />
            </div>
          </div>
          <div className="row mb-3">
            <div className="col">
              <ScoreProgressBar label="NPM Score" labelLevel="h6"
                score={details.companyScore.operatingProfitMarginPercentile} maxScore="10"
                displayWidth="20%" />
            </div>
            <div className="col">
              <ScoreProgressBar label="Volume Score" labelLevel="h6"
                score={details.companyScore.weeklyVolumeHigherThanYearlyVolume} maxScore="5"
                displayWidth="10%" />
            </div>
          </div>
        </div>
        <div className="p-3">
          <h6>{companyCode} - Detailed Information</h6>
          <pre>{JSON.stringify(details.companyScore, null, 2)}</pre>
        </div>
      </td>
    </tr>
  );
}

export default CompanyDetails;