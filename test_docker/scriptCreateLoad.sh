#!/bin/bash

cd ../employee/
hive -f employee_table.hql
hadoop fs -put employee.csv hdfs://namenode:8020/user/hive/warehouse/testdb.db/employee