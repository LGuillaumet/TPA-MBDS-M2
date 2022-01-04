/* eslint-disable max-classes-per-file */
import React, { PureComponent } from 'react';
import { Treemap, ResponsiveContainer } from 'recharts';

const data = [
	{
		name: 'axis',
		size: 5219,
		polution: 1
	},
	{
		name: 'controls',
		size: 5219,
		polution: 2
	},
	{
		name: 'data',
		size: 5219,
		polution: 3
	},
	{
		name: 'events',
		size: 5219,
		polution: 4
	},
	{
		name: 'legend',
		size: 5219,
		polution: 1
	},
	{
		name: 'operator',
		size: 5219,
		polution: 1
	},
];

const COLORS = ['#3cff1a', '#a3ff1a', '#e4ff1a', '#ffe41a', '#ff901a', '#ff3c1a'];

const CustomizedContent = ({ root, depth, x, y, width, height, index, colors, name, polution }) => {
	return (
		<g>
			<rect
				x={x}
				y={y}
				width={width}
				height={height}
				style={{
					fill: COLORS[polution],
					stroke: '#fff',
					strokeWidth: 2 / (depth + 1e-10),
					strokeOpacity: 1 / (depth + 1e-10),
				}}
			/>
			{depth === 1 ? (
				<text x={x + width / 2} y={y + height / 2 + 7} textAnchor="middle" fill="#fff" fontSize={14}>
					{name}
				</text>
			) : null}
			{depth === 1 ? (
				<text x={x + 4} y={y + 18} fill="#fff" fontSize={16} fillOpacity={0.9}>
					{index + 1}
				</text>
			) : null}
		</g>
	);
}


export const TreeMap = () => {
	return (
		<ResponsiveContainer width={"100%"} height={400}>
			<Treemap
				width={400}
				height={200}
				data={data}
				dataKey="size"
				stroke="#fff"
				fill="#8884d8"
				content={<CustomizedContent colors={COLORS} />}
			/>
		</ResponsiveContainer>
	);
}
