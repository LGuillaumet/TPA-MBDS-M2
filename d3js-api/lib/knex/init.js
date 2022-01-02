const { Model } = require('objection');
const {
    CLIENT,
    VERSION,
    HOST,
    PG_PORT,
    USER,
    PASSWORD,
    DATABASE,
} = process.env;

const knex = require('knex')({
    client: CLIENT,
    version: VERSION,
    connection: {
      host : HOST,
      port : PG_PORT,
      user : USER,
      password : PASSWORD,
      database : DATABASE,
    },
});
Model.knex(knex);

module.exports.knex = knex;
