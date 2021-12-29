
with 
    c as (
        select age, sexe, taux, situation, nbchildren, havesecondcar, registrationid
        from mongodb.datalake.clients
        union distinct 
        select age, sexe, taux, situation, nbchildren, havesecondcar, registrationid
        from hive.datalake.clients
    ),
    r as (
        select registrationid, marque, nom, puissance, longueur, nbplaces, nbportes, couleur, occasion, prix
        from cassandra.datalake.registration
    )
select r.registrationid, age, sexe, taux, situation, nbChildren, havesecondcar, marque, nom, puissance, longueur, nbplaces, nbportes, couleur, occasion, prix
from c
left join r on c.registrationid = r.registrationid;



with 
    c as (
        select age, sexe, taux, situation, nbChildren, havesecondcar, registrationId
        from mongodb.datalake.clients
        union distinct 
        select age, sexe, taux, situation, nbChildren, havesecondcar, registrationId
        from hive.datalake.clients
    )
select count(*) from c;

select count(registrationid)
from cassandra.datalake.registration;


with 
    c as (
        select age, sexe, taux, situation, nbChildren, havesecondcar, registrationId
        from mongodb.datalake.clients
    ),
    r as (
        select registrationid, marque, nom, puissance, longueur, nbplaces, nbportes, couleur, occasion, prix
        from cassandra.datalake.registration
    )
select r.registrationid, age, sexe, taux, situation, nbChildren, havesecondcar, marque, nom, puissance, longueur, nbplaces, nbportes, couleur, occasion, prix
from c
left join r on c.registrationid = r.registrationid;



with 
    c as (
        select registrationId
        from mongodb.datalake.clients
        union distinct 
        select registrationId
        from hive.datalake.clients
    )
select count(distinct(registrationId))
from c;

select * from mongodb.datalake.clients m
inner join hive.datalake.clients h on m.registrationid = h.registrationid;