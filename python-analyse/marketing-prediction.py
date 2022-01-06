from keras.layers.normalization.batch_normalization import BatchNormalization
from pandas.core.frame import DataFrame
from matplotlib import pyplot as plt
import prestodb
import pandas as pd
import numpy as np

pd.options.display.max_rows = 20

def printModelHistory(history): #Affiche les résultats (efficacité, perte etc)
  # list all data in history
  print(history.history.keys())
  # summarize history for accuracy
  plt.plot(history.history['accuracy'])
  plt.plot(history.history['val_accuracy'])
  plt.title('model accuracy')
  plt.ylabel('accuracy')
  plt.xlabel('epoch')
  plt.legend(['train', 'test'], loc='upper left')
  plt.show()
  # summarize history for loss
  plt.plot(history.history['loss'])
  plt.plot(history.history['val_loss'])
  plt.title('model loss')
  plt.ylabel('loss')
  plt.xlabel('epoch')
  plt.legend(['train', 'validation'], loc='upper left')
  plt.show()

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

conn=prestodb.dbapi.connect(
    host='localhost',
    port=8080,
    user='user'
)

cur = conn.cursor()
cur.execute("select "+ clientQueryAttributes +" FROM mongodb.datalake.clients union all select "+ clientQueryAttributes +" from hive.datalake.clients")
rowClients = pd.DataFrame(cur.fetchall(), columns = clientColumns)
cur.execute("select "+ categoriesQueryAttributes +" from postegres.datawarehouse.carscategories")
rowCategories = pd.DataFrame(cur.fetchall(), columns = categoriesColumns)
cur.execute("select "+ registrationQueryAttributes +" from cassandra.datalake.registration")
rowRegistrations = pd.DataFrame(cur.fetchall(), columns = registrationColumns)

rowClients = dropNaClients(rowClients)
#rowClients = convertTypesClients(rowClients)
rowClients = transformClients(rowClients)
rowRegistrations = transformRegistrations(rowRegistrations)

firstJoin = pd.merge(rowRegistrations, rowCategories, on= joinRegiCatAttributes, how = 'inner')
secondJoin = pd.merge(firstJoin, rowClients, on= 'registrationid', how = 'inner')

secondJoin = secondJoin.drop(columns=['puissance', 'longueur', 'nbplaces', 'nbportes', 'registrationid'])

secondJoin = prepareData(secondJoin)

corr_df = secondJoin.corr()
print(corr_df, "\n")


import keras
import tensorflow as tf
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler
from keras.models import Sequential
from keras.layers import Dense, Activation, Dropout, Embedding, GRU, SimpleRNN, Dense
from keras.callbacks import EarlyStopping
from sklearn.preprocessing import MinMaxScaler
from sklearn.preprocessing import LabelEncoder
from keras.utils.np_utils import to_categorical
from tensorflow.keras.optimizers import Adam
from keras.callbacks import LearningRateScheduler
from keras import regularizers

config = tf.compat.v1.ConfigProto( device_count = {'GPU': 1 , 'CPU': 8} ) 
sess = tf.compat.v1.Session(config=config) 
keras.backend.set_session(sess)

value = secondJoin.drop(columns=['age', 'sexe'])
X = value.drop('idcategorietype', axis=1).values
y = value['idcategorietype'].values

X_train, X_test, y_train, y_test = train_test_split(
    X, y, 
    test_size=0.3, random_state=42, shuffle=True
)

X_test, X_val, y_test, y_val = train_test_split(X_test, y_test, test_size=0.5, stratify=y_test)

scaler = StandardScaler()
encoder = LabelEncoder()

X_train_scaled = scaler.fit_transform(X_train)
X_test_scaled = scaler.transform(X_test)
X_val_scaled = scaler.transform(X_val)

encoder.fit(y_train)
#y_train_enc = encoder.transform(y_train)
#y_test_enc = encoder.transform(y_test)
#y_val_enc = encoder.transform(y_val)
y_train_d = to_categorical(encoder.transform(y_train))
y_test_d = to_categorical(encoder.transform(y_test))
y_val_d = to_categorical(encoder.transform(y_val))

nbInput = len(X_train_scaled[0])
nbOutput = len(y_train_d[0])

class CustomDecay:
	def __init__(self, callback):
		self.callback = callback
	def __call__(self, epoch):
		return self.callback(epoch)

def learning_sche(epoch):
  lr = 0.01
  if epoch>20:
    lr = 0.0001
  elif epoch>10:
    lr = 0.001
  return lr

custom1 = CustomDecay(learning_sche)
lr_scheduler = LearningRateScheduler(custom1)
ourCallback = keras.callbacks.EarlyStopping(monitor='val_accuracy', min_delta=0.0001, patience=20)

model1 = Sequential()
model1.add(Dense(8, input_dim=nbInput, activation='relu'))
model1.add(Dense(nbOutput, activation='softmax'))
model1.compile(
    loss='categorical_crossentropy', 
    optimizer=Adam(learning_rate=0.001), 
    metrics=['accuracy']
)

model1.fit(
    X_train_scaled, 
    y_train_d, 
    validation_data=(X_val_scaled, y_val_d), 
    epochs=30, 
    shuffle=True,
    callbacks=[ourCallback,lr_scheduler]
)

predict_x=model1.predict(X_test_scaled)
classes_x=np.argmax(predict_x,axis=1)