library("rstudioapi")
library(questionr)
library(dplyr)
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

#Nettoyage de la variable "Situation"
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
clients$nbEnfantsAcharge[clients$nbEnfantsAcharge == "?" | clients$sexe == " " | clients$sexe == "-1"] = NA
which(is.na(clients$nbEnfantsAcharge))
sum(is.na(clients$nbEnfantsAcharge))
clients = clients %>% drop_na()

sum(is.na(clients))
#198033 individus

##Lier la colonne des categories sur le fichier client
#Lier la colonne category au fichier client par l'immatriculation
data <- clients %>%
  left_join(immat, by = "immatriculation") %>%
  select(c(-puissance, -longueur, -nbPlaces, -nbPortes, -couleur, -occasion, -prix))
#198721 individus

#Voir indices des dupliques
which(duplicated(data))

##Train la prediction sur le fichier client








