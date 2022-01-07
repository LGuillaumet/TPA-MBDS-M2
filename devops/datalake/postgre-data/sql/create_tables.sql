CREATE SCHEMA if not exists datawarehouse;

CREATE TABLE if not exists datawarehouse.cars(
	id              bigint,
	marque          VARCHAR (50) NOT NULL,
	originalmarque  VARCHAR (50),
	nom           	VARCHAR (255) NOT NULL,
	puissance       integer,
	longueur 		VARCHAR (50),
	nbplaces        integer,
	nbportes    	integer,
	couleur			VARCHAR (50),
	CONSTRAINT cars_PK PRIMARY KEY (id)
) WITHOUT OIDS;

CREATE INDEX if not exists cars_char_idx ON datawarehouse.cars (puissance, longueur, nbplaces, nbportes);
CREATE INDEX if not exists cars_marque_idx ON datawarehouse.cars (marque);
CREATE INDEX if not exists cars_nom_idx ON datawarehouse.cars (nom);
CREATE INDEX if not exists cars_marque_nom_idx ON datawarehouse.cars (marque, nom);

CREATE TABLE if not exists datawarehouse.registrations(
	id              VARCHAR (50),
	registrationid  VARCHAR (50),
	occasion        BOOL,
	prix           	float,
	idCar			bigint,
	CONSTRAINT registrations_PK PRIMARY KEY (id),
	CONSTRAINT registrations_cars_FK FOREIGN KEY (idCar) REFERENCES datawarehouse.cars(id)
)  WITHOUT OIDS;

CREATE INDEX if not exists registrations_registrationid_idx ON datawarehouse.registrations (registrationid);
CREATE INDEX if not exists registrations_cars_idx ON datawarehouse.registrations (idCar);

CREATE TABLE if not exists datawarehouse.catalogue(
	id              bigint,
	occasion        BOOL,
	prix           	float,
	idCar			bigint,
	CONSTRAINT catalogue_PK PRIMARY KEY (id),
	CONSTRAINT catalogue_cars_FK FOREIGN KEY (idCar) REFERENCES datawarehouse.cars(id)
)  WITHOUT OIDS;

CREATE INDEX if not exists catalogue_cars_idx ON datawarehouse.registrations (idCar);

CREATE TABLE if not exists datawarehouse.clients(
	id               bigint,
	age              integer,
	sexe             VARCHAR(1),
	situation        VARCHAR(50),
	nbchildren 		 integer,
	taux             integer,
	havesecondcar    BOOL,
	registrationid   VARCHAR (50),
	CONSTRAINT clients_PK PRIMARY KEY (id)
) WITHOUT OIDS;

CREATE INDEX if not exists clients_registration_idx ON datawarehouse.clients (registrationid);

CREATE TABLE if not exists datawarehouse.marketing(
	id               text,
	age              integer,
	sexe             VARCHAR(1),
	situation        VARCHAR(50),
	nbchildren 		 integer,
	taux             integer,
	havesecondcar    BOOL,
	CONSTRAINT marketing_PK PRIMARY KEY (id)
) WITHOUT OIDS;

CREATE TABLE if not exists datawarehouse.carbon(
	marque          VARCHAR (50),
	bonusmalus      float,
	rejet           float,
	coutenergie		float,
	CONSTRAINT carbon_PK PRIMARY KEY (marque),
	CONSTRAINT carbon_cars_FK FOREIGN KEY (marque) REFERENCES datawarehouse.cars(marque)
)  WITHOUT OIDS;

CREATE TABLE if not exists datawarehouse.typecategories(
	id       		integer,
	name 			VARCHAR (50),
	CONSTRAINT categories_PK PRIMARY KEY (id)
);

CREATE TABLE if not exists datawarehouse.carscategories(
	puissance       integer,
	longueur 		VARCHAR (50),
	nbplaces        integer,
	nbportes    	integer,
	idcategorietype	integer,
	CONSTRAINT carscategories_type_FK FOREIGN KEY (idcategorietype) REFERENCES datawarehouse.typecategories(id)
);

CREATE INDEX if not exists carscategories_cars_idx ON datawarehouse.carscategories (puissance, longueur, nbplaces, nbportes);


CREATE TABLE if not exists datawarehouse.marketingtypecarsprediction(
	idmarketing					text,
	idpredictioncategorietype   integer,
	CONSTRAINT marketingtypecarsprediction_marketing_FK FOREIGN KEY (idmarketing) REFERENCES datawarehouse.marketing(id),
	CONSTRAINT marketingtypecarsprediction_types_FK FOREIGN KEY (idpredictioncategorietype) REFERENCES datawarehouse.typecategories(id)
);

CREATE INDEX if not exists marketingtypecarsprediction_marketing_idx ON datawarehouse.marketingtypecarsprediction (idmarketing);
CREATE INDEX if not exists marketingtypecarsprediction_type_idx ON datawarehouse.marketingtypecarsprediction (idpredictioncategorietype);

CREATE TABLE if not exists datawarehouse.marketingtypecarsprediction_ml(
	idmarketing					text,
	idpredictioncategorietype   integer,
	prediction					double precision,
	CONSTRAINT marketingtypecarsprediction_marketing_FK FOREIGN KEY (idmarketing) REFERENCES datawarehouse.marketing(id),
	CONSTRAINT marketingtypecarsprediction_types_FK FOREIGN KEY (idpredictioncategorietype) REFERENCES datawarehouse.typecategories(id)
);

CREATE INDEX if not exists marketingtypecarsprediction_ml_marketing_idx ON datawarehouse.marketingtypecarsprediction_ml (idmarketing);
CREATE INDEX if not exists marketingtypecarsprediction_ml_type_idx ON datawarehouse.marketingtypecarsprediction_ml (idpredictioncategorietype);

CREATE TABLE if not exists datawarehouse.carmarque_total_stats(
	marque       		text,
	q0age 				double precision,
    q1age 				double precision,
    q2age 				double precision,
    q3age 				double precision,
    q4age 				double precision,
    q0taux 				double precision,
    q1taux 				double precision,
    q2taux 				double precision,
    q3taux 				double precision,
    q4taux 				double precision,
    q0nbchildren 		double precision,
    q1nbchildren 		double precision,
    q2nbchildren 		double precision,
    q3nbchildren 		double precision,
    q4nbchildren 		double precision,
	CONSTRAINT carmarque_total_stats_car_FK FOREIGN KEY (marque) REFERENCES datawarehouse.cars(marque)
);

CREATE TABLE if not exists datawarehouse.carmarque_age_stats(
	marque       		text,
	age					bigint,
    q0taux 				double precision,
    q1taux 				double precision,
    q2taux 				double precision,
    q3taux 				double precision,
    q4taux 				double precision,
    q0nbchildren 		double precision,
    q1nbchildren 		double precision,
    q2nbchildren 		double precision,
    q3nbchildren 		double precision,
    q4nbchildren 		double precision,
	CONSTRAINT carmarque_age_stats_car_FK FOREIGN KEY (marque) REFERENCES datawarehouse.cars(marque)
);

CREATE TABLE if not exists datawarehouse.carmarque_taux_stats(
	marque       		text,
	taux				bigint,
	q0age 				double precision,
    q1age 				double precision,
    q2age 				double precision,
    q3age 				double precision,
    q4age 				double precision,
    q0nbchildren 		double precision,
    q1nbchildren 		double precision,
    q2nbchildren 		double precision,
    q3nbchildren 		double precision,
    q4nbchildren 		double precision,
	CONSTRAINT carmarque_taux_stats_car_FK FOREIGN KEY (marque) REFERENCES datawarehouse.cars(marque)
);

CREATE TABLE if not exists datawarehouse.carmarque_nbchildren_stats(
	marque       		text,
	nbchildren			bigint,
	q0age 				double precision,
    q1age 				double precision,
    q2age 				double precision,
    q3age 				double precision,
    q4age 				double precision,
    q0taux 				double precision,
    q1taux 				double precision,
    q2taux 				double precision,
    q3taux 				double precision,
    q4taux 				double precision,
	CONSTRAINT carmarque_nbchildren_stats_car_FK FOREIGN KEY (marque) REFERENCES datawarehouse.cars(marque)
);