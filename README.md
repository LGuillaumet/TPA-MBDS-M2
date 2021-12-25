## TPA-MBDS-M2

# Contexte du Projet
Vous avez été contacté par un concessionnaire automobile afin de l'aider à mieux cibler les véhicules
susceptibles d'intéresser ses clients. Pour cela il met à votre disposition :
- Son catalogue de véhicules
- Son fichier clients concernant les achats de l'année en cours
- Un accès à toutes les informations sur les immatriculations effectuées cette année
- Une brève documentation des données
- Un vendeur (voir son interview ci-dessous)
# Votre client sera satisfait si vous lui proposez un moyen afin :
- Qu'un vendeur puisse en quelques secondes évaluer le type de véhicule le plus susceptible d'intéresser des clients qui se présentent dans la concession
- Qu'il puisse envoyer une documentation précise sur le véhicule le plus adéquat pour des clients sélectionnés par son service marketing (voir ci-dessous) 


## A Faire 

REFLECHIR QUEL BASE SERT DE STOCKAGE DE RESULTATS ANALYSE
    - nouvelle base ? Postgre ? Oracle ?
    - existante ? hive ? mongo ? cassandra ?

### Data injestion

Ecrire les producers kafka afin de remplir le datalake via les topics kafka et l'envoie des données sous format JSON. Voir pour ingerer des données csv (car vu la gueule du fichier CO2 impossible à mapper en JSON ? est ce possible ? comment cela peut il exister ? ff) ?

Topics kafka :

- upserts-marketing-cassandra -> cassandra.datalake.marketing
- upserts-registration-cassandra -> cassandra.datalake.registration
- upserts-catalogue-hive -> hive.datalake.catalogue
- upserts-clients-hive -> hive.datalake.clients
- upserts-carbon-mongo -> mongodb.datalake.carbon
- upserts-clients-mongo -> mongodb.datalake.clients


### HADOOP MAP REDUCE / SPARK Sergio SIMONIAN

NOUVELLE TABLE POUR SAVE RESULTATS

- Nettoyer les données CO2
- Puis obtenir les données suivantes :
    - Valeur moyenne de CO2 pour chaques marques
    - Valeur moyenne Bonus/Malus pour chaques marques
    - Valeur moyenne Energie pour chaques marques 
- Croiser les données avec les données catalogue

### Datavis

NOUVELLE TABLE PRECALCULE DES REQUETES ?
QUEL TECHNOLOGIE POUR API ?

Préparation/Précalcule des données que l'on à besoin pour le front (API, MapReduce job, Spark etc)

- Décrire la chaîne de traitement (« visualisation pipeline ») que, à partir de donnés bruts, permet de créer une représentation graphique et interactive avec l’ensemble de données. 
- Décrire les utilisateurs visés
- Décrire les objectifs de visualisation et le tâches utilisateurs
- Développer de (au moins) trois techniques de visualisations avec l’API D3JS selon les contraintes suivantes :
    - Utiliser une base de données multidimensionnelles (minimum 5 attributs différents)
    - Prévoir de l’interaction avec l’ensemble des données (ex. navigation, sélection, filtres, etc.) 
    - Deux niveaux de visualisation, c’est-à-dire une vision globale (« overview ») plus contexte
    - Donner la possibilité de charger des ensembles de données indépendants de l’application

### Data analyse 

NOUVELLE TABLE POUR SAVE RESULTATS

Synthèse :

- L’analyse  exploratoire  des  données = valeurs incohérentes, codage des valeurs manquantes, etc. et valeurs doublons, variables liées, variables d’importance parti-culière ou bien inutiles, etc.
- Identification des catégories de véhicules : En fonction de taille, puissance, prix, etc définir des catégories de véhicule comme sport familliale, ville etc
- Application des catégories de véhicules définies aux données des Immatriculations : Lié aux véhicule vendu (immatriculation) aux catégories précèdente
- Fusion des données Clients et Immatriculations : L'objectif est de faire la fusion entre les données des Clients et des Immatriculations afin d'obtenir sur une même ligne l'ensemble des informations sur le client (âge, sexe, etc.) et sur le vé-hicule qu'il a acheté (avec sa catégorie).
- Classificateur pour prédiction catégorie véhicule : à partir du résultat de la fusion précédente un classifieur (mo-dèle de classification supervisée) permettant d'associer aux caractéristiques des clients (âge, sexe, etc.) une catégorie de véhicules. (modifié)
- modèle de prédiction aux données Marketing : données Marketing contiennent les informations sur les clients pour lesquels on souhaite pré-dire une catégorie de véhicules, prédire pour chacun de ces clients la catégorie de véhicules qui lui correspond le mieux en utilisant le classifieur généré durant l'étape précédente

Note camille :

- faire une jointure entre client et immat pour avoir les liens
- on aura nos 2 donnees cote a cote
- clustering sur cette grosse matrice
- segmentation clientele pour distinguer categories
- donner les noms a la fin de notre clustering
- group by cluster et regarder les infos qui sont regroupees
- categorie variable a prevoir pour marketing
- reprendre les colonnes de client qui correspondent a marketing pour train 
- notre algo et ensuite les appliquer a notre fichier marketing`

!!!SUPPRIMER LES IDENTIFIANTS!!!
code NA pour valeurs manquantes ou aberrantes et ne va pas les utiliser dans son calcul OU estimer la valeur probable (si l'algo ne veut pas de NA) OU remplacer par la moyenne des personnes qui ont la même catégorie (moyenne de ces lignes)
si besoin, supprimer les lignes où il y a NA

