import request from '../request';

export const fetchUserProfilByBrand = (brand) => request(`/lambda/${brand}`);