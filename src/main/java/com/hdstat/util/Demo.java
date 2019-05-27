package com.hdstat.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Demo {
    public static void main(String[] args) {
        Connection connection = HiveUtil.getCon("default", "hive", "");
        try {
            Statement statement = connection.createStatement();

            System.out.println(statement.execute("show databases;"));
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}
