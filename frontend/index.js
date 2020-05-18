const express  = require('express');
const axios = require('axios');
const path = require('path');
const client = require('prom-client');
const exphbs = require('express-handlebars');

const app = express();

const summary = new client.Summary({
    name: 'frontend_api_calls_made_to_backend',
    help: 'All_the_Api_Calls_made_to_backend'
  });

app.engine('handlebars', exphbs());
app.set('view engine', 'handlebars');

app.get('/', (req, res)=> {
    const end = summary.startTimer();
    console.log(process.env.API_URL);
    axios.get(`https://${process.env.API_URL}/v1/recipes/`)
    .then(result => {
        // result.data.sort(function(a,b){
        //     let aUpdated = new Date(a.created_ts);
        //     let bUpdated = new Date(b.created_ts);
        //     return bUpdated-aUpdated;
        // });
        //console.log(result.data);
        res.render('index', {data: result.data})
    })
    .catch(error => {
        console.log(error.response.data)
        res.render('error-view', {data: error.response.data})
    })
    end();
    
})

app.get('/actuator/prometheus', (req,res)=>{
    res.set('Content-Type', client.register.contentType);
    res.end(client.register.metrics())
})

const PORT = 80;

app.listen(PORT, console.log(`Server started on port: ${PORT} and API_URL is ${process.env.API_URL}`))
