library("FSelectorRcpp")
library("FSelector")

##Analyser les bonnes variables predictives
#Transformation des variables en vartiables numeriques
dataChanged = data
str(dataChanged)

dataChanged$nbEnfantsAcharge = as.numeric(dataChanged$nbEnfantsAcharge)
dataChanged$taux = as.numeric(dataChanged$taux)

dataChanged = subset(dataChanged, select = c(-immatriculation, -marque, -nom, -categoryName))

#Choix des variables predictives
information.gain(dataChanged, dataChanged$prediction)
# attributes  importance
# 1                age 0.001611973
# 2               sexe 0.000323427
# 3               taux 0.056426395
# 4 situationFamiliale 0.307179530
# 5   nbEnfantsAcharge 0.403450597
# 6      X2eme.voiture 0.088851625
# 7         prediction 1.469479541
# 8       categoryName 1.469479541
#Les variables pertinentes sont peut-etre : nbEnfantsACharge (0.4) et situation familiale (0.3).

weight = information.gain(prediction ~ ., dataChanged);weight
# attributes  importance
# 1                age 0.001611973
# 2               sexe 0.000323427
# 3               taux 0.056426395
# 4 situationFamiliale 0.307179530
# 5   nbEnfantsAcharge 0.403450597
# 6      X2eme.voiture 0.088851625
# 7       categoryName 1.469479541

# information_gain(prediction ~ ., dataChanged, type = "gainratio")
# # attributes   importance
# # 1                age 0.0032979179
# # 2               sexe 0.0005297622 
# # 3               taux 0.0548230502
# # 4 situationFamiliale 0.4474677226
# # 5   nbEnfantsAcharge 0.3345019472
# # 6      X2eme.voiture 0.2303157913
# # 7       categoryName 1.0000000000
# 
# information_gain(prediction ~ ., dataChanged, type = "symuncert")
# # attributes   importance
# # 1                age 0.0016463285
# # 2               sexe 0.0003109885
# # 3               taux 0.0451641431
# # 4 situationFamiliale 0.2849579719
# # 5   nbEnfantsAcharge 0.3015773409
# # 6      X2eme.voiture 0.0957834097
# # 7       categoryName 1.0000000000

#Choisir les meilleurs attributs selon leur importance
# cutoff.k chooses k best attributes
# cutoff.k.percent chooses best k * 100% of attributes
# cutoff.biggest.diff chooses a subset of attributes which are signiﬁcantly better than other.
subset = cutoff.k(weight, 1)
f = as.simple.formula(subset, "prediction")
print(f)
#prediction ~ nbEnfantsAcharge

subset1 = cutoff.k.percent(weight, 0.75)
f1 = as.simple.formula(subset1, "prediction")
print(f1)
#prediction ~ nbEnfantsAcharge + situationFamiliale + X2eme.voiture + taux

subset2 = cutoff.biggest.diff(weight)
f2 = as.simple.formula(subset2, "prediction")
print(f2)
#prediction ~ nbEnfantsAcharge + situationFamiliale

#Les meilleures variables de prediction sont nbEnfantsAcharge + situationFamiliale + X2eme.voiture + taux
#Les plus importantes sont nbEnfantsAcharge + situationFamiliale
