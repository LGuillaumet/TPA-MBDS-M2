CREATE SCHEMA if not exists datawarehouse;

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

CREATE TABLE if not exists datawarehouse.cars(
	id              bigint,
	marque          VARCHAR (50) NOT NULL,
	nom           	VARCHAR (255) NOT NULL,
	puissance       integer,
	longueur 		VARCHAR (50),
	nbplaces        integer,
	nbportes    	integer,
	couleur			VARCHAR (50),
	CONSTRAINT cars_PK PRIMARY KEY (id)
) WITHOUT OIDS;

CREATE UNIQUE INDEX if not exists cars_unique_idx ON datawarehouse.cars (marque, nom, puissance, longueur, nbplaces, nbportes, couleur);
CREATE INDEX if not exists cars_char_idx ON datawarehouse.cars (puissance, longueur, nbplaces, nbportes);
CREATE INDEX if not exists cars_marque_idx ON datawarehouse.cars (marque);
CREATE INDEX if not exists cars_nom_idx ON datawarehouse.cars (nom);
CREATE INDEX if not exists cars_marque_nom_idx ON datawarehouse.cars (marque, nom);

CREATE TABLE if not exists datawarehouse.registrations(
	id              VARCHAR (50),
	occasion        BOOL,
	prix           	float,
	idCar			bigint,
	CONSTRAINT registrations_PK PRIMARY KEY (id),
	CONSTRAINT registrations_cars_FK FOREIGN KEY (idCar) REFERENCES datawarehouse.cars(id)
)  WITHOUT OIDS;

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

CREATE TABLE if not exists datawarehouse.carbon(
	marque          VARCHAR (50),
	bonusmalus      float,
	rejet           float,
	coutenergie		float,
	CONSTRAINT carbon_PK PRIMARY KEY (marque)
)  WITHOUT OIDS;

CREATE TABLE if not exists datawarehouse.carscategories(
	puissance       integer,
	longueur 		VARCHAR (50),
	nbplaces        integer,
	nbportes    	integer,
	category		VARCHAR (50)
);

CREATE INDEX if not exists carscategories_cars_idx ON datawarehouse.carscategories (puissance, longueur, nbplaces, nbportes);