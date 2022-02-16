'use strict';

const express = require('express');

// константы
const port = 8080;
const host = '127.0.0.1';

// приложение
const app = express();
app.get('/', (req, res) => {
  res.send('Hello World');
});

app.listen(port, host);
console.log(`running on http://${host}:${port}`);
