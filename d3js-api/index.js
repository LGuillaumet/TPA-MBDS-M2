require('dotenv').config();
const express = require('express');
const app = express();
const { PORT } = process.env;
const apiRoutes = require('./routes/api');
const cors = require('cors')


app.use(cors())
app.use('/api/v1', apiRoutes);

app.listen(PORT, () => {
  console.log(`Application exemple à l'écoute sur le port ${PORT}!`)
});