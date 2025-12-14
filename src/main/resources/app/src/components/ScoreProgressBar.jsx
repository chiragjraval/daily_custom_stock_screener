import React from "react";

function ScoreProgressBar({ label, labelLevel, score, maxScore, displayWidth }) {
  const LabelTag = labelLevel;
  const normalizedScore = (score / maxScore) * 100;

  // Pick Bootstrap color class based on input
  const colorClass =
    normalizedScore < 70 ? "bg-danger" :
    normalizedScore < 90 ? "bg-warning" :
    "bg-success"; // default green

  return (
    <div>
      <LabelTag>{label} ({maxScore})</LabelTag>
      <div className="progress" role="progressbar" style={{width:displayWidth}}>
        <div className={`progress-bar ${colorClass}`} style={{width:normalizedScore+'%'}}>{score}</div>
      </div>
    </div>
  );
}

export default ScoreProgressBar;
