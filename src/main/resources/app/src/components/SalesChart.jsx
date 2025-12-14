import React, { useEffect, useState } from "react";
import {Line, Bar,ComposedChart, Brush, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ReferenceLine, ResponsiveContainer } from 'recharts';

function SalesChart({ quarterlyResults }) {
  return (
    <div style={{ width: '100%', height: 400 }}>
      <ResponsiveContainer>
        <ComposedChart data={quarterlyResults}>
          <CartesianGrid stroke="#f5f5f5" />
          <XAxis dataKey="date" scale="band" tick={{fontSize:12}} />
          <YAxis yAxisId="left" tick={{fontSize:12}} domain={[(dataMin) => Math.floor(dataMin-10), (dataMax) => Math.ceil(dataMax+10)]}
            label={{ value: "Percentage", angle: -90, position: "insideLeft" }} />
          <YAxis yAxisId="right" orientation="right" tick={{fontSize:12}} label={{ value: "Sales (Crore)", angle: -90, position: "insideRight" }} />
          <Tooltip />
          <Legend />
          <Brush dataKey="date" />
          <ReferenceLine yAxisId="left" y={0} stroke="black" strokeDasharray="3 3"
            label={{ value: "0", position: "left", style: { fontSize: 12 } }} />
          <Line yAxisId="left" type="monotone" dataKey="grossProfitMargin" stroke="red" dot={false} />
          <Line yAxisId="left" type="monotone" dataKey="operatingProfitMargin" stroke="orange" dot={false} />
          <Line yAxisId="left" type="monotone" dataKey="netProfitMargin" stroke="green" dot={false} />
          <Bar yAxisId="right" dataKey="sales" fill="lightblue" />
        </ComposedChart>
      </ResponsiveContainer>
    </div>
  );
}

export default SalesChart;