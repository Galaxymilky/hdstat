package com.hdstat.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class HiveUtil {

    /**
     * @param @return 设定文件
     * @return Connection    返回类型
     * @throws
     * @Title: getCon
     * @Description: TODO(获取HIVE JDBC 连接)
     */
    public static Connection getCon() {

        try {
            Class.forName("org.apache.hive.jdbc.HiveDriver");
            Connection conn = DriverManager.getConnection(
                    "jdbc:hive2://127.0.0.1:10000/default", "hive", "");
            System.out.println("连接hive");
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * @param @param  database
     * @param @return 设定文件
     * @return Connection    返回类型
     * @throws
     * @Title: getCon
     * @Description: TODO(获取HIVE 指定数据库 JDBC 连接)
     */
    public static Connection getCon(String database, String user, String password) {

        try {
            Class.forName("org.apache.hive.jdbc.HiveDriver");
            Connection conn = DriverManager.getConnection(
                    "jdbc:hive2://127.0.0.1:10000/" + database, user, password);
            System.out.println("连接hive");
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * @param @param stmt
     * @param @param conn 设定文件
     * @return void    返回类型
     * @throws
     * @Title: close
     * @Description: TODO(关闭HIVE JDBC 连接)
     */
    public static void close(Statement stmt, Connection conn) {
        try {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    /**
     * @param @param conn 设定文件
     * @return void    返回类型
     * @throws
     * @Title: close
     * @Description: TODO(关闭HIVE JDBC 连接)
     */
    public static void close(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * @param @param  str
     * @param @return 设定文件
     * @return byte[]    返回类型
     * @throws
     * @Title: hex2byte
     * @Description: 字符串转二进制
     */

    public static byte[] hex2byte(String str) { // 字符串转二进制
        if (str == null)
            return null;
        str = str.trim();
        int len = str.length();
        if (len == 0 || len % 2 == 1)
            return null;
        byte[] b = new byte[len / 2];
        try {
            for (int i = 0; i < str.length(); i += 2) {
                b[i / 2] = (byte) Integer.decode("0X" + str.substring(i, i + 2)).intValue();
            }
            return b;
        } catch (Exception e) {
            return null;
        }
    }
}