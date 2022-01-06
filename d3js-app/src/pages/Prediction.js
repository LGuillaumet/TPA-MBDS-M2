import { useState, useEffect } from 'react';
import { Container, Row, Col, Card, Table } from 'reactstrap';
import Select from "react-select";
import { useQuery } from 'react-query';

import { NavBar } from '../components/NavBar';

import { fetchPredictionTypeCar, fetchPredictionCar } from '../api/requests/predictions';


export const Prediction = () => {
	const [customers, setPredictCustomer] = useState([]);
	const [cars, setPredictCars] = useState([]);


	const { data: dataPrediction, isFetching: isLoading, isSuccess } =
		useQuery('predictionTypeCAr', () => fetchPredictionTypeCar(), {
		});


	const { data: dataPredictionCarByTypeCategory, isFetching: isLoadingCar, isSuccessCar } =
		useQuery('predictionCar', () => fetchPredictionCar(), {
		});

	const [dataPlot, setDataPlot] = useState([]);
	const [dataPlotCars, setDataPlotCars] = useState([]);


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
									<Table bordered className="m-0">
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
													<tr key={index}>
														<th scope="row">{index}</th>
														<td>{customer.age}</td>
														<td>{customer.situation}</td>
														<td>{customer.nbchildren}</td>
														<td>{customer.taux}</td>
														<td>{customer.havesecondcar ? 'oui' : 'non'}</td>
													</tr>
												);
											})}
										</tbody>
									</Table>

								</Card>
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
