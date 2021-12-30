
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

db.carbon.insert([
    {
        "marque": "AUDI",
        "model": "E-TRON SPORTBACK 55 (408ch) quattro",
        "bonusMalus": -6000,
        "rejection": 0,
        "energiecost": 319
    }
]);

db.clients.createIndex( { registrationId: 1 } )