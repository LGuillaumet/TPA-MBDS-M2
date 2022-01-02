import request from '../request';

export const fetchAllColors = () => request('/colors');
export const fetchAllDoors = () => request('/doors');