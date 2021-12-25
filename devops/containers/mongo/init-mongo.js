
db = new Mongo().getDB("datalake");

db.createCollection('clients', { capped: false });

db.createUser({
    user: 'app',
    pwd: 'app',
    roles: [
        {
            role: 'readWrite',
            db: 'datalake',
        },
    ],
});

db.clients.insert([
{ 
    "age": 10,
    "sexe":"M",
    "taux":4,
    "situation":"Celibataire",
    "nbChildren":4,
    "haveSecondCar": false,
    "immatriculation":"immatriculation"
},
{ 
    "age": 13,
    "sexe":"F",
    "taux":4,
    "situation":"Celibataire",
    "nbChildren":4,
    "haveSecondCar": false,
    "immatriculation":"immatriculation"
}
]);

db.carbon.insert([
    {
        "marque": "AUDI",
        "model": "E-TRON SPORTBACK 55 (408ch) quattro",
        "bonus": -6000,
        "rejection": 0,
        "energiecost": 319
    }
])
