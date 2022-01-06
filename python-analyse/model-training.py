import prestodb
import pandas as pd
import keras
import tensorflow as tf
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler
from keras.models import Sequential
from keras.layers import Dense
from sklearn.preprocessing import LabelEncoder
from keras.utils.np_utils import to_categorical
from tensorflow.keras.optimizers import Adam
from keras.callbacks import LearningRateScheduler

##
#   Data preparation
##
from coreprediction import *

presto_connection=prestodb.dbapi.connect(host='localhost',port=8080,user='user')

# Fetch all datas from datalake

print('Fetch all datas from datalake')

cur = presto_connection.cursor()
cur.execute("select "+ clientQueryAttributes +" FROM mongodb.datalake.clients union all select "+ clientQueryAttributes +" from hive.datalake.clients")
rowClients = pd.DataFrame(cur.fetchall(), columns = clientColumns)
cur.execute("select "+ categoriesQueryAttributes +" from postegres.datawarehouse.carscategories")
rowCategories = pd.DataFrame(cur.fetchall(), columns = categoriesColumns)
cur.execute("select "+ registrationQueryAttributes +" from cassandra.datalake.registration")
rowRegistrations = pd.DataFrame(cur.fetchall(), columns = registrationColumns)

# data transformation

print('Transform and clean all datas')

rowClients = dropNaClients(rowClients)
rowClients = transformClients(rowClients)
rowRegistrations = transformRegistrations(rowRegistrations)

# Join datas

firstJoin = pd.merge(rowRegistrations, rowCategories, on= joinRegiCatAttributes, how = 'inner')
secondJoin = pd.merge(firstJoin, rowClients, on= 'registrationid', how = 'inner')
secondJoin = secondJoin.drop(columns=['puissance', 'longueur', 'nbplaces', 'nbportes', 'registrationid'])

# Prepare for fit

print('Encoding datas')

secondJoin = prepareData(secondJoin)

##
#   Neuronal network model
##

# GPU process

config = tf.compat.v1.ConfigProto( device_count = {'GPU': 1 , 'CPU': 8} ) 
sess = tf.compat.v1.Session(config=config) 
keras.backend.set_session(sess)

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

# Correlation

corr_df = secondJoin.corr()
print(corr_df, "\n")

print('Prepare X_train, X_test, X_val')

value = secondJoin.drop(columns=['age', 'sexe'])
X = value.drop('idcategorietype', axis=1).values
y = value['idcategorietype'].values

X_train, X_test, y_train, y_test = train_test_split(
    X, y, 
    test_size=0.2, random_state=42, shuffle=True
)

X_test, X_val, y_test, y_val = train_test_split(X_test, y_test, test_size=0.5, stratify=y_test)

scaler = StandardScaler()
encoder = LabelEncoder()

X_train_scaled = scaler.fit_transform(X_train)
X_test_scaled = scaler.transform(X_test)
X_val_scaled = scaler.transform(X_val)

encoder.fit(y_train)
y_train_d = to_categorical(encoder.transform(y_train))
y_test_d = to_categorical(encoder.transform(y_test))
y_val_d = to_categorical(encoder.transform(y_val))

nbInput = len(X_train_scaled[0])
nbOutput = len(y_train_d[0])

custom1 = CustomDecay(learning_sche)
lr_scheduler = LearningRateScheduler(custom1)
ourCallback = keras.callbacks.EarlyStopping(monitor='val_accuracy', min_delta=0.0001, patience=20)

print('Create network and fit')

model = Sequential()
model.add(Dense(8, input_dim=nbInput, activation='relu'))
model.add(Dense(nbOutput, activation='sigmoid'))
model.compile(
    loss='categorical_crossentropy', 
    optimizer=Adam(learning_rate=0.01), 
    metrics=['accuracy']
)

model.fit(
    X_train_scaled, 
    y_train_d, 
    validation_data=(X_val_scaled, y_val_d), 
    epochs=100, 
    shuffle=True,
    batch_size=32,
    callbacks=[ourCallback,lr_scheduler]
)

loss, acc = model.evaluate(X_test_scaled, y_test_d)
print("Model evaluate, accuracy: {:5.2f}%".format(100*acc))

##
#   Save model
##

print('Save model, scaler and encoder')

save_model(model, scaler, encoder)

presto_connection.close()

#predict_x=model.predict(X_test_scaled)
#classes_x=np.argmax(predict_x,axis=1)
#encoder.inverse_transform(classes_x)
#result = pd.DataFrame(predict_x, columns = encoder.classes_)