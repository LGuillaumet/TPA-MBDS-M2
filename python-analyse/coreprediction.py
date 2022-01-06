from pandas.core.frame import DataFrame
from pickle import dump, load
import keras
import pathlib

clientColumns = ["age", "sexe", "situation", "taux", "nbchildren", "havesecondcar", "registrationid"]
clientQueryAttributes = ', '.join(clientColumns)

registrationColumns = ["puissance", "longueur", "nbplaces", "nbportes", "registrationid"]
registrationQueryAttributes = ', '.join(registrationColumns)

categoriesColumns = ["puissance", "longueur", "nbplaces", "nbportes", "idcategorietype"]
categoriesQueryAttributes = ', '.join(categoriesColumns)

joinRegiCatAttributes = [ "puissance", "longueur", "nbplaces", "nbportes" ]

sourceNone = ['N/D', '?', ' ', 'None' ]

sourceSituation = ['En Couple', 'Divorcé', 'Divorcée', 'Célibataire', 'Seul', 'Seule', 'Marié', 'Marié(e)']
finalSituation = ['Couple', 'Divorced', 'Divorced', 'Single', 'Single', 'Single', 'Married', 'Married']

sourceSexe = ['Femme', "Féminin", 'F', 'Homme', 'Masculin', 'M', ]
finalSexe = ['F', 'F', 'F', 'M', 'M', 'M']

sourceLongueur = [ 'très longue', 'longue', 'courte', 'moyenne' ]
finalLongueur = [ 'VeryLong', 'Long', 'Short', 'Medium']

sourceEncodingSituation = [ 'Couple', 'Divorced', 'Single', 'Married' ]
finalEncodingSituation = [ 1, 2, 3, 4 ]

sourceEncodingSexe = [ 'M', 'F' ]
finalEncodingSexe = [ 0, 1 ]

sourceEncodingBoolean = [ False, True ]
finalEncodingBoolean = [ 0, 1 ]

def dropNaClients(dataframe: DataFrame):
    dataframe.drop(dataframe[dataframe['age'] < 0].index, inplace = True) 
    dataframe.drop(dataframe[dataframe['taux'] < 0].index, inplace = True) 
    dataframe.drop(dataframe[dataframe['nbchildren'] < 0].index, inplace = True)
    dataframe['situation'] = dataframe['situation'].replace(sourceNone,None)
    dataframe['sexe'] = dataframe['sexe'].replace(sourceNone,None)
    dataframe = dataframe.dropna()
    return dataframe

def convertTypesClients(dataframe: DataFrame):
    dataframe['sexe'] = dataframe['sexe'].astype(str)
    dataframe['situation'] = dataframe['situation'].astype(str)
    dataframe['havesecondcar'] = dataframe['havesecondcar'].astype(bool)
    return dataframe

def transformClients(dataframe: DataFrame):
    dataframe['situation'] = dataframe['situation'].replace(sourceSituation,finalSituation)
    dataframe['sexe'] = dataframe['sexe'].replace(sourceSexe,finalSexe)
    return dataframe

def transformRegistrations(dataframe: DataFrame):
    dataframe['longueur'] = dataframe['longueur'].replace(sourceLongueur,finalLongueur)
    return dataframe

def prepareData(dataframe: DataFrame):
    dataframe['situation'] = dataframe['situation'].replace(sourceEncodingSituation,finalEncodingSituation)
    dataframe['sexe'] = dataframe['sexe'].replace(sourceEncodingSexe,finalEncodingSexe)
    dataframe['havesecondcar'] = dataframe['havesecondcar'].replace(sourceEncodingBoolean,finalEncodingBoolean)
    return dataframe

def save_model(model, scaler, encoder):
    path = pathlib.Path().resolve().__str__()
    model.save(path + '/prediction_model.h5')
    dump(scaler, open(path +'/scaler.pkl', 'wb'))
    dump(encoder, open(path +'/encoder.pkl', 'wb'))

def load_model():
    return keras.models.load_model(pathlib.Path().resolve().__str__() + '/prediction_model.h5')

def load_scaler():
    return load(open(pathlib.Path().resolve().__str__() + '/scaler.pkl', 'rb'))

def load_encoder():
    return load(open(pathlib.Path().resolve().__str__() + '/encoder.pkl', 'rb'))