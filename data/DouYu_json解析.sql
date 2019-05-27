/*   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id序号',
  `room_number` char(50) NOT NULL COMMENT '直播间号',
  `username` char(200) NOT NULL COMMENT '主播名称',
  `kind` char(200) NOT NULL COMMENT '主播类型',
  `online` int(11) DEFAULT NULL,                          --观看人数
  `w_number` int(11) DEFAULT NULL COMMENT '观看人数',     --热度
  `coefficient` float DEFAULT NULL COMMENT '影响因数',
  `link` char(200) DEFAULT NULL COMMENT '直播间链接',
  `timestamp` timestamp NULL DEFAULT NULL COMMENT '爬取时间',
  `followers` int(11) DEFAULT NULL COMMENT '关注人数', */
  
nohup hiveserver2 >/dev/null 2>1 &

beeline -u jdbc:hive2://mini01:10000 -n root

create dabatase douyu 

create table douyu_json (json string);

load data inpath "/DouyuData/DouYu3.json" into table douyu_json3;

create table  douyu_1 as
select json_tuple(json,"id","room_number","username","kind","online","w_number","coefficient","link","timestamp","followers")as(id,room_number,username,kind,`online`,w_number,coefficient,link,ts,followers) from douyu_json3;



/* create table douyu_test_txt (id int,room_number int,kind string,online string,w_number int,coefficient double,link string,timestamp date,followers string)
row format delimited fields terminated by ' '; */



/* select json_tuple(json,'id','room_number','kind','online','w_number','coefficient','link','timestamp','followers')as(id,room_number,kind,`online`,w_number,coefficient,link,`timestamp`,followers) from json_test; */

--处理json字段 避开关键字
create table  douyu_2 as
select json_tuple(json,
                  "id",
                  "room_number",
                  "username",
                  "kind",
                  "online",
                  "w_number",
                  "coefficient",
                  "link",
                  "timestamp",
                  "followers") as(id, room_number, username, kind, online, w_number, coefficient, link, ts, followers)
from douyu_json3;

--etl一个详细列表 修改字符串类型
create table douyu_3 as
select id,
       room_number,
       username,
       kind,
       cast(online as bigint) as `online`,
       cast(w_number as bigint) as w_number,
       cast(coefficient as float) as coefficient,
       link,
       year(ts) as year,
       month(ts) as month,
       day(ts) as day,
       hour(ts) as hour
  from douyu_2;

--按照游戏分类维度统计  主播数目少于50的 抛弃
create table douyu_4 as

select *
  from (select kind, count(1) as tv_total
          from douyu_3
         group by kind
         order by tv_total) z1
 where z1.tv_total > 50;


--统计出当前的主播数量>=50的 并且当前观看人数在分类前十的主播
create table douyu_5 as

select *
from (select * from douyu_3 where kind = '英雄联盟') t1
order by t1.w_number desc limit 10;





select  t2.*, row_number() over (partition by t2.kind order by t2.online desc) as rank 
from 
douyu_3 t2;

where t1.tv_total >=50 
and t1.room_number=t2.room_number;
order by t2.online


/* select id,room_number,username,
year(timestamp) as year,
month(timestamp) as year,
day(timestamp) as day
from douyu_1 limit 10; */

使用desc formatted table_name 查看列的字段类型
