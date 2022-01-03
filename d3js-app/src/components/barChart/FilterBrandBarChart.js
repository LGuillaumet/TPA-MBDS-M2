import { useState } from 'react';
import { Container, Row, Col, Card, ButtonGroup, Button, Spinner, Alert } from 'reactstrap';
import { BarChart, Bar, Cell, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';
import Select from "react-select";
import { useQuery } from 'react-query';

import { fetchAllColors, fetchAllDoors } from '../../api/requests/others';
import { fetchFilterAllBrands } from '../../api/requests/brand';

import { Colors } from '../../lib/colors';

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

	const { data: optionsCouleurs } = useQuery('colors', fetchAllColors);
	const { data: optionsPortes } = useQuery('doors', fetchAllDoors);
	const { data: dataFilter, refetch: getWithFilter, isFetching: isLoadingFilter } = useQuery('filter', () => fetchFilterAllBrands(params));

	const handleSearch = () => {
		getWithFilter();
	};
	const formatTick = (tickItem) => {
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
			<div className="d-flex mt-1 align-content-center justify-space-evenly">

				<div className="ms-auto d-flex align-content-center justify-space-evenly">
					{!isLoadingFilter && dataFilter?.data.error &&
						<Alert
							className="p-1 m-0 mx-3 d-flex align-items-center justify-content-center"
							color="primary"
							fade
						>
							{dataFilter?.data.error}
						</Alert>
					}
					{isLoadingFilter && <Spinner className="mx-3" color="primary" />}
					<Button onClick={handleSearch}>
						Rechercher
					</Button>
				</div>
			</div>
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
					<CartesianGrid />
					<YAxis />
					<Tooltip />

					{dataFilter && !dataFilter?.data.error && (
						<>
							<XAxis interval={0} dataKey="marque" tickFormatter={(tick) => formatTick(tick)} />
							<Bar
								dataKey="proportion"
								strokeWidth={1}
								maxBarSize={20}
							>
								{
									!dataFilter?.data.error ? dataFilter?.data.map((entry, index) => (
										<Cell key={`cell-${index}`} fill={Colors[index % Colors.length]} />
									))
										: null
								}
							</Bar>
						</>
					)}
				</BarChart>
			</ResponsiveContainer>
		</>
	);
}
