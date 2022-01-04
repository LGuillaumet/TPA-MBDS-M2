import { PieChart, Pie, Legend, Tooltip, ResponsiveContainer, Cell } from 'recharts';
import { useQuery } from 'react-query';
import { useState, useEffect } from 'react';
import { Colors } from '../../lib/colors';

import { fetchRatioBrand } from '../../api/requests/brand';

export const PieChartDataBrand = ({ brand }) => {
	const data02 = [
		{ name: "Group A", value: 2400 },
		{ name: "Group B", value: 4567 },
		{ name: "Group C", value: 1398 },
		{ name: "Group D", value: 9800 },
		{ name: "Group E", value: 3908 },
		{ name: "Group F", value: 4800 }
	];
	const { data: dataRatioType, refetch, isFetching: isLoading, isSuccess } = useQuery('ratioType', () => fetchRatioBrand(brand), {
		enabled: !!brand,
	});

	const [dataPlot, setDataPlot] = useState([]);

	useEffect(() => {
		refetch();
	}, [brand]);

	useEffect(() => {
		if (isSuccess && !dataRatioType.data.error) {
			setDataPlot(Object.entries(dataRatioType.data).map((e) => ({ name: e[0], value: e[1] })));
		}
		else {
			setDataPlot([]);
		}
	}, [dataRatioType]);

	return (
		<>
			<ResponsiveContainer width={"100%"} height={400}>
				<PieChart width={400} height={400}>
					<Pie
						isAnimationActive={false}
						dataKey="value"
						data={dataPlot}
						// cx={200}
						// cy={200}
						innerRadius={40}
						outerRadius={80}
						fill="#8884d8"
					>
						{dataPlot.map((entry, index) => (
							<Cell key={`cell-${index}`} fill={Colors[index % Colors.length]} />
						))}
					</Pie>

					<Tooltip />
					<Legend />
				</PieChart>
			</ResponsiveContainer>
		</>
	);
}
