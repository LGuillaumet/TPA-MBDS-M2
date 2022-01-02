require('dotenv').config();
const express = require('express');
const app = express();
const { PORT } = process.env;
const apiRoutes = require('./routes/api');

app.use('/api/v1', apiRoutes);

app.listen(PORT, () => {
  console.log(`Application exemple à l'écoute sur le port ${PORT}!`)
});