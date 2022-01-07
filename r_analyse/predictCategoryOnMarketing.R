library("rstudioapi")
library(questionr)
library(dplyr)
library("C50")
library("tidyr")

##Nettoyage du fichier de donnees
#Chargement des donnees
#Utiliser le fichier marketing.csv
marketing = read.csv(file.choose(), header=T, sep=",", dec='.')
str(marketing)

marketing$nbEnfantsAcharge = as.character(marketing$nbEnfantsAcharge)
marketing$taux = as.character(marketing$taux)

#Nettoyage de la variable "SituationFamiliale"
table(marketing$situationFamiliale)
marketing$situationFamiliale = as.factor(marketing$situationFamiliale)
levels(marketing$situationFamiliale) = c("Single","Couple")

##Prediction des categories de voiture sur le fichier marketing
#Application des classifieurs faits dans le fichier client
predictionMarketing = predict(modelClient, marketing, type="class")
table(predictionMarketing)
print(predictionMarketing)

#Ajout de la colonne dans le fichier marketing
marketing$carCategory = predictionMarketing
View(marketing)

#Ajout du nom des categories
# marketing$carCategoryName <- ifelse(marketing$carCategory == 1, 'longue5po5pl',
#                                 ifelse(marketing$carCategory == 2, 'tresLongue5pl5po',
#                                     ifelse(marketing$carCategory == 3, 'moyenne5pl5po',
#                                            ifelse(marketing$carCategory == 4, 'courte5pl5po',
#                                                   ifelse(marketing$carCategory== 5,'courte5pl3po','longue7pl5po')))))
# View(marketing)

#Grouper par categorie
marketingGrouped = marketing %>% group_by(carCategory) %>% summarise(age = age, sexe=sexe, taux=taux, situationFamiliale = situationFamiliale, nbEnfantsAcharge=nbEnfantsAcharge, X2eme.voiture=X2eme.voiture);marketingGrouped
View(marketingGrouped)
#On n'a que 4 categories car dans le fichier clients et lors de la prediction, les 7 places n'existent pas et
#il n'y a pas eu de prediction sur les voitures 3 portes. La prediction a l'air globalement bonne.

#Export du fichier marketing qui comprend les categories en plus
write.csv(marketing, "marketingCategory.csv", row.names=F)
