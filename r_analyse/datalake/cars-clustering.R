install.packages("rJava")
install.packages("DBI")
install.packages("RJDBC")

install.packages("cluster")
install.packages("dplyr")
install.packages("dbscan")
install.packages("questionr")
install.packages("ggplot2")
install.packages('RPostgres')
install.packages('rhdfs')

library(rJava)
library(RJDBC)
library("rstudioapi")

library(cluster)
library(dplyr)
library(dbscan)
library(questionr)
library(ggplot2)
library('RPostgres')
library(rhdfs)

setwd(dirname(getActiveDocumentContext()$path))
wd <- getwd()

pathPresto <- file.path(wd,"presto-jdbc-0.266.1.jar")
print(pathPresto)

new_presto_driver <- JDBC(classPath = pathPresto)

presto_jdbc <- dbConnect(
  new_presto_driver,
  user = "user",
  url = "jdbc:presto://localhost:8080",
  SSL = TRUE
)

data <- dbGetQuery(presto_jdbc, "select puissance, longueur, nbPortes as nbportes, nbPlaces as nbplaces from (select distinct puissance, longueur, nbPortes, nbPlaces from cassandra.datalake.registration) union distinct (select distinct puissance, longueur, nbPortes, nbPlaces from hive.datalake.catalogue)")
print(data)
str(data)

# Transformation

data$longueur[data$longueur == "très longue"] = "VeryLong"
data$longueur[data$longueur == "longue"] = "Long"
data$longueur[data$longueur == "courte"] = "Short"
data$longueur[data$longueur == "moyenne"] = "Medium"
data$longueur = as.factor(data$longueur)

#Matrice de distance
matrix = daisy(data)

#Clustering avec dbscan
dbs = dbscan(matrix, eps = 0.125, 3)
cluster = dbs$cluster
describe(cluster)

#Ajout de la colonne dans le dataframe
data$idcategorietype = cluster
View(data)

#Distincte les donnees
dataGrouped = data %>% group_by(idcategorietype) %>% summarise( name = paste(longueur, "nbpo", nbportes, "nbpl", nbplaces, sep="_") );dataGrouped
categoryDistinct <- dataGrouped %>% distinct(.keep_all=TRUE);
dataGrouped
# RENAME COL TO MATCH WITH TABLE DATABASE COLUMN
names(categoryDistinct)[names(categoryDistinct) == "idcategorietype"] <- "id"

print(categoryDistinct)
print(data)

con = dbConnect(RPostgres::Postgres(), user="postgres", password="postgres", host="localhost", port=5432, dbname="postgres")

# CLEAN DATABASE
dbExecute(con, "delete from datawarehouse.carscategories")
dbExecute(con, "delete from datawarehouse.typecategories")

# UPDATE DATABASE
dbWriteTable(con, Id(schema = "datawarehouse", table = "typecategories"), value = categoryDistinct, append = TRUE)
dbWriteTable(con, Id(schema = "datawarehouse", table = "carscategories"), value = data, append = TRUE)

dbDisconnect(presto_jdbc)
dbDisconnect(con)


