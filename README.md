### Env

apache-hive-1.2.2

hadoop-2.8.3

### cmd

1.先开启 metastore

hive --service metastore &

2.先开启 hiveserver2

hive --service hiveserver2 &

3.可以通过命令netstat -ntulp |grep 10000 
可以看到结果 
tcp 0 0 0.0.0.0:10000 0.0.0.0:* LISTEN 27799/java
Mac可以通过命令lsof -i:10000 查看结果

lsof -i:9083

lsof -i:10000


### hive db cmd

http://localhost:8080/hive/table/list

http://localhost:8080/hive/table/create


### hive db cmd2

hive> load data inpath "/Users/dynamicniu/IdeaProjects/hdstat/data/DouYu3.json" into table douyu_json;
FAILED: SemanticException Line 1:17 Invalid path '"/Users/dynamicniu/IdeaProjects/hdstat/data/DouYu3.json"': No files matching path hdfs://localhost:9000/Users/dynamicniu/IdeaProjects/hdstat/data/DouYu3.json
hive> 

异常提示需要先将本机文件提交到Hadoop路径下
hdfs dfs -put /Users/dynamicniu/IdeaProjects/hdstat/data/d8.json /user/input/

新建Json表
create table IF NOT EXISTS json_douyin_1 (json string);

CREATE TABLE IF NOT EXISTS json_d8 (json string) STORED AS textfile;

将Json数据导入到Json表中
hive> load data inpath "/user/input/d8.json" into table json_d8;
Loading data to table default.douyu_json
[Warning] could not update stats.
OK
Time taken: 23.747 seconds
hive> 


###

create table t_douyin_8 as select json_tuple(json,"id","name","age","province","sex","type","time") as (id,name,age,province,sex,type,time) from json_d8;

create table douyin_8 as select json_tuple(json,int "id",string "name",int "age",string "province",string "sex",string "type",timestamp "time") as (id,name,age,province,sex,type,time) from json_d2;

create table douyin_8(id int, name string, age int, province string, sex string, type string, time timestamp);

create table json_d5(id int, name string, age int, province string, sex string, type string, time timestamp) STORED AS JSONFILE;

load data inpath "/user/input/d2.json" into table json_d5;

###

create table douyu_1 as select json_tuple(json,"id","room_number","username","kind","online","w_number","coefficient","link","timestamp","followers")as(id,room_number,username,kind,`online`,w_number,coefficient,link,ts,followers) from douyu_json3;




####

start-dfs.sh

start-yarn.sh


dynamicdeMBP:~ dynamicniu$ hive --service metastore &
[1] 1431
dynamicdeMBP:~ dynamicniu$ Starting Hive Metastore Server

dynamicdeMBP:~ dynamicniu$ hive --service hiveserver2 &
[2] 1480

dynamicdeMBP:~ dynamicniu$ lsof -i:9083
COMMAND  PID       USER   FD   TYPE             DEVICE SIZE/OFF NODE NAME
java    1431 dynamicniu  304u  IPv4 0xe46752218bbabcf9      0t0  TCP *:9083 (LISTEN)

dynamicdeMBP:~ dynamicniu$ lsof -i:10000
COMMAND  PID       USER   FD   TYPE             DEVICE SIZE/OFF NODE NAME
java    1480 dynamicniu  319u  IPv4 0xe46752218d764ff9      0t0  TCP localhost:ndmp (LISTEN)




##########


Last login: Thu May 30 00:26:33 on console
dynamicdeMacBook-Pro:~ dynamicniu$ start-all.sh
This script is Deprecated. Instead use start-dfs.sh and start-yarn.sh
19/05/30 00:28:12 WARN util.NativeCodeLoader: Unable to load native-hadoop library for your platform... using builtin-java classes where applicable
Starting namenodes on [localhost]
Password:
localhost: starting namenode, logging to /Users/dynamicniu/hadoop/hadoop-2.7.3/logs/hadoop-dynamicniu-namenode-dynamicdeMacBook-Pro.local.out
Password:
localhost: starting datanode, logging to /Users/dynamicniu/hadoop/hadoop-2.7.3/logs/hadoop-dynamicniu-datanode-dynamicdeMacBook-Pro.local.out
Patech@Starting secondary namenodes [account.jetbrains.com]
Password:
account.jetbrains.com: starting secondarynamenode, logging to /Users/dynamicniu/hadoop/hadoop-2.7.3/logs/hadoop-dynamicniu-secondarynamenode-dynamicdeMacBook-Pro.local.out
19/05/30 00:28:43 WARN util.NativeCodeLoader: Unable to load native-hadoop library for your platform... using builtin-java classes where applicable
starting yarn daemons
starting resourcemanager, logging to /Users/dynamicniu/hadoop/hadoop-2.7.3/logs/yarn-dynamicniu-resourcemanager-dynamicdeMacBook-Pro.local.out
Password:
localhost: starting nodemanager, logging to /Users/dynamicniu/hadoop/hadoop-2.7.3/logs/yarn-dynamicniu-nodemanager-dynamicdeMacBook-Pro.local.out
dynamicdeMacBook-Pro:~ dynamicniu$ lsof -i:9083;
dynamicdeMacBook-Pro:~ dynamicniu$ lsof -i:10000;
dynamicdeMacBook-Pro:~ dynamicniu$ hive --service metastore &
[1] 1117
dynamicdeMacBook-Pro:~ dynamicniu$ Starting Hive Metastore Server

dynamicdeMacBook-Pro:~ dynamicniu$ hive --service hiveserver2 &
[2] 1165
dynamicdeMacBook-Pro:~ dynamicniu$ 
dynamicdeMacBook-Pro:~ dynamicniu$ 
dynamicdeMacBook-Pro:~ dynamicniu$ lsof -i:9083;
COMMAND  PID       USER   FD   TYPE             DEVICE SIZE/OFF NODE NAME
java    1117 dynamicniu  304u  IPv4 0xb40e61b5aa504d3b      0t0  TCP *:9083 (LISTEN)
dynamicdeMacBook-Pro:~ dynamicniu$ lsof -i:10000;
dynamicdeMacBook-Pro:~ dynamicniu$ lsof -i:10000;
COMMAND  PID       USER   FD   TYPE             DEVICE SIZE/OFF NODE NAME
java    1165 dynamicniu  318u  IPv4 0xb40e61b5ab6136bb      0t0  TCP localhost:ndmp (LISTEN)
dynamicdeMacBook-Pro:~ dynamicniu$ OK
Query ID = dynamicniu_20190530003141_da495971-ff73-4eed-93f1-ec6e74359e99
Total jobs = 1
Launching Job 1 out of 1
Number of reduce tasks not specified. Estimated from input data size: 1
In order to change the average load for a reducer (in bytes):
  set hive.exec.reducers.bytes.per.reducer=<number>
In order to limit the maximum number of reducers:
  set hive.exec.reducers.max=<number>
In order to set a constant number of reducers:
  set mapreduce.job.reduces=<number>
Starting Job = job_1559147325507_0001, Tracking URL = http://dynamicdeMacBook-Pro.local:8088/proxy/application_1559147325507_0001/
Kill Command = /Users/dynamicniu/hadoop/hadoop-2.7.3/bin/hadoop job  -kill job_1559147325507_0001
dynamicdeMacBook-Pro:~ dynamicniu$ Hadoop job information for Stage-1: number of mappers: 1; number of reducers: 1
2019-05-30 00:31:55,829 Stage-1 map = 0%,  reduce = 0%
dynamicdeMacBook-Pro:~ dynamicniu$ 2019-05-30 00:32:03,425 Stage-1 map = 100%,  reduce = 0%
2019-05-30 00:32:11,196 Stage-1 map = 100%,  reduce = 100%
Ended Job = job_1559147325507_0001
MapReduce Jobs Launched: 
Stage-Stage-1: Map: 1  Reduce: 1   HDFS Read: 7670 HDFS Write: 18 SUCCESS
Total MapReduce CPU Time Spent: 0 msec
OK

