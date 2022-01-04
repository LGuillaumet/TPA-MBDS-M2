library("rstudioapi")
library(questionr)
library(dplyr)
library("C50")
library("tidyr")

setwd(dirname(getActiveDocumentContext()$path))
getwd()

##Nettoyage des fichiers de donnees
#Chargement des donnees
client1 = read.csv("Clients_1.csv", header=T, sep=",", dec='.')
client9 = read.csv("Clients_9.csv", header=T, sep=",", dec='.')

#Merge les 2 fichiers
clients = rbind(client1, client9)
str(clients)

#Nettoyage de la variable "Age"
table(clients$age)
clients$age = as.numeric(clients$age)
summary(clients)
clients$age[clients$age == -1] = NA
#609NAs
which(is.na(clients$age))
clients = clients %>% drop_na()

#Nettoyage de la variable "Sexe"
table(clients$sexe)
summary(clients$sexe)
clients$sexe[clients$sexe == "?" | clients$sexe == " " | clients$sexe == "N/D"] = NA
which(is.na(clients$sexe))
sum(is.na(clients$sexe))
clients = clients %>% drop_na()

clients$sexe[clients$sexe == "FÃ©minin" | clients$sexe == "Femme"] = "F"
clients$sexe[clients$sexe == "Masculin" | clients$sexe == "Homme"] = "M"

#Nettoyage de la variable "SituationFamiliale"
table(clients$situationFamiliale)
summary(clients$situationFamiliale)
clients$situationFamiliale[clients$situationFamiliale == "?" | clients$situationFamiliale == " " | clients$situationFamiliale == "N/D"] = NA
which(is.na(clients$situationFamiliale))
sum(is.na(clients$situationFamiliale))
clients = clients %>% drop_na()

clients$situationFamiliale[clients$situationFamiliale == "CÃ©libataire" | clients$situationFamiliale == "Seul" | clients$situationFamiliale == "Seule"] = "Single"

clients$situationFamiliale = as.factor(clients$situationFamiliale)
levels(clients$situationFamiliale) = c("Divorced","Couple","Maried","Single")

#Nettoyage sur la variable "NbEnfantsAcharge"
table(clients$nbEnfantsAcharge)
summary(clients$nbEnfantsAcharge)
clients$nbEnfantsAcharge[clients$nbEnfantsAcharge == "?" | clients$nbEnfantsAcharge == " " | clients$nbEnfantsAcharge == "-1"] = NA
which(is.na(clients$nbEnfantsAcharge))
sum(is.na(clients$nbEnfantsAcharge))
clients = clients %>% drop_na()

#Nettoyage sur la variable "taux"
table(clients$taux)
summary(clients$taux)
clients$taux[clients$taux == "?" | clients$taux == " " | clients$taux == "-1"] = NA
which(is.na(clients$taux))
sum(is.na(clients$taux))
clients = clients %>% drop_na()

#Nettoyage sur la variable "X2eme.voiture"
table(clients$X2eme.voiture)
summary(clients$X2eme.voiture)
clients$X2eme.voiture[clients$X2eme.voiture == "?" | clients$X2eme.voiture == " "] = NA
which(is.na(clients$X2eme.voiture))
sum(is.na(clients$X2eme.voiture))
clients = clients %>% drop_na()

sum(is.na(clients))
#196669 individus

##Lier la colonne des categories sur le fichier client
#Lier la colonne category au fichier client par l'immatriculation
data <- clients %>%
  left_join(immat, by = "immatriculation") %>%
  select(c(-puissance, -longueur, -nbPlaces, -nbPortes, -couleur, -occasion, -prix))

sum(is.na(data))
str(data)
#197352 individus

#Voir indices des dupliques
which(duplicated(data))

##Train la prediction sur le fichier client
data = subset(data, select = c(-immatriculation, -marque, -nom))

#2/3 pour train and 1/3 pour test
sizeData = nrow(data)
sizeTrain = round(sizeData * (2/3), digits = 0)
dataTrain = data[1:sizeTrain,]
dataVal = data[(sizeTrain+1):sizeData,]

table(dataTrain$prediction)

#Constitution des classifieurs
#Utilisation de C50
#On explique cluster en fonction des variables explicatives (nbEnfantsAcharge, situationFamiliale, X2eme.voiture, taux)
modelClient = C5.0(prediction~nbEnfantsAcharge + situationFamiliale + X2eme.voiture + taux, dataTrain)

#Modeliser l'arbre
plot(modelClient, type="simple")

#Prediction
predictModelClient = predict(modelClient, dataVal, type="class")
print(predictModelClient)
table(predictModelClient)

#Ajout de la variable dans le dataframe
dataVal$predictCategoryClient = predictModelClient
#Afficher le taleau pour le dataframe dataVal[toutes les lignes, colonnes choisies]
View(dataVal[,c("prediction", "predictCategoryClient")])

#Calcul du taux de succes
successClientPrediction = nrow(dataVal[dataVal$prediction == dataVal$predictCategoryClient,]) / nrow(dataVal)*100
#Le taux de succes est a 72.5%.

#Donner un nom aux nouvelles predictions 
dataVal$categoryNamePredict <- ifelse(dataVal$predictCategoryClient == 1, 'longue5po5pl',
                                  ifelse(dataVal$predictCategoryClient == 2, 'tresLongue5pl5po',
                                    ifelse(dataVal$predictCategoryClient == 3, 'moyenne5pl5po',
                                           ifelse(dataVal$predictCategoryClient == 4, 'courte5pl5po',
                                                  ifelse(dataVal$predictCategoryClient== 5,'courte5pl3po','longue7pl5po')))))
View(dataVal)


#Grouper les categories afin de mieux comparer la prediction
dataGrouped = dataVal %>% group_by(prediction) %>% summarise(age = age, sexe=sexe, taux=taux, situationFamiliale = situationFamiliale, nbEnfantsAcharge=nbEnfantsAcharge, X2eme.voiture=X2eme.voiture, categoryName=categoryName, predictCategoryClient=predictCategoryClient, categoryNamePredict=categoryNamePredict);dataGrouped
View(dataGrouped)
