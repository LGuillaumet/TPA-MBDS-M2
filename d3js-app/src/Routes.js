import { lazy, Suspense } from 'react';

import {
	Routes,
	Route
} from "react-router-dom";
// import your route components too
import { Home } from './pages/Home';
import { DataViz } from './pages/DataViz';
import { Prediction } from './pages/Prediction';


const RoutesApp = () => (
	<Routes>
		<Route path="/" element={<Home />}>
			<Route index element={<Home />} />
		</Route>
		<Route path="dataviz" element={<DataViz />} />
		<Route path="prediction" element={<Prediction />} />
	</Routes>
);

export default RoutesApp;