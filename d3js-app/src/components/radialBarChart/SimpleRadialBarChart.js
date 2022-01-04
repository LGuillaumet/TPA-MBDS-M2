import { RadialBarChart, RadialBar, Legend, ResponsiveContainer, Cell } from 'recharts';
import { useEffect, useState } from 'react';
import { useQuery } from 'react-query';
import { Spinner } from 'reactstrap';

import { Colors } from '../../lib/colors';
import { fetchUserProfilByBrand } from '../../api/requests/user';

const style = {
	top: '50%',
	right: 0,
	lineHeight: '24px',
};

export const SimpleRadialBand = ({ brand }) => {

	const { data: dataUserProfile, refetch, isFetching: isLoading, isSuccess } = useQuery('lambdaRadialBar', () => fetchUserProfilByBrand(brand), {
		enabled: !!brand,
	});

	const [dataPlot, setDataPlot] = useState([]);

	useEffect(() => {
		refetch();
	}, [brand]);

	useEffect(() => {
		if (isSuccess && !dataUserProfile.data.error) {
			setDataPlot(Object.entries(dataUserProfile.data).map((e) => ({ name: e[0], value: e[1] })));
		}
		else {
			setDataPlot([]);
		}
	}, [dataUserProfile]);
	console.log(dataPlot);
	return (
		<ResponsiveContainer width={"100%"} height={400}>
			{isLoading ?
				<Spinner />
				:
				<RadialBarChart
					width={730}
					height={250}
					innerRadius="10%"
					outerRadius="80%"
					data={dataPlot}
					startAngle={180}
					endAngle={0}
				>
					<RadialBar
						minAngle={15}
						label={{ position: 'insideStart', fill: '#fff' }}
						background
						clockWise
						dataKey="value"
					>
						{dataPlot.map((entry, index) => (
							<Cell key={`cell-${index}`} fill={Colors[index % Colors.length]} />
						))}
					</RadialBar>

					<Legend iconSize={10} layout="vertical" verticalAlign="middle" wrapperStyle={style} />
				</RadialBarChart>
			}
		</ResponsiveContainer>
	);
}
