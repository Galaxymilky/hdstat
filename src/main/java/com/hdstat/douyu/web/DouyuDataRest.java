package com.hdstat.douyu.web;

import com.hdstat.common.BaseRest;
import com.hdstat.douyu.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/douyu")
public class DouyuDataRest extends BaseRest {

    @Autowired
    @Qualifier("hiveDruidDataSource")
    DataSource druidDataSource;

    /**
     * 列举当前Hive库中的所有数据表
     */
    @PostMapping("/count")
    public Map<String, Object> count(HttpServletRequest request) throws SQLException {
        Map<String, Object> result = new HashMap<>();

        HttpSession session = request.getSession();
        Map<String, Object> countMap = (Map<String, Object>) session.getAttribute("countResult");

        if (countMap == null) {
            Statement statement = druidDataSource.getConnection().createStatement();
            StringBuffer sql = new StringBuffer();
            sql.append("select sum(w_number), sum(online), sum(followers), sum(1) from douyu_1");
            System.out.println(sql.toString());

            ResultSet res = statement.executeQuery(sql.toString());
            while (res.next()) {
                result.put("wNumberCount", res.getInt(1));
                result.put("onlineCount", res.getInt(2));
                result.put("followersCount", res.getInt(3));
                result.put("userNameCount", res.getInt(4));
            }
            session.setAttribute("countResult", result);
        } else {
            countMap.put(RESULT_CODE, SUCCESS);
            return countMap;
        }

        result.put(RESULT_CODE, SUCCESS);
        return result;
    }

    /**
     * 列举当前Hive库中的所有数据表
     */
    @PostMapping("/list1")
    public Map<String, Object> list1(HttpServletRequest request, String tableName) throws SQLException {
        tableName = "douyu_1";
        Map<String, Object> result = new HashMap<>();
        List<DouyuDataBaseJson> list;

        HttpSession session = request.getSession();
        list = (List<DouyuDataBaseJson>) session.getAttribute("dataList1");

        if (list == null) {
            list = new ArrayList<>();
            Statement statement = druidDataSource.getConnection().createStatement();
            StringBuffer sql = new StringBuffer();
            sql.append("select * from (select * from douyu_1 order by w_number desc) a limit 3");
            System.out.println(sql.toString());

            ResultSet res = statement.executeQuery(sql.toString());
            while (res.next()) {
                DouyuDataBaseJson json = new DouyuDataBaseJson();

                json.setId(res.getString(1));
                json.setRoomNumber(res.getString(2));
                json.setUserName(res.getString(3));
                json.setKind(res.getString(4));
                json.setOnline(res.getString(5));
                json.setwNumber(res.getString(6));
                json.setCoefficient(res.getString(7));
                json.setLink(res.getString(8));
                json.setTs(res.getString(9));
                json.setFollowers(res.getString(10));

                list.add(json);
            }

            String sql2 = "select * from (select * from douyu_1 order by w_number asc) a limit 3";
            System.out.println(sql2.toString());

            ResultSet res2 = statement.executeQuery(sql2);
            while (res2.next()) {
                DouyuDataBaseJson json = new DouyuDataBaseJson();
                json.setId(res.getString(1));
                json.setRoomNumber(res.getString(2));
                json.setUserName(res.getString(3));
                json.setKind(res.getString(4));
                json.setOnline(res.getString(5));
                json.setwNumber(res.getString(6));
                json.setCoefficient(res.getString(7));
                json.setLink(res.getString(8));
                json.setTs(res.getString(9));
                json.setFollowers(res.getString(10));
                list.add(json);
            }

            session.setAttribute("dataList1", list);

        }
        result.put("dataList1", list);
        result.put(RESULT_CODE, SUCCESS);

        return result;
    }

    /**
     * 列举当前Hive库中的所有数据表
     */
    @PostMapping("/list2")
    public Map<String, Object> list2(HttpServletRequest request, String tableName) throws SQLException {
        tableName = "douyu_2";
        Map<String, Object> result = new HashMap<>();
        List<DouyuData2Json> list = new ArrayList<>();

        Statement statement = druidDataSource.getConnection().createStatement();
        StringBuffer sql = new StringBuffer();
        sql.append("select kind, sum(w_number), sum(online) from douyu_1 group by kind");
        System.out.println(sql.toString());

        ResultSet res = statement.executeQuery(sql.toString());
        while (res.next()) {
            DouyuData2Json json = new DouyuData2Json();
            json.setKind(res.getString(1));
            json.setwNumber(res.getString(2));
            json.setOnline(res.getString(3));
            list.add(json);
        }

        result.put("dataList2", list);

        List<String> list_1 = new ArrayList<>();
        List<String> list_2 = new ArrayList<>();
        List<String> list_3 = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            list_1.add(list.get(i).getKind());
            list_2.add(list.get(i).getOnline());
            list_3.add(list.get(i).getwNumber());
        }

        result.put("dataList2_1", list_1);
        result.put("dataList2_2", list_2);
        result.put("dataList2_3", list_3);

        result.put(RESULT_CODE, SUCCESS);

        return result;
    }

    /**
     * 列举当前Hive库中的所有数据表
     */
    @PostMapping("/list3")
    public Map<String, Object> list3(HttpServletRequest request, String tableName) throws SQLException {
        tableName = "douyu_3";
        Map<String, Object> result = new HashMap<>();
        Statement statement = druidDataSource.getConnection().createStatement();
        StringBuffer sql = new StringBuffer();
        sql.append("select count(*), t1.kind from douyu_1 t1 group by t1.kind");
        System.out.println(sql.toString());
        ResultSet res = statement.executeQuery(sql.toString());
        List<String> list_1 = new ArrayList<>();
        List<String> list_2 = new ArrayList<>();
        while (res.next()) {
            list_1.add(res.getString(1));
            list_2.add(res.getString(2));
        }

        result.put("dataList3_1", list_1);
        result.put("dataList3_2", list_2);
        result.put(RESULT_CODE, SUCCESS);
        return result;
    }

    /**
     * 列举当前Hive库中的所有数据表
     */
    @PostMapping("/list4")
    public Map<String, Object> list4(HttpServletRequest request, String tableName) throws SQLException {
        tableName = "douyu_4";
        Map<String, Object> result = new HashMap<>();
        Statement statement = druidDataSource.getConnection().createStatement();
        StringBuffer sql = new StringBuffer();
        sql.append("select * from (select * from douyu_4 order by tv_total desc) a limit 10");
        System.out.println(sql.toString());
        ResultSet res = statement.executeQuery(sql.toString());

        List<DouyuData4Json> dataList4 = new ArrayList<>();
        int columnCount = res.getMetaData().getColumnCount();

        while (res.next()) {
            DouyuData4Json json = new DouyuData4Json();
            json.setKind(res.getString(1));
            json.setTvTotal(res.getLong(2));
            dataList4.add(json);
        }

        result.put("dataList4", dataList4);
        result.put(RESULT_CODE, SUCCESS);

        return result;
    }

    /**
     * 列举当前Hive库中的所有数据表
     */
    @PostMapping("/list5")
    public Map<String, Object> list5(HttpServletRequest request, String tableName) throws SQLException {
        Map<String, Object> result = new HashMap<>();
        List<DouyuDataJson> dataList5 = null;

        HttpSession session = request.getSession();

        dataList5 = (List<DouyuDataJson>) session.getAttribute("dataList5");
        if (dataList5 == null) {
            dataList5 = new ArrayList<>();

            tableName = "douyu_5";
            Statement statement = druidDataSource.getConnection().createStatement();
            StringBuffer sql = new StringBuffer();
            sql.append("select * from ").append(tableName).append("");
            System.out.println(sql.toString());
            ResultSet res = statement.executeQuery(sql.toString());
            while (res.next()) {
                DouyuDataJson json = new DouyuDataJson();
                json.setId(res.getString(1));
                json.setRoomNumber(res.getString(2));
                json.setUserName(res.getString(3));
                json.setKind(res.getString(4));
                json.setOnline(res.getLong(5));
                json.setwNumber(res.getLong(6));
                json.setCoefficient(res.getDouble(7));
                json.setLink(res.getString(8));
                json.setYear(res.getInt(9));
                json.setMonth(res.getInt(10));
                json.setDay(res.getInt(11));
                json.setHour(res.getInt(12));
                dataList5.add(json);
            }

            session.setAttribute("dataList5", dataList5);
        }

        result.put("dataList5", dataList5);
        result.put(RESULT_CODE, SUCCESS);

        return result;
    }

}
