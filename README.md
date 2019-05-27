### Env

apache-hive-1.2.2

hadoop-2.8.3



### cmd

一，先开启 metastore

hive --service metastore &

二，先开启 hiveserver2

hive --service hiveserver2 &

三，可以通过命令netstat -ntulp |grep 10000 
可以看到结果 
tcp 0 0 0.0.0.0:10000 0.0.0.0:* LISTEN 27799/java

四、可以通过命令lsof -i:10000 查看结果

lsof -i:10000


### hive db cmd

http://localhost:8080/hive/table/list

http://localhost:8080/hive/table/create


### hive db cmd2

hive> load data inpath "/Users/dynamicniu/IdeaProjects/hdstat/data/DouYu3.json" into table douyu_json;
FAILED: SemanticException Line 1:17 Invalid path '"/Users/dynamicniu/IdeaProjects/hdstat/data/DouYu3.json"': No files matching path hdfs://localhost:9000/Users/dynamicniu/IdeaProjects/hdstat/data/DouYu3.json
hive> 


$> hdfs dfs -put /Users/dynamicniu/IdeaProjects/hdstat/data/DouYu3.json /user/input/


hive> load data inpath "/user/input/DouYu3.json" into table douyu_json;
Loading data to table default.douyu_json
[Warning] could not update stats.
OK
Time taken: 23.747 seconds
hive> 


###

create table douyu_1 as select json_tuple(json,"id","room_number","username","kind","online","w_number","coefficient","link","timestamp","followers")as(id,room_number,username,kind,`online`,w_number,coefficient,link,ts,followers) from douyu_json3;



dynamicdeMBP:~ dynamicniu$ hive --service metastore &
[1] 1431
dynamicdeMBP:~ dynamicniu$ Starting Hive Metastore Server

dynamicdeMBP:~ dynamicniu$ 
dynamicdeMBP:~ dynamicniu$ 
dynamicdeMBP:~ dynamicniu$ hive --service hiveserver2 &
[2] 1480
dynamicdeMBP:~ dynamicniu$ lsof -i:9083
COMMAND  PID       USER   FD   TYPE             DEVICE SIZE/OFF NODE NAME
java    1431 dynamicniu  304u  IPv4 0xe46752218bbabcf9      0t0  TCP *:9083 (LISTEN)
dynamicdeMBP:~ dynamicniu$ lsof -i:10000
COMMAND  PID       USER   FD   TYPE             DEVICE SIZE/OFF NODE NAME
java    1480 dynamicniu  319u  IPv4 0xe46752218d764ff9      0t0  TCP localhost:ndmp (LISTEN)
dynamicdeMBP:~ dynamicniu$ 
