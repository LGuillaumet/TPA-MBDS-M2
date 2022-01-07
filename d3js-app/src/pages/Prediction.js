import { useState, useEffect } from 'react';
import { Container, Row, Col, Card, Table } from 'reactstrap';
import { BarChart, Bar, Cell, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';
import Select from "react-select";
import { useQuery } from 'react-query';

import { NavBar } from '../components/NavBar';

import { fetchPredictionTypeCar, fetchPredictionCar, fetchPredictionByIdMarketing } from '../api/requests/predictions';


export const Prediction = () => {
	const [customers, setPredictCustomer] = useState([]);
	const [cars, setPredictCars] = useState([]);


	const [idmarketing, setIdmarketing] = useState(null);



	const { data: dataPrediction, isFetching: isLoading, isSuccess } =
		useQuery('predictionTypeCAr', () => fetchPredictionTypeCar(), {
		});


	const { data: dataPredictionCarByTypeCategory, isFetching: isLoadingCar, isSuccessCar } =
		useQuery('predictionCar', () => fetchPredictionCar(), {
		});

	const { data: dataPredictionTypeByMarketingId, refetch } =
		useQuery('predictionTypeByMarketingId', () => fetchPredictionByIdMarketing(idmarketing), {
			enabled: idmarketing !== null,
		});

	useEffect(() => {
		console.log(idmarketing);
		refetch();
	}, [idmarketing])

	const [dataPlot, setDataPlot] = useState([]);
	const [dataPlotCars, setDataPlotCars] = useState([]);
	const [dataPlotPrediction, setDataPlotPrediction] = useState([]);


	useEffect(() => {
		if (isSuccess && !dataPrediction.data.error) {
			setDataPlot(Object.entries(dataPrediction.data).map((e) => ({ name: e[0], value: e[1] })));
		}
		else {
			setDataPlot([]);
		}
	}, [dataPrediction]);

	useEffect(() => {
		if (isSuccess && !dataPredictionCarByTypeCategory.data.error) {
			setDataPlotCars(Object.entries(dataPredictionCarByTypeCategory.data).map((e) => ({ name: e[0], value: e[1] })));
		}
		else {
			setDataPlotCars([]);
		}
	}, [dataPredictionCarByTypeCategory]);

	useEffect(() => {
		if (isSuccess && !dataPredictionTypeByMarketingId.data.error) {
			setDataPlotPrediction(Object.entries(dataPredictionTypeByMarketingId.data).map((e) => ({ name: e[0], value: e[1][0].prediction })));
		}
		else {
			setDataPlotPrediction([]);
		}
	}, [dataPredictionTypeByMarketingId]);

	console.log(dataPlotPrediction)

	return (
		<>
			<NavBar />
			<Row xs="12" className="justify-content-center mx-5">
				<Col xs="12">
					<Container>
						<Row xs="12" className="justify-content-center mx-5">
							<Col xs="12">
								<Card className="home-card">
									<Select
										onChange={(item) => setPredictCustomer(item.value)}
										options={dataPlot.map(v => ({
											label: v.name,
											value: v.value
										}))}
									/>
									<Table hover bordered className="m-0">
										<thead>
											<tr>
												<th>#</th>
												<th>age</th>
												<th>situation</th>
												<th>nombre d'enfants</th>
												<th>taux endettement</th>
												<th>a une seconde voiture</th>
											</tr>
										</thead>
										<tbody>
											{customers.map((customer, index) => {
												return (
													<tr key={index} onClick={() => setIdmarketing(customer.idmarketing)} style={{cursor: 'pointer'}}>
														<th onClick={() => setIdmarketing(customer.idmarketing)} scope="row">{index}</th>
														<td onClick={() => setIdmarketing(customer.idmarketing)}>{customer.age}</td>
														<td onClick={() => setIdmarketing(customer.idmarketing)}>{customer.situation}</td>
														<td onClick={() => setIdmarketing(customer.idmarketing)}>{customer.nbchildren}</td>
														<td onClick={() => setIdmarketing(customer.idmarketing)}>{customer.taux}</td>
														<td onClick={() => setIdmarketing(customer.idmarketing)}>{customer.havesecondcar ? 'oui' : 'non'}</td>
													</tr>
												);
											})}
										</tbody>
									</Table>

								</Card>
							</Col>
							<Col>
								{idmarketing && (
									<ResponsiveContainer width={"100%"} height={400}>
										<BarChart
											width={500}
											height={300}
											data={dataPlotPrediction}
											margin={{
												top: 5,
												right: 30,
												left: 20,
												bottom: 5,
											}}
											barSize={20}
										>
											<XAxis dataKey="name" scale="point" padding={{ left: 10, right: 10 }} />
											<YAxis />
											<Tooltip />
											<CartesianGrid strokeDasharray="3 3" />
											<Bar dataKey="value" fill="#8884d8" background={{ fill: '#eee' }} />
										</BarChart>
									</ResponsiveContainer>
								)}
							</Col>
						</Row>
						<Row xs="12" className="justify-content-center mx-5 mt-5">
							<Col xs="12">
								<Card className="home-card">
									<Select
										onChange={(item) => setPredictCars(item.value)}
										options={dataPlotCars.map(v => ({
											label: v.name,
											value: v.value
										}))}
									/>
									<Table bordered className="m-0">
										<thead>
											<tr>
												<th>#</th>
												<th>marque</th>
												<th>nom</th>
												<th>puissance</th>
												<th>nbplaces</th>
												<th>nbportes</th>
												<th>couleur</th>
												<th>prix</th>
												<th>occasion</th>
											</tr>
										</thead>
										<tbody>
											{cars.map((car, index) => {
												return (
													<tr key={index}>
														<th scope="row">{index}</th>
														<td>{car.marque}</td>
														<td>{car.nom}</td>
														<td>{car.puissance}</td>
														<td>{car.nbplaces}</td>
														<td>{car.nbportes}</td>
														<td>{car.couleur}</td>
														<td>{car.prix}</td>
														<td>{car.havesecondcar ? 'oui' : 'non'}</td>
													</tr>
												);
											})}
										</tbody>
									</Table>

								</Card>
							</Col>
						</Row>
					</Container>
				</Col>
			</Row>
		</>
	);
}
