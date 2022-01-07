install.packages("rstudioapi")
install.packages("questionr")
install.packages("dplyr")
install.packages("C50")
install.packages('tidyr')

library("rstudioapi")
library(questionr)
library(dplyr)
library("C50")
library("tidyr")

install.packages("rJava")
install.packages("DBI")
install.packages("RJDBC")
install.packages('RPostgres')

library(rJava)
library(RJDBC)
library('RPostgres');

setwd(dirname(getActiveDocumentContext()$path))
wd <- getwd()

pathPresto <- file.path(wd,"presto-jdbc-0.266.1.jar")
presto_driver <- JDBC(classPath = pathPresto)

##
# Connexion aux databases
##
presto_jdbc <- dbConnect(presto_driver,user = "user", url = "jdbc:presto://localhost:8080", SSL = TRUE)

con = dbConnect(RPostgres::Postgres(), user="postgres", password="postgres", host="localhost", port=5432, dbname="postgres")

clients <- dbGetQuery(presto_jdbc, "select age, sexe, taux, situation, nbchildren, havesecondcar, registrationid from mongodb.datalake.clients union all select age, sexe, taux, situation, nbchildren, havesecondcar, registrationid from hive.datalake.clients")
print(clients)

##
# Transformation de la dataframe clients
##

#Nettoyage de la variable "Age"
table(clients$age)
summary(clients)
clients$age[clients$age <= 0] = NA
#609NAs
which(is.na(clients$age))
sum(is.na(clients$age))
clients = clients %>% drop_na()

#Nettoyage de la variable "Sexe"
table(clients$sexe)
summary(clients$sexe)
clients$sexe[clients$sexe == "?" | clients$sexe == "" | clients$sexe == "N/D"] = NA
which(is.na(clients$sexe))
sum(is.na(clients$sexe))
clients = clients %>% drop_na()

clients$sexe[clients$sexe == "Féminin" | clients$sexe == "Femme"] = "F"
clients$sexe[clients$sexe == "Masculin" | clients$sexe == "Homme"] = "M"

#Nettoyage de la variable "situation"
table(clients$situation)
summary(clients$situation)
clients$situation[clients$situation == "?" | clients$situation == "" | clients$situation == "N/D"] = NA
which(is.na(clients$situation))
sum(is.na(clients$situation))
clients = clients %>% drop_na()

clients$situation[clients$situation == "Célibataire" | clients$situation == "Seul" | clients$situation == "Seule"] = "Single"

clients$situation = as.factor(clients$situation)
levels(clients$situation) = c("Divorced","Couple","Married","Single")

#Nettoyage sur la variable "nbchildren"
table(clients$nbchildren)
summary(clients$nbchildren)
clients$nbchildren[clients$nbchildren < 0] = NA
which(is.na(clients$nbchildren))
sum(is.na(clients$nbchildren))
clients = clients %>% drop_na()

#Nettoyage sur la variable "taux"
table(clients$taux)
summary(clients$taux)
clients$taux[clients$taux < 0] = NA
which(is.na(clients$taux))
sum(is.na(clients$taux))
clients = clients %>% drop_na()

#Nettoyage sur la variable "havesecondcar"
table(clients$havesecondcar)
summary(clients$havesecondcar)
clients$havesecondcar[clients$havesecondcar == "?" | clients$havesecondcar == " "] = NA
which(is.na(clients$havesecondcar))
sum(is.na(clients$havesecondcar))
clients = clients %>% drop_na()

sum(is.na(clients))

##
# Recuperation des r�sultats de classification v�hicule et jointure entre les dataframes categories, immatriculation et clients
##

data <- dbGetQuery(presto_jdbc, "select * from postegres.datawarehouse.carscategories")
immat <- dbGetQuery(presto_jdbc, "select puissance, longueur, nbplaces, nbportes, registrationid from cassandra.datalake.registration")

immat$longueur[immat$longueur == "très longue"] = "VeryLong"
immat$longueur[immat$longueur == "longue"] = "Long"
immat$longueur[immat$longueur == "courte"] = "Short"
immat$longueur[immat$longueur == "moyenne"] = "Medium"
immat$longueur = as.factor(data$longueur)

datalink <- immat %>%
  inner_join(data, by = c("puissance", "longueur", "nbplaces", "nbportes")) %>%
  select(c(-puissance, -longueur, -nbplaces, -nbportes)) %>%
  inner_join(clients, by = c("registrationid"))
  
sum(is.na(datalink))
str(datalink)

#Voir indices des dupliques
which(duplicated(datalink))

##Train la prediction sur le fichier client
finaldata = subset(datalink, select = c(-registrationid))

##
# Constitutiondes dataframes de train et de validation
##

#2/3 pour train and 1/3 pour test
sizeData = nrow(finaldata)
sizeTrain = round(sizeData * (2/3), digits = 0)
dataTrain = finaldata[1:sizeTrain,]
dataVal = finaldata[(sizeTrain+1):sizeData,]

str(dataTrain)
dataTrain$idcategorietype<-as.factor(dataTrain$idcategorietype)
dataVal$idcategorietype<-as.factor(dataVal$idcategorietype)

##
# Constitution des classifieurs
# Utilisation de C50
# On explique cluster en fonction des variables explicatives (nbEnfantsAcharge, situationFamiliale, X2eme.voiture, taux)
##
dataTrain

modelClient = C5.0(idcategorietype~nbchildren + situation  + havesecondcar + taux, dataTrain)

#Modeliser l'arbre
plot(modelClient, type="simple")

predictModelClient = predict(modelClient, dataVal, type="class")
print(predictModelClient)
table(predictModelClient)

#Ajout de la variable dans le dataframe
dataVal$predictionidcategorietype = predictModelClient
#Afficher le taleau pour le dataframe dataVal[toutes les lignes, colonnes choisies]
View(dataVal[,c("idcategorietype", "predictionidcategorietype")])

#Calcul du taux de succes
successClientPrediction = nrow(dataVal[dataVal$idcategorietype == dataVal$predictionidcategorietype,]) / nrow(dataVal)*100

##
# Prediction sur marketing
##

marketing <- dbGetQuery(presto_jdbc, "select * from cassandra.datalake.marketing")

marketing

marketing$situation[marketing$situation == "Célibataire" | marketing$situation == "Seul" | marketing$situation == "Seule"] = "Single"
marketing$situation[marketing$situation == "En Couple"] = "Couple"
marketing$situation = as.factor(marketing$situation)

predictModelmarketing = predict(modelClient, marketing, type="class")
print(predictModelmarketing)
table(predictModelmarketing)

#Ajout de la variable dans le dataframe
marketing$idpredictioncategorietype = as.integer(predictModelmarketing)

# supprime les champs devenu inutile
marketing <- marketing %>%
  select(c(-age, -havesecondcar, -nbchildren , -sexe, -situation, -taux))

# clean la datable avant d'�crire pour override
dbExecute(con, "delete from datawarehouse.marketingtypecarsprediction")

# renomme le champ pour match avec la table dans postgres
names(marketing)[names(marketing) == "id"] <- "idmarketing"

# �crit le r�sultat dans la table marketingtypecarsprediction de postgres
dbWriteTable(con, Id(schema = "datawarehouse", table = "marketingtypecarsprediction"), value = marketing, append = TRUE)

dbDisconnect(presto_jdbc)
dbDisconnect(con)






