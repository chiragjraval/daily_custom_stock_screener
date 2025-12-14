import React, { useEffect, useState } from "react";
import {LineChart, Line,BarChart, Bar,ComposedChart, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';

function PriceChart({ technicalHistory }) {
  return (
    <div style={{ width: '100%', height: 400 }}>
      <ResponsiveContainer>
        <ComposedChart data={technicalHistory}>
          <CartesianGrid stroke="#f5f5f5" />
          <XAxis dataKey="date" scale="band" />
          <YAxis yAxisId="left" label={{ value: "Price", angle: -90, position: "insideLeft" }} />
          <YAxis yAxisId="right" orientation="right" label={{ value: "Volume", angle: -90, position: "insideRight" }} />
          <Tooltip />
          <Legend />
          <Line yAxisId="left" type="monotone" dataKey="price" stroke="blue" dot={false} />
          <Line yAxisId="left" type="monotone" dataKey="dma50" stroke="green" dot={false} />
          <Line yAxisId="left" type="monotone" dataKey="dma100" stroke="orange" dot={false} />
          <Line yAxisId="left" type="monotone" dataKey="dma200" stroke="red" dot={false} />
          <Bar yAxisId="right" dataKey="volume" fill="gray" />
        </ComposedChart>
      </ResponsiveContainer>
    </div>
  );
}

export default PriceChart;