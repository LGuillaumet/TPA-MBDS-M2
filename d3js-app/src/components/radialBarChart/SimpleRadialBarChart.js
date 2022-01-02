import { RadialBarChart, RadialBar, Legend, ResponsiveContainer } from 'recharts';

const data = [
	{
		name: '2 em voiture',
		uv: "OUI",
		pv: 2400,
		fill: '#8884d8',
	},
	{
		name: 'sexe M-F%',
		uv: "F 50%",
		pv: 4567,
		fill: '#83a6ed',
	},
	{
		name: 'taux dette',
		uv: 15.69,
		pv: 1398,
		fill: '#8dd1e1',
	},
	{
		name: 'situation familliale %',
		uv: 8.22,
		pv: 9800,
		fill: '#82ca9d',
	},
	{
		name: 'nombre enfants a charge   0-...',
		uv: 8.63,
		pv: 3908,
		fill: '#a4de6c',
	},
	{
		name: '2 em voiture',
		uv: 2.63,
		pv: 4800,
		fill: '#d0ed57',
	},
];

const style = {
	top: '50%',
	right: 0,
	lineHeight: '24px',
};

export const SimpleRadialBand = () => {
	return (
		<ResponsiveContainer width={"100%"} height={400}>
			<RadialBarChart
				width={730}
				height={250}
				innerRadius="10%"
				outerRadius="80%"
				data={data}
				startAngle={180}
				endAngle={0}
			>
				<RadialBar
					minAngle={15}
					label={{ position: 'insideStart', fill: '#fff' }}
					background
					clockWise
					dataKey="uv"
				/>
				<Legend iconSize={10} layout="vertical" verticalAlign="middle" wrapperStyle={style} />
			</RadialBarChart>
		</ResponsiveContainer>
	);
}
