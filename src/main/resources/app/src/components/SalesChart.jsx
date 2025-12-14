import React, { useEffect, useState } from "react";
import {LineChart, Line,BarChart, Bar,ComposedChart, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';

function SalesChart({ quarterlyResults }) {
  return (
    <div style={{ width: '100%', height: 400 }}>
      <ResponsiveContainer>
        <ComposedChart data={quarterlyResults}>
          <CartesianGrid stroke="#f5f5f5" />
          <XAxis dataKey="date" scale="band" />
          <YAxis yAxisId="left" label={{ value: "Percentage", angle: -90, position: "insideLeft" }} />
          <YAxis yAxisId="right" orientation="right" label={{ value: "Sales (Crore)", angle: -90, position: "insideRight" }} />
          <Tooltip />
          <Legend />
          <Line yAxisId="left" type="monotone" dataKey="grossProfitMargin" stroke="red" dot={false} />
          <Line yAxisId="left" type="monotone" dataKey="operatingProfitMargin" stroke="orange" dot={false} />
          <Line yAxisId="left" type="monotone" dataKey="netProfitMargin" stroke="green" dot={false} />
          <Line yAxisId="left" type="monotone" dataKey="salesGrowthYoY" stroke="blue" dot={false} />
          <Bar yAxisId="right" dataKey="sales" fill="gray" />
        </ComposedChart>
      </ResponsiveContainer>
    </div>
  );
}

export default SalesChart;