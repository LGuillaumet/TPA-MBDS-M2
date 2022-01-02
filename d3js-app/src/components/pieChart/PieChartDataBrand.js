import { PieChart, Pie, Legend, Tooltip, ResponsiveContainer, Cell } from 'recharts';
import { perc2color } from '../../lib/color-range';
import { Colors } from '../../lib/colors';

export const PieChartDataBrand = ({ brand }) => {
	const data02 = [
		{ name: "Group A", value: 2400 },
		{ name: "Group B", value: 4567 },
		{ name: "Group C", value: 1398 },
		{ name: "Group D", value: 9800 },
		{ name: "Group E", value: 3908 },
		{ name: "Group F", value: 4800 }
	];

	return (
		<>
			<ResponsiveContainer width={"100%"} height={400}>
				<PieChart width={400} height={400}>
					<Pie
						isAnimationActive={false}
						dataKey="value"
						data={data02}
						// cx={200}
						// cy={200}
						innerRadius={40}
						outerRadius={80}
						fill="#8884d8"
					>
						{data02.map((entry, index) => (
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
