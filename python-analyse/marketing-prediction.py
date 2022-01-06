import pandas as pd
import prestodb
import pandas as pd
from sqlalchemy import create_engine

markColumns = ["id", "age", "sexe", "situation", "taux", "nbchildren", "havesecondcar"]
markQueryAttributes = ', '.join(markColumns)

##
#   Marketing
##

from coreprediction import *

presto_connection=prestodb.dbapi.connect(host='localhost',port=8080,user='user')

print('Fetch marketing from datalake')

cur = presto_connection.cursor()
cur.execute("select "+ markQueryAttributes +" FROM cassandra.datalake.marketing")
rowMark = pd.DataFrame(cur.fetchall(), columns = markColumns)

print('Transform and clean marketing data')

rowMark = transformClients(rowMark)
data = prepareData(rowMark)

print('Prepare marketing data for prediction')

valuemark = data.drop(columns=['age', 'sexe'])
ids = data['id']
Xmark = valuemark.drop('id', axis=1).values

model=load_model()
scaler_mark = load_scaler()
encoder_mark = load_encoder()

X_predict = scaler_mark.transform(Xmark)
predict_mark=model.predict(X_predict)

result_mark = pd.DataFrame(predict_mark, columns = encoder_mark.classes_)
result = pd.concat([ids, result_mark], axis=1)

print('Result brut')
print(result)

o = pd.DataFrame(columns=['idmarketing', 'idpredictioncategorietype', 'prediction'])
for index, p in enumerate(predict_mark) :
    for id in encoder_mark.classes_:
        o = o.append({'idmarketing': ids[index], 'idpredictioncategorietype': id, 'prediction': p[id-1] }, ignore_index=True)

print('Results')
print(o)

conn_string = 'postgresql://postgres:postgres@localhost/postgres'
db = create_engine(conn_string)
connp = db.connect()

print('Delete precedent result')

connp.execute("TRUNCATE TABLE datawarehouse.marketingtypecarsprediction_ml")

print('Save result in database')

o.to_sql('marketingtypecarsprediction_ml', con=connp, if_exists='append', index=False, schema='datawarehouse')

connp.close()
presto_connection.close()





