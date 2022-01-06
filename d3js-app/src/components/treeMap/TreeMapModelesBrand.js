import { Treemap, ResponsiveContainer, Tooltip } from 'recharts';
import { useEffect, useState } from 'react';
import { useQuery } from 'react-query';
import { Spinner } from 'reactstrap';

import { Colors } from '../../lib/colors';
import { fetchNumberModelesBrand } from '../../api/requests/brand';


const CustomizedContent = ({ root, depth, x, y, width, height, index, colors, name, polution, children }) => {
	return (
		<g style={{ pointerEvents: "all" }}>
			<rect
				x={x}
				y={y}
				width={width}
				height={height}
				style={{
					fill: depth < 2 ? Colors[Math.floor((index / root.children?.length) * 6)] : 'none',
					strokeWidth: 2 / (depth + 1e-10),
					strokeOpacity: 1 / (depth + 1e-10),
				}}
			>
				{depth === 2 && (
					<title>{root.name + '-' + name}</title>

				)}
			</rect>
			{depth === 1 ? (
				<text x={x + 4} y={y + 18} stroke="none" fill="#000" fontSize={16} fillOpacity={0.9}>
					{name}
				</text>
			) : null}
			{depth === 2 ? (
				width > 50 ?
					<text x={x + width / 2} y={y + height / 2 + 7} textAnchor="middle" stroke="none" fill="#000000" fontSize={10}>
						{name}
					</text>
					: null
			) : null}

		</g>
	);
}

export const TreeMapModelesBrand = ({ brand }) => {


	const { data: dataNumberModeles, refetch, isFetching: isLoading, isSuccess } =
		useQuery(`numberModeles${brand}`, () => fetchNumberModelesBrand(brand), {
			enabled: !!brand,
		});

	const [dataPlot, setDataPlot] = useState([]);

	useEffect(() => {
		refetch();
	}, [brand]);

	useEffect(() => {
		if (isSuccess && !dataNumberModeles.data.error) {
			setDataPlot(Object.entries(dataNumberModeles.data).map((e) => ({ name: e[0], children: e[1] })));
		}
		else {
			setDataPlot([]);
		}
	}, [dataNumberModeles]);

	console.log(dataPlot);
	return (
		<>
			{isLoading ?
				<div className="d-flex align-items-center justify-content-center" style={{ height: 400 }}>
					<Spinner />
				</div>
				:
				<ResponsiveContainer width={"100%"} height={400}>
					<Treemap
						width={400}
						height={200}
						data={dataPlot}
						dataKey="value"
						aspectRatio={3 / 4}
						stroke="#fff"
						fill="#8884d8"
						content={<CustomizedContent />}
					/>
				</ResponsiveContainer>
			}
		</>
	);
}
