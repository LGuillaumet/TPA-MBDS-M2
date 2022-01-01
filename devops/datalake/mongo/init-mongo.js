
db = new Mongo().getDB("datalake");

db.createCollection('clients', { capped: false });
db.createCollection('carbon', { capped: false });

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

db.clients.createIndex( { registrationId: 1 } )