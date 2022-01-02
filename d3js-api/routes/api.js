const express = require('express');
const router = express.Router();
const { knex } = require('../lib/knex/init');

router.get('/colors', async (req, res) =>{
    const ret = await knex('datawarehouse.cars').distinct().pluck('couleur');
    res.json({ couleurs: ret });
});

router.get('/doors', async (req, res) =>{
    const ret = await knex('datawarehouse.cars').distinct().pluck('nbportes');
    res.json({ portes: ret });
});

router.get('/marques', async (req, res) =>{
    const marques = await knex('datawarehouse.cars').distinct().pluck('marque');
    res.json({ marques });
});


router.get('/filter', async (req, res) => {
    const { couleurs, portes, occasion, source } = req.query;
    const ret = {};
    const colors = (couleurs || "").split(',');

    if (!colors && colors?.length > 0 && !portes && !occasion && !source) {
        res.json({ error: 'Vous devez renseigner au moins un critère de filtrage' });
        return;
    }

    const marques = await knex('datawarehouse.cars').distinct().pluck('marque');

    let cars = [];
    if (source === 'catalogue') {
        cars = await knex('datawarehouse.catalogue').join('datawarehouse.cars', 'datawarehouse.cars.id', 'datawarehouse.catalogue.idcar');
    } else if (source === 'registrations') {
        cars = await knex('datawarehouse.registrations').join('datawarehouse.cars', 'datawarehouse.cars.id', 'datawarehouse.registrations.idcar');
    } else {
        const catalogueCars = await knex('datawarehouse.catalogue').join('datawarehouse.cars', 'datawarehouse.cars.id', 'datawarehouse.catalogue.idcar');
        const registrationsCars = await knex('datawarehouse.registrations').join('datawarehouse.cars', 'datawarehouse.cars.id', 'datawarehouse.registrations.idcar');
        cars = [...catalogueCars, ...registrationsCars];
    }

    marques.forEach((marque) => {
        let tmpCars = cars.filter((car) => car.marque === marque);
        const nbCarsByMarque = tmpCars.length || 1;

        if (portes) {
            tmpCars = tmpCars.filter((car) => car.nbportes === parseInt(portes, 10));
        }
        if (colors && colors?.length > 0) {
            tmpCars = tmpCars.filter((car) => colors.includes(car.couleur));
        }
        if (occasion != undefined) {
            const occasionBool = occasion === 'true';
            tmpCars = tmpCars.filter((car) => car.occasion === occasionBool); 
        }
        ret[marque] = (tmpCars.length / nbCarsByMarque) * 100;
    });

    res.json(ret);
});
 
 
module.exports = router;