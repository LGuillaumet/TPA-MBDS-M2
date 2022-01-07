install.packages("rJava")
install.packages("DBI")
install.packages("RJDBC")

library(rJava)
library(RJDBC)
library("rstudioapi")

setwd(dirname(getActiveDocumentContext()$path))
getwd()

##
#   Possible to use R with Spark on cluster ?
#
# http://eric.univ-lyon2.fr/~ricco/tanagra/fichiers/fr_Tanagra_Spark_with_R.pdf
# https://spark.apache.org/docs/latest/sparkr.html
#
##

##
#   CREATE PACKAGE R
#
# https://thinkr.fr/creer-package-r-quelques-minutes/
# https://tinyheero.github.io/jekyll/update/2015/07/26/making-your-first-R-package.html
#
##

##
#   SETUP PRESTODB CONNECTOR
#   /!\ Todo change path of presto jar get getwd()
##
new_presto_driver <- JDBC(classPath = "H:/Documents/Scolaire/TPA-MBDS-M2/libs/presto-jdbc-0.266.1.jar")

presto_jdbc <- dbConnect(
  new_presto_driver,
  user = "user",
  url = "jdbc:presto://localhost:8080",
  SSL = TRUE
)

##
#   Example query to presto to obtain dataframe result
##

union <- dbGetQuery(presto_jdbc, "select * from (select distinct puissance, longueur, nbPortes, nbPlaces, prix from cassandra.datalake.registration) union distinct (select distinct puissance, longueur, nbPortes, nbPlaces, prix from hive.datalake.catalogue)")
print(union)

count <- dbGetQuery(presto_jdbc, "select registrationid from cassandra.datalake.registration")
print(count)

clientConcat <- dbGetQuery(presto_jdbc, "select age, sexe, taux, situation, nbChildren, havesecondcar, immatriculation 
                                          from hive.datalake.clients union distinct select age, sexe, taux, situation, nbChildren, havesecondcar, registrationId from mongodb.datalake.clients")

desc <- dbGetQuery(presto_jdbc, "describe hive.datalake.clients")
desc <- dbGetQuery(presto_jdbc, "describe mongodb.datalake.clients")

co2 <- dbGetQuery(presto_jdbc, "select * from mongodb.datalake.carbon")


dbDisconnect(presto_jdbc)

# coonect hdfs hadoop ? 
#https://github.com/RevolutionAnalytics/RHadoop/wiki/user%3Erhdfs%3EHome

# Tables
# cassandra.datalake.marketing
# cassandra.datalake.registration
# hive.datalake.catalogue
# hive.datalake.clients
# mongodb.datalake.carbon
# mongodb.datalake.clients
