La uri para transformar es http://localhost:8080/exchangeRates/changecurrency - POST
Siguiendo la siguiente estructura
{
    "fromCurrencyCode": "USD",
    "toCurrencyCode": "JPY",
    "value": 5
}

para agregar un exchange rate la siguiente uri http://localhost:8080/exchangeRates - POST
{
    "id":{
        "fromCurrencyCode": "USD",
        "toCurrencyCode": "EUR"
    },
    "fromFactor": 2,
    "fromPresision": 0,
    "toFactor": 10,
    "toPresision": 0
}

{
    "id":{
        "fromCurrencyCode": "JPY",
        "toCurrencyCode": "EUR"
    },
    "fromFactor": 2,
    "fromPresision": 0,
    "toFactor": 3,
    "toPresision": 0
}

con la uri http://localhost:8080/exchangeRates - GET reviso los exchange rates disponibles
