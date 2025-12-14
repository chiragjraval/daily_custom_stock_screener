import React, { useEffect, useState } from "react";
import {LineChart, Line,BarChart, Bar,ComposedChart, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';

function PriceChart({ technicalHistory }) {
  return (
    <div style={{ width: '100%', height: 400 }}>
      <ResponsiveContainer>
        <ComposedChart data={technicalHistory}>
          <CartesianGrid stroke="#f5f5f5" />
          <XAxis dataKey="date" scale="band" tick={{fontSize:12}} />
          <YAxis yAxisId="left" orientation="right" domain={[(dataMin) => Math.floor(dataMin*0.97), (dataMax) => Math.ceil(dataMax*1.03)]}
              tick={{fontSize:12}} label={{ value: "Price", angle: -90, position: "insideRight" }} />
          <YAxis yAxisId="right"  tick={{fontSize:12}} label={{ value: "Volume", angle: -90, position: "insideLeft" }} />
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