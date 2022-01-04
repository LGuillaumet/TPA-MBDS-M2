import { useState } from 'react';
import Select from "react-select";
import { Container, Row, Col, Card, Input, Fade } from 'reactstrap';
import { useQuery } from 'react-query';

import { NavBar } from '../components/NavBar';
import { PieChartDataBrand } from '../components/pieChart/PieChartDataBrand';
import { SimpleRadialBand } from '../components/radialBarChart/SimpleRadialBarChart';
import { FilterBrandBarChart } from '../components/barChart/FilterBrandBarChart';
import { TreeMap } from '../components/treeMap/CustomTreeMap';
import { SimpleRadarChart } from '../components/radarChart/SimpleRadarChart';

import { fetchAllBrands } from '../api/requests/brand';

import './DataViz.scss';

export const DataViz = () => {
	const [brand, setBrand] = useState(null);
	const [compare, setCompare] = useState(false);

	const onchangeSelect = (item) => {
		setBrand(item.value);
	};

	const { data: optionsBrands } = useQuery('brands', fetchAllBrands);

	return (
		<>
			<NavBar />
			<Row xs="12" className="justify-content-center mx-5">
				<Col xs="12">
					<h3>
						Data visualization
					</h3>
					<div className="d-flex align-items-center justify-content-center">
						<span>Comparer avec une autre marque</span>
						<Input
							className="mx-1"
							type="switch"
							id="compare"
							checked={compare}
							onChange={() => setCompare(!compare)}
							name="compare"
						/>
					</div>
					<Row xs="12" className="justify-content-center">
						<Col className="mx-1 datavizCol">
							<h6>Marque : {brand}</h6>
							<Select
								onChange={onchangeSelect}
								options={optionsBrands?.data.marques.map(v => ({
									label: v,
									value: v
								}))}
							/>
							<Row xs="2">
								<Col className="border">
									<PieChartDataBrand brand={brand} />
								</Col>
								<Col className="border h-50">
									<TreeMap />
								</Col>
								<Col className="border">
									<SimpleRadialBand brand={brand} />
								</Col>
								<Col className="border">
									<SimpleRadarChart />
								</Col>
							</Row>
						</Col>
						{compare && (
							<Col className="mx-1">
								<Fade in={compare}>
									<h6>Marque : {brand}</h6>
									<Select
										onChange={onchangeSelect}
										options={optionsBrands}
									/>
									<Row xs="2">
										<Col className="border">
											<PieChartDataBrand />
										</Col>
										<Col className="border h-50">
											<TreeMap />
										</Col>
										<Col className="border">
											<SimpleRadialBand />
										</Col>
										<Col className="border">
											<SimpleRadarChart />
										</Col>
									</Row>
								</Fade>

							</Col>
						)}
						<h6>
							Bar Chart
						</h6>
						<FilterBrandBarChart />
					</Row>
				</Col>
			</Row>
		</>
	);
}


