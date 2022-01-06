import { useState, useEffect } from 'react';
import { Container, Row, Col, Card, Table } from 'reactstrap';
import Select from "react-select";
import { useQuery } from 'react-query';

import { NavBar } from '../components/NavBar';

import { fetchPredictionTypeCar } from '../api/requests/predictions';


export const Prediction = () => {
	const [customers, setPredictCustomer] = useState([]);

	const onchangeSelect = (item) => {
		console.log(item);
		setPredictCustomer(item.value);
	};


	const { data: dataPrediction, isFetching: isLoading, isSuccess } =
		useQuery('predictionTypeCAr', () => fetchPredictionTypeCar(), {
		});

	const [dataPlot, setDataPlot] = useState([]);

	useEffect(() => {
		if (isSuccess && !dataPrediction.data.error) {
			setDataPlot(Object.entries(dataPrediction.data).map((e) => ({ name: e[0], value: e[1] })));
		}
		else {
			setDataPlot([]);
		}
	}, [dataPrediction]);

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
										onChange={onchangeSelect}
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
														<td>{customer.havesecondcar ? 'oui': 'non'}</td>
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
