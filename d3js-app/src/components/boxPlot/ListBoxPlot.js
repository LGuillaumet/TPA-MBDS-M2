import { PieChart, Pie, Legend, Tooltip, ResponsiveContainer, Cell } from 'recharts';
import { useQuery } from 'react-query';
import { useState, useEffect } from 'react';

import { BoxPlot } from './SimpleBoxPlot';


import { fetchUserQByBrand } from '../../api/requests/user';

export const ListBoxPlot = ({ brand }) => {

	const { data: dataRatioType, refetch, isFetching: isLoading, isSuccess } = useQuery(`userQBrand${brand}`, () => fetchUserQByBrand(brand), {
		enabled: !!brand,
	});

	const [dataPlot, setDataPlot] = useState([]);

	useEffect(() => {
		refetch();
	}, [brand]);

	useEffect(() => {
		if (isSuccess && !dataRatioType.data.error) {
			setDataPlot(dataRatioType.data[0]);
		}
		else {
			setDataPlot([]);
		}
	}, [dataRatioType]);

	const labelKey = {
		q0age: 'min',
		q1age: 'q1',
		q2age: 'median',
		q3age: 'q3',
		q4age: 'max',
		q0taux: 'min',
		q1taux: 'q1',
		q2taux: 'median',
		q3taux: 'q3',
		q4taux: 'max',
		q0nbchildren: 'min',
		q1nbchildren: 'q1',
		q2nbchildren: 'median',
		q3nbchildren: 'q3',
		q4nbchildren: 'max',
	};
	const filteredKeysAge = ['q0age', 'q1age', 'q2age', 'q3age', 'q4age'];
	const filteredKeysTaux = ['q0taux', 'q1taux', 'q2taux', 'q3taux', 'q4taux'];
	const filteredKeysNbChildren = ['q0nbchildren', 'q1nbchildren', 'q2nbchildren', 'q3nbchildren', 'q4nbchildren'];


	const filteredAge = filteredKeysAge.reduce((obj, key) => ({ ...obj, [labelKey[key]]: dataPlot[key] }), {});
	const filteredTaux = filteredKeysTaux.reduce((obj, key) => ({ ...obj, [labelKey[key]]: dataPlot[key] }), {});
	const filteredNbChildren = filteredKeysNbChildren.reduce((obj, key) => ({ ...obj, [labelKey[key]]: dataPlot[key] }), {});

	console.log(filteredNbChildren);
	return (
		<>
			<BoxPlot title={'age'} data={filteredAge} />
			<BoxPlot title={'taux'} data={filteredTaux} />
			<BoxPlot title={'nbEnfants'} data={filteredNbChildren} />
		</>
	);


}