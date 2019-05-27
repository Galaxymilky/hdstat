package com.hdstat.douyu.web;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 使用 DataSource 操作 Hive
 */
@RestController
@RequestMapping("/hive")
public class HiveDataSourceController {

    @Autowired
    @Qualifier("hiveDruidDataSource")
    DataSource druidDataSource;

    @GetMapping("/table/init")
    public String initAllTables() {
        StringBuffer sql = new StringBuffer("create table douyu_1 as select ");
        sql.append("json_tuple(json,\"id\",\"room_number\",\"username\",\"kind\",\"online\",\"w_number\",\"coefficient\",\"link\",\"timestamp\" \n");
        sql.append(",\"followers\")as(id,room_number,username,kind,`online`,w_number,coefficient,link,ts,followers) from \n");
        sql.append("douyu_json"); // 作为文本存储

        System.out.println("Running: " + sql);
        String result = "Create table successfully...";
        try {
            // hiveJdbcTemplate.execute(sql.toString());
            Statement statement = druidDataSource.getConnection().createStatement();
            statement.execute(sql.toString());
        } catch (Exception dae) {
            dae.printStackTrace();
            result = "Create table encounter an error: " + dae.getMessage();
        }
        return result;
    }

    /**
     * 列举当前Hive库中的所有数据表
     */
    @GetMapping("/table/list")
    public List<String> listAllTables() throws SQLException {
        List<String> list = new ArrayList<String>();
        // Statement statement = jdbcDataSource.getConnection().createStatement();
        Statement statement = druidDataSource.getConnection().createStatement();
//        Connection conn = HiveUtil.getCon("default", "root", "system");
//        Statement statement = conn.createStatement();
        String sql = "show tables";
        ResultSet res = statement.executeQuery(sql);
        while (res.next()) {
            list.add(res.getString(1));
        }
        return list;
    }

    /**
     * 查询Hive库中的某张数据表字段信息
     */
    @PostMapping("/table/describe")
    public List<String> describeTable(String tableName) throws SQLException {
        List<String> list = new ArrayList<String>();
        // Statement statement = jdbcDataSource.getConnection().createStatement();
        Statement statement = druidDataSource.getConnection().createStatement();
        String sql = "describe " + tableName;
        ResultSet res = statement.executeQuery(sql);
        while (res.next()) {
            list.add(res.getString(1));
        }
        return list;
    }

    /**
     * 查询指定tableName表中的数据
     */
    @PostMapping("/table/select")
    public List<String> selectFromTable(HttpServletRequest request, String tableName) throws SQLException {
        // Statement statement = jdbcDataSource.getConnection().createStatement();
        if (tableName == null) {
            tableName = "douyu_json";
        }
        Statement statement = druidDataSource.getConnection().createStatement();
        String sql = "select * from " + tableName;
        ResultSet res = statement.executeQuery(sql);
        List<String> list = new ArrayList<String>();
        int count = res.getMetaData().getColumnCount();
        String str = null;
        while (res.next()) {
            str = "";
            for (int i = 1; i < count; i++) {
                str += res.getString(i) + " ";
            }
            str += res.getString(count);
            list.add(str);
        }
        return list;
    }


    /**
     * 示例：创建新表
     */
    @PostMapping("/table/create")
    public String createTable() {
        StringBuffer sql = new StringBuffer("CREATE TABLE IF NOT EXISTS ");
        sql.append("user_sample");
        sql.append("(user_num BIGINT, user_name STRING, user_gender STRING, user_age INT)");
        sql.append("ROW FORMAT DELIMITED FIELDS TERMINATED BY ',' LINES TERMINATED BY '\n' "); // 定义分隔符
        sql.append("STORED AS TEXTFILE"); // 作为文本存储

        System.out.println("Running: " + sql);
        String result = "Create table successfully...";
        try {
            // hiveJdbcTemplate.execute(sql.toString());
            Statement statement = druidDataSource.getConnection().createStatement();
            statement.execute(sql.toString());
        } catch (Exception dae) {
            result = "Create table encounter an error: " + dae.getMessage();
        }
        return result;
    }

    /**
     * 示例：将Hive服务器本地文档中的数据加载到Hive表中
     */
    @PostMapping("/table/load")
    public String loadIntoTable() {
        String filepath = "/home/hadoop/user_sample.txt";
        String sql = "load data local inpath '" + filepath + "' into table user_sample";
        String result = "Load data into table successfully...";

        try {
            Statement statement = druidDataSource.getConnection().createStatement();
            // hiveJdbcTemplate.execute(sql);
            statement.execute(sql);
        } catch (Exception dae) {
            result = "Load data into table encounter an error: " + dae.getMessage();
        }
        return result;
    }

    /**
     * 示例：向Hive表中添加数据
     */
    @PostMapping("/table/insert")
    public String insertIntoTable() {
        String sql = "INSERT INTO TABLE  user_sample(user_num,user_name,user_gender,user_age) VALUES(888,'Plum','M',32)";
        String result = "Insert into table successfully...";
        try {
            // hiveJdbcTemplate.execute(sql);
            Statement statement = druidDataSource.getConnection().createStatement();
            statement.execute(sql);
        } catch (Exception dae) {
            result = "Insert into table encounter an error: " + dae.getMessage();
        }
        return result;
    }

    /**
     * 示例：删除表
     */
    @PostMapping("/table/delete")
    public String delete(String tableName) {
        String sql = "DROP TABLE IF EXISTS " + tableName;
        String result = "Drop table successfully...";
        System.out.println("Running: " + sql);
        try {
            Statement statement = druidDataSource.getConnection().createStatement();
            // hiveJdbcTemplate.execute(sql);
            statement.execute(sql);
        } catch (Exception dae) {
            result = "Drop table encounter an error: " + dae.getMessage();
        }
        return result;
    }
}