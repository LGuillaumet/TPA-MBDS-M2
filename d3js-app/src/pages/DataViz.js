import { useState } from 'react';
import Select from "react-select";
import { Container, Row, Col, Card, Input, Fade } from 'reactstrap';
import { useQuery } from 'react-query';

import { NavBar } from '../components/NavBar';
import { PieChartDataBrand } from '../components/pieChart/PieChartDataBrand';
import { ListBoxPlot } from '../components/boxPlot/ListBoxPlot';
import { FilterBrandBarChart } from '../components/barChart/FilterBrandBarChart';
import { PolutionTreeMap } from '../components/treeMap/PolutionTreeMap';
import { TreeMapModelesBrand } from '../components/treeMap/TreeMapModelesBrand';

import { SimpleRadarChart } from '../components/radarChart/SimpleRadarChart';

import { fetchAllBrands } from '../api/requests/brand';

import './DataViz.scss';

export const DataViz = () => {
	const [brand, setBrand] = useState(null);
	const [brandCompare, setBrandComapre] = useState(null);


	const [compare, setCompare] = useState(false);

	const onchangeSelect = (item) => {
		setBrand(item.value);
	};

	const onchangeSelectCompare = (item) => {
		setBrandComapre(item.value);
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
						<Col className="mx-1 datavizCol" key="1">
							<h6>Marque : {brand}</h6>
							<Select
								value={brand}
								onChange={onchangeSelect}
								options={optionsBrands?.data.marques.map(v => ({
									label: v,
									value: v
								}))}
							/>
							<Row xs="2">
								<Col className="border p-0">
									<PieChartDataBrand brand={brand} />
								</Col>
								<Col className="border p-0">
									<TreeMapModelesBrand brand={brand} />
								</Col>
								<Col className="border p-0">
									<ListBoxPlot brand={brand} />

								</Col>
								<Col className="border p-0">
									<SimpleRadarChart />
								</Col>
							</Row>
						</Col>
						{compare && (
							<Col className="mx-1 datavizCol" key="2">
								<Fade in={compare}>
									<h6>Marque : {brandCompare}</h6>
									<Select
										value={brandCompare}
										onChange={onchangeSelectCompare}
										options={optionsBrands?.data.marques.map(v => ({
											label: v,
											value: v
										}))}
									/>
									<Row xs="2">
										<Col className="border p-0">
											<PieChartDataBrand key="2" brand={brandCompare} />
										</Col>
										<Col className="border p-0">
											<TreeMapModelesBrand brand={brandCompare} />
										</Col>
										<Col className="border p-0">
											<ListBoxPlot brand={brandCompare} />

										</Col>
										<Col className="border p-0">
											<SimpleRadarChart />
										</Col>
									</Row>
								</Fade>

							</Col>
						)}
						<h6>
							Bar Chart
						</h6>
						<FilterBrandBarChart setBrand={setBrand} />
						<PolutionTreeMap />

					</Row>
				</Col>
			</Row>
		</>
	);
}


