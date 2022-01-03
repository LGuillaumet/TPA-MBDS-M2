import request from '../request';

export const fetchAllBrands= () => request('/marques');

export const fetchFilterAllBrands = (params) => request(`/filter?portes=${params.doors.join()}&couleurs=${params.colors.join()}&occasion=${params.occasion}&source=${params.source}`);