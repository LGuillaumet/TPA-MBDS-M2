install.packages("rJava")
install.packages("DBI")
install.packages("RJDBC")

library(rJava)
library(RJDBC)
library("rstudioapi")

setwd(dirname(getActiveDocumentContext()$path))
getwd()

new_presto_driver <- JDBC(classPath = "H:/Documents/Scolaire/TPA-MBDS-M2/libs/presto-jdbc-0.266.1.jar")

presto_jdbc <- dbConnect(
  new_presto_driver,
  user = "user",
  url = "jdbc:presto://localhost:8080",
  SSL = TRUE
)

customer <- dbGetQuery(presto_jdbc, "select * from (select distinct puissance, longueur, nbPortes, nbPlaces, prix from cassandra.datalake.registration) union distinct (select distinct puissance, longueur, nbPortes, nbPlaces, prix from hive.datalake.catalogue)")
print(customer)

dbDisconnect(presto_jdbc)

# coonect hdfs hadoop ? 
#https://github.com/RevolutionAnalytics/RHadoop/wiki/user%3Erhdfs%3EHome
