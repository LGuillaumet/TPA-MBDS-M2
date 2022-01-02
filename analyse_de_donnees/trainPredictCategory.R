library("rstudioapi")
library("C50")
library("tree")
library("rpart")

setwd(dirname(getActiveDocumentContext()$path))
getwd()

##Test de prediction
#Chargement des donnees
category = read.csv("cluster.csv", header=T, sep=",", dec='.', stringsAsFactors = T)
category$cluster = as.factor(category$cluster)

table(category$cluster)

#2/3 pour train and 1/3 pour test
sizeCategory = nrow(category)
sizeTrain = round(sizeCategory * (2/3), digits = 0)
categoryTrain = category[1:sizeTrain,]
categoryVal = category[(sizeTrain+1):sizeCategory,]

table(categoryTrain$cluster)

#Constitution des classifieurs
#Utilisation de C50
#On explique cluster en fonction des variables explicatives (puissance, longueur, nbPlaces, nbPortes)
model = C5.0(cluster~puissance+longueur+nbPlaces+nbPortes, categoryTrain)


#Modeliser l'arbre
plot(model, type="simple")
#L'arbre n'a aucun sens


#Prediction
predictModel = predict(model, categoryVal, type="class")
print(predictModel)
table(predictModel)

#Ajout de la variable dans le dataframe
categoryVal$prediction = predictModel
#Afficher le taleau pour le dataframe categoryVal[toutes les lignes, colonnes choisies]
View(categoryVal[,c("cluster", "prediction")])

#Calcul du taux de succes
success = nrow(categoryVal[categoryVal$cluster == categoryVal$prediction,]) / nrow(categoryVal)*100
#Le taux de succes est a 50% mais la prediction reste bonne.