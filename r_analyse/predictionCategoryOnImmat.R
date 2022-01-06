library("rstudioapi")

# setwd(dirname(getActiveDocumentContext()$path))
# getwd()

##Previsions pour prevoir les categories sur les immatriculations
#Chargement des donnees
#Utiliser le fichier "immatriculation.csv"
immat = read.csv(file.choose(), header=T, sep=",", dec='.', stringsAsFactors = T)

#Application des classifieurs et de la prediction sur le fichier immat
#Ici, on reprend la variable model qui a ete definie dans le fichier trainPredictCategory.
#C'est notre modele de prediction.
predictionImmat = predict(model, immat, type="class")
table(predictionImmat)
print(predictionImmat)

#Ajout de la colonne dans le fichier immat
immat$prediction = predictionImmat
View(immat)

#Ajout du nom des categories
# immat$categoryName <- ifelse(immat$prediction == 1, 'longue5po5pl',
#                             ifelse(immat$prediction == 2, 'tresLongue5pl5po',
#                                    ifelse(immat$prediction == 3, 'moyenne5pl5po',
#                                           ifelse(immat$prediction == 4, 'courte5pl5po',
#                                                  ifelse(immat$prediction== 5,'courte5pl3po','longue7pl5po')))))
# View(immat)

#Grouper par cluster
immatGrouped = immat %>% group_by(prediction) %>% summarise(immatriculation = immatriculation, marque=marque, nom=nom, puissance = puissance, longueur=longueur, nbPlaces=nbPlaces, nbPortes=nbPortes, couleur=couleur, occasion=occasion, prix=prix);immatGrouped
View(immatGrouped)
#On n'a que 5 categories car dans le fichier immatriculation, les 7 places n'existent pas. La prediction a l'air globalement bonne.

#Export du fichier immat qui comprend les categories en plus
write.csv(immat, "immatCategory.csv", row.names=F)