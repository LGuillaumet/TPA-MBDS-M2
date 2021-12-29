library("rstudioapi")
library(cluster)
library(dplyr)
library(dbscan)
library(questionr)
library(ggplot2)

setwd(dirname(getActiveDocumentContext()$path))
getwd()


##Clustering pour trouver les categories
#Clustering base sur la densite dbscan
#Chargement des donnees

immat = read.csv("Immatriculations.csv", header=T, sep=",", dec='.', stringsAsFactors = T)
catag = read.csv("Catalogue.csv", header=T, sep=",", dec='.', stringsAsFactors = T)
#Suppression des colonnes inutiles
immat=subset(immat,select= c(-immatriculation,-nom, -couleur, -occasion, -prix, -marque))
catag=subset(catag,select= c(-nom, -couleur, -occasion, -prix, -marque))

#Fusion des 2 jeux de donnees afin d'avoir un jeu de donnees complet
data = rbind(immat, catag)
str(data)

#Distinct pour enlever les lignes en doublon, fichier trop gros pour daisy
data = data %>% distinct(.keep_all=TRUE)

#Matrice de distance
matrix = daisy(data)

#Clustering avec dbscan
dbs=dbscan(matrix, eps=0.125, 3)
cluster=dbs$cluster
describe(cluster)
#6 clusters

#Boucle for afin d'evaluer les clusters les plus pertinents
for(i in 3:8){
  print(i)
  dbsTest = dbscan(matrix, eps=0.125, minPts = i)
  clusterTest = dbsTest$cluster
  print(table(clusterTest))
}
#minPts = 4 et minPts = 3 interessants, voir pour minPts = 5

#MinPts = 4
dbs4=dbscan(matrix, eps=0.125, 4)
cluster4=dbs4$cluster
describe(cluster4)

#Ajout de la colonne cluster dans le dataframe
data$cluster = cluster

data$clusterMinPts4 = cluster4

#Grouper par cluster
dataGrouped = data %>% group_by(cluster) %>% summarise(puissance = puissance, longueur=longueur, nbPlaces=nbPlaces, nbPortes=nbPortes, categoryName=categoryName);dataGrouped
View(dataGrouped)

#Choix entre minPts=3 ou minPts=4
#Minpts=3, 6 clusters qui differencient les courtes avec 5places/5portes et 3places/5portes
#Minpts=4, 5 clusters qui ne differencient pas les courtes avec 5places/5portes et 3places/5portes

#Creer une variable qui porte le nom des clusters
data$categoryName <- ifelse(data$cluster == 1, 'longue5po5pl',
                                 ifelse(data$cluster == 2, 'tresLongue5pl5po',
                                        ifelse(data$cluster == 3, 'moyenne5pl5po',
                                               ifelse(data$cluster == 4, 'courte5pl5po',
                                                      ifelse(data$cluster== 5,'courte5pl3po','longue7pl5po')))))
View(data)