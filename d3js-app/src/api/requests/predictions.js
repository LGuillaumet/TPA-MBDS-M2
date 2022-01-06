import request from '../request';

export const fetchPredictionTypeCar = () => request('/listPredictionTypeCar');

export const fetchPredictionCar = () => request('/listPredictionCar');

export const fetchPredictionByIdMarketing = (idmarketing) => request(`/getPredictionByIdMarketing/${idmarketing}`);

