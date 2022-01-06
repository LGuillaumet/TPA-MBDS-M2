const express = require('express');
const router = express.Router();
const { knex } = require('../lib/knex/init');

let source = "public";

const labelType = {
    Short_nbpo_3_nbpl_5: 'Sous-compactes',
    Medium_nbpo_5_nbpl_5: 'Compactes',
    Long_nbpo_5_nbpl_5: 'Berlines',
    Short_nbpo_5_nbpl_5: 'Citadines',
    VeryLong_nbpo_5_nbpl_5: 'Grandes Berlines',
    Long_nbpo_5_nbpl_7: 'Monospaces'
}

if (process.env.NODE_ENV !== 'production') { }
router.get('/colors', async (req, res) => {
    const ret = await knex('datawarehouse.cars').distinct().pluck('couleur');
    res.json({ couleurs: ret });
});

router.get('/doors', async (req, res) => {
    const ret = await knex('datawarehouse.cars').distinct().pluck('nbportes');
    res.json({ portes: ret });
});

router.get('/marques', async (req, res) => {
    const marques = await knex('datawarehouse.cars').distinct().pluck('marque');
    res.json({ marques });
});

router.get('/categories', async (req, res) => {
    const categories = await knex('datawarehouse.typecategories').distinct().pluck('name');
    res.json({ categories });
});

router.get('/listall', async (req, res) => {
    const carbon = await knex('datawarehouse.carbon');
    const registrationsCars = await knex('datawarehouse.registrations').join('datawarehouse.cars', 'datawarehouse.cars.id', 'datawarehouse.registrations.idcar');
    const ret = [];

    carbon.forEach((carCarbon) => {
        if (carCarbon.rejet > 0) {
            ret.push({
                nomMarque: carCarbon.marque,
                pollution: carCarbon.rejet,
            });
        }
    });

    res.json(ret);
});

router.get('/listPredictionTypeCar', async (req, res) => {
    const predictionTypeCar = await
        knex.select('m.*', 't.*').from('datawarehouse.marketing AS m').join(
            'datawarehouse.marketingtypecarsprediction AS p', 'p.idmarketing', 'm.id').join(
                'datawarehouse.typecategories AS t', 't.id', 'p.idpredictioncategorietype');

    const ret = {};

    predictionTypeCar.forEach((prediction) => {
        const name = [labelType[prediction.name]]
        if (ret[name]) {
            ret[name] =
                [...ret[name], prediction];
        }
        else {
            ret[name] = [prediction];

        }
    });
    res.json(ret);

});

router.get('/listPredictionCar', async (req, res) => {
    const predictionTypeCar = await
        knex.select('cars.*', ' c.prix', 'c.occasion', 't.id AS idtype', 't.name').from('datawarehouse.catalogue AS c ').join(
            'datawarehouse.cars AS cars', 'cars.id', 'c.idcar').join(
                'datawarehouse.carscategories AS cat', function () {
                    this
                        .on('cars.puissance', '=', 'cat.puissance')
                        .on('cars.longueur', '=', 'cat.longueur')
                        .on('cars.nbplaces', '=', 'cat.nbplaces')
                        .on('cars.nbportes', '=', 'cat.nbportes')

                }).join('datawarehouse.typecategories AS t', 't.id', 'cat.idcategorietype');

    const ret = {};

    predictionTypeCar.forEach((prediction) => {
        const name = [labelType[prediction.name]]
        if (ret[name]) {
            ret[name] =
                [...ret[name], prediction];
        }
        else {
            ret[name] = [prediction];

        }
    });
    res.json(ret);

});

router.get('/userQBrand/:brand', async (req, res) => {
    const { brand } = req.params;

    const clients = await knex('datawarehouse.carmarque_total_stats').where({ marque: brand.toUpperCase() });

    if (clients.length <= 0) {
        res.json({ error: 'Marque non trouvée' });
        return;
    }

    res.json(clients);

});

router.get('/filter', async (req, res) => {
    const { couleurs, portes, occasion, source } = req.query;
    const ret = [];
    const colors = (couleurs ? (couleurs).split(',') : null);
    const doors = (portes ? (portes).split(',') : null);
    console.log(couleurs, portes, occasion, source)
    if (!couleurs && !portes && !occasion && !source) {
        return res.json({ error: 'Vous devez renseigner au moins un critère de filtrage' });
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

    marques.forEach((marque, index) => {
        let tmpCars = cars.filter((car) => car.marque === marque);
        const nbCarsByMarque = tmpCars.length || 1;

        if (!!doors && doors?.length >= 1) {
            tmpCars = tmpCars.filter((car) => doors.includes(car.nbportes.toString()));
        }
        if (!!colors && colors?.length >= 1) {
            tmpCars = tmpCars.filter((car) => colors.includes(car.couleur));
        }
        if (occasion != undefined) {
            const occasionBool = occasion === 'true';
            tmpCars = tmpCars.filter((car) => car.occasion === occasionBool);
        }
        ret[index] = {
            marque: marque,
            proportion: (tmpCars.length / nbCarsByMarque) * 100,
        };
    });

    res.json(ret);
});

router.get('/lambda/:brand', async (req, res) => {
    const { brand } = req.params;

    const cars = await knex('datawarehouse.registrations').join('datawarehouse.cars', 'datawarehouse.cars.id', 'datawarehouse.registrations.idcar').where({ marque: brand.toUpperCase() });
    const ids = cars.map((c) => c.registrationid);

    if (ids.length <= 0) {
        res.json({ error: 'Marque non trouvée' });
        return;
    }

    const splitArrayIntoChunksOfLen = (arr, len) => {
        var chunks = [], i = 0, n = arr.length;
        while (i < n) {
            chunks.push(arr.slice(i, i += len));
        }
        return chunks;
    }

    const arrays = splitArrayIntoChunksOfLen(ids, 10000);

    const clients = await Promise.all(arrays.map(async (arr) => {
        return knex('datawarehouse.clients').whereIn('registrationid', arr);
    }));
    const mergedClients = [].concat.apply([], clients);

    let age = 0;
    let nbAge = 0;

    let sexeH = 0;
    let nbSexe = 0;

    let taux = 0;
    let nbTaux = 0;

    let situationSingle = 0;
    let nbSituation = 0;

    let nbChildren = 0;
    let nbNbChildren = 0;

    let haveSecondCar = 0;
    let nbHaveSecondCar = 0;

    const ageMax = mergedClients.reduce((max, p) => p.age > max ? p.age : max, 0);
    const tauxMax = mergedClients.reduce((max, p) => p.taux > max ? p.taux : max, 0);
    const nbChildrenMax = mergedClients.reduce((max, p) => p.nbchildren > max ? p.nbchildren : max, 0);

    mergedClients.forEach((client) => {
        if (client.age) {
            age += client.age;
            nbAge++;
        }

        if (client.sexe) {
            if (client.sexe === 'M') {
                sexeH++;
            }
            nbSexe++;
        }

        if (client.taux) {
            taux += client.taux;
            nbTaux++;
        }

        if (client.situation) {
            nbSituation++;
            if (client.situation === 'Single') {
                situationSingle++;
            }
        }

        if (client.nbchildren !== null || client.nbchildren !== undefined) {
            nbNbChildren++;
            nbChildren += client.nbchildren;
        }

        if (client.havesecondcar !== null || client.havesecondcar !== undefined) {
            if (client.havesecondcar) {
                haveSecondCar++;
            }
            haveSecondCar++;
        }

    });

    const ret = {
        age: age / nbAge,
        sexe: sexeH / nbSexe * 100 >= 50 ? 'H' : 'F',
        taux: taux / nbTaux,
        situation: situationSingle / nbSituation * 100 >= 50 ? 'Single' : 'Married',
        nbchildren: nbChildren / nbNbChildren,
        havesecondcar: haveSecondCar / nbHaveSecondCar * 100 >= 50 ? true : false,
        ageMax,
        tauxMax,
        nbChildrenMax,
    }


    res.json(ret);
});

router.get('/model/lambda/:model', async (req, res) => {
    const { model } = req.params;

    console.log("model", model);
    const cars = await knex('datawarehouse.registrations').join('datawarehouse.cars', 'datawarehouse.cars.id', 'datawarehouse.registrations.idcar').where({ nom: model });
    const ids = cars.map((c) => c.registrationid);

    if (ids.length <= 0) {
        res.json({ error: 'Modele non trouvé dans registrations' });
        return;
    }

    const splitArrayIntoChunksOfLen = (arr, len) => {
        var chunks = [], i = 0, n = arr.length;
        while (i < n) {
            chunks.push(arr.slice(i, i += len));
        }
        return chunks;
    }

    const arrays = splitArrayIntoChunksOfLen(ids, 10000);

    const clients = await Promise.all(arrays.map(async (arr) => {
        return knex('datawarehouse.clients').whereIn('registrationid', arr);
    }));
    const mergedClients = [].concat.apply([], clients);

    let age = 0;
    let nbAge = 0;

    let sexeH = 0;
    let nbSexe = 0;

    let taux = 0;
    let nbTaux = 0;

    let situationSingle = 0;
    let nbSituation = 0;

    let nbChildren = 0;
    let nbNbChildren = 0;

    let haveSecondCar = 0;
    let nbHaveSecondCar = 0;

    const ageMax = mergedClients.reduce((max, p) => p.age > max ? p.age : max, 0);
    const tauxMax = mergedClients.reduce((max, p) => p.taux > max ? p.taux : max, 0);
    const nbChildrenMax = mergedClients.reduce((max, p) => p.nbchildren > max ? p.nbchildren : max, 0);

    mergedClients.forEach((client) => {
        if (client.age) {
            age += client.age;
            nbAge++;
        }

        if (client.sexe) {
            if (client.sexe === 'M') {
                sexeH++;
            }
            nbSexe++;
        }

        if (client.taux) {
            taux += client.taux;
            nbTaux++;
        }

        if (client.situation) {
            nbSituation++;
            if (client.situation === 'Single') {
                situationSingle++;
            }
        }

        if (client.nbchildren !== null || client.nbchildren !== undefined) {
            nbNbChildren++;
            nbChildren += client.nbchildren;
        }

        if (client.havesecondcar !== null || client.havesecondcar !== undefined) {
            if (client.havesecondcar) {
                haveSecondCar++;
            }
            haveSecondCar++;
        }

    });

    const ret = {
        age: age / nbAge,
        sexe: sexeH / nbSexe * 100 >= 50 ? 'H' : 'F',
        taux: taux / nbTaux,
        situation: situationSingle / nbSituation * 100 >= 50 ? 'Single' : 'Married',
        nbchildren: nbChildren / nbNbChildren,
        havesecondcar: haveSecondCar / nbHaveSecondCar * 100 >= 50 ? true : false,
        ageMax,
        tauxMax,
        nbChildrenMax,
    }


    res.json(ret);
});

router.get('/ratio/:brand', async (req, res) => {
    const { brand } = req.params;

    const marques = await knex('datawarehouse.cars').distinct().pluck('marque');

    if (!marques.includes(brand.toUpperCase())) {
        return res.json({ error: 'Marque inconnue' });
    }

    const ret = {};
    const catalogueCars = await knex('datawarehouse.catalogue').join('datawarehouse.cars', 'datawarehouse.cars.id', 'datawarehouse.catalogue.idcar').where({ marque: brand.toUpperCase() });
    const registrationsCars = await knex('datawarehouse.registrations').join('datawarehouse.cars', 'datawarehouse.cars.id', 'datawarehouse.registrations.idcar').where({ marque: brand.toUpperCase() });
    const typeCategories = await knex('datawarehouse.typecategories');
    const carscategories = await knex('datawarehouse.carscategories');
    const cars = [...catalogueCars, ...registrationsCars];

    carscategories.forEach((category) => {
        const carsLongueur = cars.filter((car) => car.longueur === category.longueur);
        const carType = typeCategories.find((type) => type.id === category.idcategorietype);
        if (carsLongueur.length > 0) {
            ret[labelType[carType.name]] = carsLongueur.length;
        }
    });


    res.json(ret);
});

router.get('/numberModeles/:brand', async (req, res) => {
    const { brand } = req.params;

    const marques = await knex('datawarehouse.cars').distinct().pluck('marque');

    if (!marques.includes(brand.toUpperCase())) {
        return res.json({ error: 'Marque inconnue' });
    }

    const ret = {};
    const catalogueCars = await knex('datawarehouse.catalogue').join('datawarehouse.cars', 'datawarehouse.cars.id', 'datawarehouse.catalogue.idcar').where({ marque: brand.toUpperCase() });
    const registrationsCars = await knex('datawarehouse.registrations').join('datawarehouse.cars', 'datawarehouse.cars.id', 'datawarehouse.registrations.idcar').where({ marque: brand.toUpperCase() });
    const listModeleBrand = await knex('datawarehouse.cars').where({ marque: brand.toUpperCase() });


    const cars = [...catalogueCars, ...registrationsCars];

    listModeleBrand.forEach((carModele, index) => {
        const carsNumber = cars.filter((car) => car.idcar === carModele.id).length
        if (ret[carModele.nom]) {
            ret[carModele.nom] =
                [
                    ...ret[carModele.nom],
                    { name: `${carModele.puissance}CH-${carModele.couleur}`, value: carsNumber }
                ];
        }
        else {
            ret[carModele.nom] = [{ name: `${carModele.puissance}CH-${carModele.couleur}`, value: carsNumber }];
        }
    });


    res.json(ret);
});

module.exports = router;