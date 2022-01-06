/* eslint-disable max-classes-per-file */
import React, { PureComponent } from 'react';
import { Treemap, ResponsiveContainer } from 'recharts';

import { useEffect, useState } from 'react';
import { useQuery } from 'react-query';
import { Spinner } from 'reactstrap';

import { Colors } from '../../lib/colors';
import { fetchListPollutionBrands } from '../../api/requests/brand';

function getColorFromRedToGreenByPercentage(value) {
    const hue = Math.round(value);
    return ["hsl(", hue, ", 50%, 50%)"].join("");
}
const CustomizedContent = ({ root, depth, x, y, width, height, index, colors, name, pollution }) => {

	return (
		<g>
			<rect
				x={x}
				y={y}
				width={width}
				height={height}
				style={{
					fill: getColorFromRedToGreenByPercentage(pollution),
					stroke: '#fff',
					strokeWidth: 2 / (depth + 1e-10),
					strokeOpacity: 1 / (depth + 1e-10),
				}}
			>
				<title>{pollution}</title>

			</rect>
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


export const PolutionTreeMap = () => {

	const { data: dataNumberModeles, isFetching: isLoading, isSuccess } =
		useQuery('pollutionBrand', () => fetchListPollutionBrands(), {
		});

	const [dataPlot, setDataPlot] = useState([]);

	useEffect(() => {
		if (isSuccess && !dataNumberModeles.data.error) {
			setDataPlot(Object.entries(dataNumberModeles.data).map((e) => ({ name: e[1].nomMarque, pollution: e[1].pollution })));
		}
		else {
			setDataPlot([]);
		}
	}, [dataNumberModeles]);
	return (
		<ResponsiveContainer width={"100%"} height={400}>
			<Treemap
				width={400}
				height={200}
				data={dataPlot}
				aspectRatio={4 / 3}
				dataKey="pollution"
				stroke="#fff"
				fill="#8884d8"
				content={<CustomizedContent />}
			/>
		</ResponsiveContainer>
	);
}
