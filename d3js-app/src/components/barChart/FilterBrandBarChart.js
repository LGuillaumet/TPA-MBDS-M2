import { useState } from 'react';
import { Container, Row, Col, Card, ButtonGroup, Button } from 'reactstrap';
import { BarChart, Bar, Cell, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';
import Select from "react-select";
import { useQuery } from 'react-query';

import { fetchAllColors, fetchAllDoors } from '../../api/requests/others';
import { fetchFilterAllBrands } from '../../api/requests/brand';

export const FilterBrandBarChart = ({ brand }) => {
	const [params, setParams] = useState(
		{
			doors: [],
			colors: [],
			state: '',
			source: '',
		});

	const onchangeSelect = (key, v) => {
		console.log('ICI', v);
		setParams((prev) => ({ ...prev, [key]: v }));
	};
	console.log('params', params);

	const { data: optionsCouleurs } = useQuery('colors', fetchAllColors);
	const { data: optionsPortes } = useQuery('doors', fetchAllDoors);
	const { data: dataFilter, refetch: getWithFilter } = useQuery('filter', () => fetchFilterAllBrands(params));
 
	console.log(dataFilter?.data);

	const handleSearch = () => {
		getWithFilter();
		console.log('filters')
	};
	console.log(dataFilter);
	const formatTick = (tickItem) => {
		console.log(tickItem);
		return tickItem.substring(0, 3);
	}
	return (
		<>
			<Row xs="4" className="align-items-center justify-content-center m-0">
				<Col>
					<span>Couleur</span>
					<Select
						onChange={(value) => onchangeSelect('colors', value.map(({ value }) => value))}
						isMulti
						options={optionsCouleurs?.data.couleurs.map(v => ({
							label: v,
							value: v
						}))}
					/>
				</Col>
				<Col>
					<span>Portes</span>
					<Select
						onChange={(value) => onchangeSelect('doors', value.map(({ value }) => value))}
						isMulti
						options={optionsPortes?.data.portes.map(p => ({
							label: p,
							value: p
						}))}
					/>
				</Col>
				<Col>
					<span>Etat</span>
					<>
						<ButtonGroup className="d-flex">
							<Button
								outline
								onClick={function noRefCheck() { }}
							>
								Neuve
							</Button>
							<Button
								outline
								onClick={function noRefCheck() { }}
							>
								Occasion
							</Button>
						</ButtonGroup>
					</>
				</Col>
				<Col>
					<span>Source(s)</span>
					<>
						<ButtonGroup className="d-flex">
							<Button
								outline
								onClick={function noRefCheck() { }}
							>
								Catalogue
							</Button>
							<Button
								outline
								onClick={function noRefCheck() { }}
							>
								Immatriculations
							</Button>
							<Button
								outline
								onClick={function noRefCheck() { }}
							>
								All
							</Button>
						</ButtonGroup>
					</>
				</Col>
			</Row>
			<div className="d-flex mt-1">
				<div className="ms-auto">
					<Button onClick={handleSearch}>
						Rechercher
					</Button>
				</div>
			</div>
			{dataFilter && !dataFilter?.data.error && (
			<ResponsiveContainer width={"100%"} height={500}>
				<BarChart
					width={500}
					height={300}
					data={!dataFilter?.data.error ? dataFilter?.data : null}
					margin={{
						top: 5,
						right: 30,
						left: 20,
						bottom: 5,
					}}
				>
					<CartesianGrid/>
					<XAxis interval={0} dataKey="marque" tickFormatter={(tick) => formatTick(tick)}/>
					<YAxis />
					<Tooltip />
					<Bar
						dataKey="proportion"
						strokeWidth={1}
						maxBarSize={20}
					>
						{
							!dataFilter?.data.error ? dataFilter?.data.map((entry, index) => (
								<Cell key={`cell-${index}`} fill={"#" + ((1 << 24) * Math.random() | 0).toString(16)} />
							))
							:	null
						}
					</Bar>
				</BarChart>
			</ResponsiveContainer>
			)}
		</>
	);
}
