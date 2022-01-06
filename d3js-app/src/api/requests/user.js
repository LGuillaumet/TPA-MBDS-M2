import request from '../request';

export const fetchUserProfilByBrand = (brand) => request(`/lambda/${brand}`);

export const fetchUserQByBrand = (brand) => request(`/userQBrand/${brand}`);