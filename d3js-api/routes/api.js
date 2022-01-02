const express = require('express');
const router = express.Router();
const { knex } = require('../lib/knex/init');

router.get('/colors', async (req, res) =>{
    const ret = await knex('datawarehouse.cars').distinct().pluck('couleur');
    res.json({ couleurs: ret });
});
 
module.exports = router;