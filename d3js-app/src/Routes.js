import { lazy, Suspense } from 'react';

import {
	Routes,
	Route
} from "react-router-dom";
// import your route components too
import { Home } from './pages/Home';
const RoutesApp = () => (
	<Routes>
		<Route path="/" element={<Home />}>
			<Route index element={<Home />} />
		</Route>
	</Routes>
);

export default RoutesApp;