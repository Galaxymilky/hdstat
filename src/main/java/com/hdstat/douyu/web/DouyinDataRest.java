package com.hdstat.douyu.web;

import com.hdstat.common.BaseRest;
import com.hdstat.douyu.mapper.DouyinBaseListJson;
import com.hdstat.douyu.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
@RequestMapping("/douyin")
public class DouyinDataRest extends BaseRest {

    private static final String tableName = "t_douyin_8";

    private static final String tableName_1 = "douyin_7";
    private static final String tableName_2 = "t_douyin_8";

    @Autowired
    @Qualifier("hiveDruidDataSource")
    DataSource druidDataSource;

    /**
     * 查询出当前表总数
     */
    @PostMapping("/count")
    public Map<String, Object> count(HttpServletRequest request) throws SQLException {
        Map<String, Object> result = new HashMap<>();

        HttpSession session = request.getSession();
        Integer totalCount = (Integer) session.getAttribute("totalCount");

        if (totalCount == null) {
            Statement statement = druidDataSource.getConnection().createStatement();
            StringBuffer sql = new StringBuffer();
            sql.append("select count(*) from ").append(tableName);
            System.out.println(sql.toString());

            ResultSet res = statement.executeQuery(sql.toString());
            while (res.next()) {
                result.put("totalCount", res.getInt(1));
            }
            session.setAttribute("totalCount", result.get("totalCount"));
        } else {
            result.put("totalCount", totalCount);
        }

        result.put(RESULT_CODE, SUCCESS);
        return result;
    }


    /**
     * 查询抖音用户男女比例
     */
    @PostMapping("/list1")
    public Map<String, Object> list1(HttpServletRequest request) throws SQLException {
        Map<String, Object> result = new HashMap<>();
        List<DouyinList1Json> list;

        HttpSession session = request.getSession();
        list = (List<DouyinList1Json>) session.getAttribute("dataList1");

        if (list == null) {
            list = new ArrayList<>();
            Statement statement = druidDataSource.getConnection().createStatement();
            StringBuffer sql = new StringBuffer();
            sql.append("select count(*), sex from ").append(tableName).append(" group by sex");
            System.out.println(sql.toString());

            ResultSet res = statement.executeQuery(sql.toString());
            while (res.next()) {
                DouyinList1Json json = new DouyinList1Json();

                json.setSexSum(res.getInt(1));
                json.setSex(res.getString(2));

                list.add(json);
            }
            session.setAttribute("dataList1", list);
        }

        result.put("dataList1", list);
        result.put(RESULT_CODE, SUCCESS);

        return result;
    }

    /**
     * 查询各省份抖音用户
     */
    @PostMapping("/list2")
    public Map<String, Object> list2(HttpServletRequest request) throws SQLException {
        Map<String, Object> result = new HashMap<>();
        List<BaseEchartJson> list;

        HttpSession session = request.getSession();
        list = (List<BaseEchartJson>) session.getAttribute("dataList2");

        if (list == null) {
            list = new ArrayList<>();
            Statement statement = druidDataSource.getConnection().createStatement();
            StringBuffer sql = new StringBuffer();
            sql.append("select * from (");
            sql.append("select count(*) totalCount, province from ").append(tableName).append(" group by province");
            sql.append(") t order by t.totalCount desc");
            System.out.println(sql.toString());

            ResultSet res = statement.executeQuery(sql.toString());
            while (res.next()) {
                BaseEchartJson json = new BaseEchartJson();

                json.setValue(res.getInt(1));
                json.setName(res.getString(2));

                list.add(json);
            }
            session.setAttribute("dataList2", list);
        }

        result.put("dataList2", list);
        result.put(RESULT_CODE, SUCCESS);

        return result;
    }

    /**
     * 查询抖音用户年龄分布
     */
    @PostMapping("/list3")
    public Map<String, Object> list3(HttpServletRequest request) throws SQLException {
        Map<String, Object> result = new HashMap<>();
        List<DouyinList3Json> list;

        HttpSession session = request.getSession();
        list = (List<DouyinList3Json>) session.getAttribute("dataList3");

        if (list == null) {
            list = new ArrayList<>();
            Statement statement = druidDataSource.getConnection().createStatement();

            String sql = "";
            sql = "select t.* from ";
            sql += "(select count(*) as countNum, type from " + tableName;
            sql += " where cast(age as int) < 30 ";
            sql += " and cast(age as int) > 18 ";
            sql += " group by type) t order by t.countNum desc";
            System.out.println(sql);
            ResultSet res1 = statement.executeQuery(sql);
            int i = 0;
            while (res1.next() && i < 3) {
                i++;
                DouyinList3Json json = new DouyinList3Json();
                json.setTypeSum(res1.getInt(1));
                json.setType(res1.getString(2));
                json.setwAge("18-30");
                list.add(json);
            }
            for (int k = 2; k >= i; k--) {
                DouyinList3Json json = new DouyinList3Json();
                json.setTypeSum(0);
                json.setType("");
                json.setwAge("18-30");
                list.add(json);
            }

            sql = "select t.* from ";
            sql += "(select count(*) as countNum, type from " + tableName;
            sql += " where cast(age as int) < 40 ";
            sql += " and cast(age as int) > 31 ";
            sql += " group by type) t order by t.countNum desc";
            System.out.println(sql);
            ResultSet res2 = statement.executeQuery(sql);
            i = 0;
            while (res2.next() && i < 3) {
                i++;
                DouyinList3Json json = new DouyinList3Json();
                json.setTypeSum(res2.getInt(1));
                json.setType(res2.getString(2));
                json.setwAge("31-40");
                list.add(json);
            }
            for (int k = 2; k >= i; k--) {
                DouyinList3Json json = new DouyinList3Json();
                json.setTypeSum(0);
                json.setType("");
                json.setwAge("31-40");
                list.add(json);
            }

            sql = "select t.* from ";
            sql += "(select count(*) as countNum, type from " + tableName;
            sql += " where cast(age as int) < 50 ";
            sql += " and cast(age as int) > 41 ";
            sql += " group by type) t order by t.countNum desc";
            System.out.println(sql);
            ResultSet res3 = statement.executeQuery(sql);
            i = 0;
            while (res3.next() && i < 3) {
                i++;
                DouyinList3Json json = new DouyinList3Json();
                json.setTypeSum(res3.getInt(1));
                json.setType(res3.getString(2));
                json.setwAge("41-50");
                list.add(json);
            }
            for (int k = 2; k >= i; k--) {
                DouyinList3Json json = new DouyinList3Json();
                json.setTypeSum(0);
                json.setType("");
                json.setwAge("41-50");
                list.add(json);
            }


            sql = "select t.* from ";
            sql += "(select count(*) as countNum, type from " + tableName;
            sql += " where cast(age as int) < 60 ";
            sql += " and cast(age as int) > 51 ";
            sql += " group by type) t order by t.countNum desc";
            System.out.println(sql);
            ResultSet res4 = statement.executeQuery(sql);
             i = 0;
            while (res4.next() && i < 3) {
                i++;
                DouyinList3Json json = new DouyinList3Json();
                json.setTypeSum(res4.getInt(1));
                json.setType(res4.getString(2));
                json.setwAge("50-60");
                list.add(json);
            }
            for (int k = 2; k >= i; k--) {
                DouyinList3Json json = new DouyinList3Json();
                json.setTypeSum(0);
                json.setType("");
                json.setwAge("50-60");
                list.add(json);
            }


            sql = "select t.* from ";
            sql += "(select count(*) as countNum, type from " + tableName;
            sql += " where cast(age as int) > 61 ";
            sql += " group by type) t order by t.countNum desc";
            System.out.println(sql);
            ResultSet res5 = statement.executeQuery(sql);
             i = 0;
            while (res5.next() && i < 3) {
                i++;
                DouyinList3Json json = new DouyinList3Json();
                json.setTypeSum(res5.getInt(1));
                json.setType(res5.getString(2));
                json.setwAge("61以上");
                list.add(json);
            }
            for (int k = 2; k >= i; k--) {
                DouyinList3Json json = new DouyinList3Json();
                json.setTypeSum(0);
                json.setType("");
                json.setwAge("61以上");
                list.add(json);
            }

            session.setAttribute("dataList3", list);
        }

        result.put("dataList3", list);
        result.put(RESULT_CODE, SUCCESS);

        return result;
    }

    @PostMapping("/list4")
    public Map<String, Object> list4(HttpServletRequest request) throws SQLException {
        Map<String, Object> result = new HashMap<>();
        List<DouyinList4Json> list;

        HttpSession session = request.getSession();
        list = (List<DouyinList4Json>) session.getAttribute("dataList4");

        if (list == null) {
            list = new ArrayList<>();
            Statement statement = druidDataSource.getConnection().createStatement();
            String sql = "";
            sql = "select count(1) from " + tableName + " where unix_timestamp(time,'HH24:mm:ss') > unix_timestamp('09:00:00','HH24:mm:ss') and unix_timestamp(time,'HH24:mm:ss') < unix_timestamp('12:00:00','HH24:mm:ss');";
            sql = "select count(1) from " + tableName + " where cast(substr(substr(time, 12, 13),0,2) as int) > 6 and cast(substr(substr(time, 12, 13),0,2) as int) < 9";
            System.out.println(sql);
            ResultSet res1 = statement.executeQuery(sql);
            if (res1.next()) {
                DouyinList4Json json = new DouyinList4Json();
                json.setCountNum(res1.getInt(1));
                json.setwTime("06-09");
                list.add(json);
            } else {
                DouyinList4Json json = new DouyinList4Json();
                json.setCountNum(0);
                json.setwTime("06-09");
                list.add(json);
            }

            sql = "select count(1) from " + tableName + " where unix_timestamp(time,'HH24:mm:ss') > unix_timestamp('09:00:00','HH24:mm:ss') and unix_timestamp(time,'HH24:mm:ss') < unix_timestamp('12:00:00','HH24:mm:ss');";
            sql = "select count(1) from " + tableName + " where cast(substr(substr(time, 12, 13),0,2) as int) > 9 and cast(substr(substr(time, 12, 13),0,2) as int) < 12";
            System.out.println(sql);
            ResultSet res2 = statement.executeQuery(sql);
            if (res2.next()) {
                DouyinList4Json json = new DouyinList4Json();
                json.setCountNum(res2.getInt(1));
                json.setwTime("09-12");
                list.add(json);
            } else {
                DouyinList4Json json = new DouyinList4Json();
                json.setCountNum(0);
                json.setwTime("09-12");
                list.add(json);
            }

            sql = "select count(1) from " + tableName + " where cast(substr(substr(time, 12, 13),0,2) as int) > 12 and cast(substr(substr(time, 12, 13),0,2) as int) < 15";
            System.out.println(sql);
            ResultSet res3 = statement.executeQuery(sql);
            if (res3.next()) {
                DouyinList4Json json = new DouyinList4Json();
                json.setCountNum(res3.getInt(1));
                json.setwTime("12-15");
                list.add(json);
            } else {
                DouyinList4Json json = new DouyinList4Json();
                json.setCountNum(0);
                json.setwTime("12-15");
                list.add(json);
            }

            sql = "select count(1) from " + tableName + " where cast(substr(substr(time, 12, 13),0,2) as int) > 15 and cast(substr(substr(time, 12, 13),0,2) as int) < 18";
            System.out.println(sql);
            ResultSet res4 = statement.executeQuery(sql);
            if (res4.next()) {
                DouyinList4Json json = new DouyinList4Json();
                json.setCountNum(res4.getInt(1));
                json.setwTime("15-18");
                list.add(json);
            } else {
                DouyinList4Json json = new DouyinList4Json();
                json.setCountNum(0);
                json.setwTime("15-18");
                list.add(json);
            }

            sql = "select count(1) from " + tableName + " where cast(substr(substr(time, 12, 13),0,2) as int) > 18 and cast(substr(substr(time, 12, 13),0,2) as int) < 21";
            System.out.println(sql);
            ResultSet res5 = statement.executeQuery(sql);
            if (res5.next()) {
                DouyinList4Json json = new DouyinList4Json();
                json.setCountNum(res5.getInt(1));
                json.setwTime("18-21");
                list.add(json);
            } else {
                DouyinList4Json json = new DouyinList4Json();
                json.setCountNum(0);
                json.setwTime("18-21");
                list.add(json);
            }

            sql = "select count(1) from " + tableName + " where cast(substr(substr(time, 12, 13),0,2) as int) > 21 and cast(substr(substr(time, 12, 13),0,2) as int) < 24";
            System.out.println(sql);
            ResultSet res6 = statement.executeQuery(sql);
            if (res6.next()) {
                DouyinList4Json json = new DouyinList4Json();
                json.setCountNum(res6.getInt(1));
                json.setwTime("21-24");
                list.add(json);
            } else {
                DouyinList4Json json = new DouyinList4Json();
                json.setCountNum(0);
                json.setwTime("21-24");
                list.add(json);
            }

            session.setAttribute("dataList4", list);
        }

        result.put("dataList4", list);
        result.put(RESULT_CODE, SUCCESS);

        return result;
    }

    @PostMapping("/list5")
    public Map<String, Object> list5(HttpServletRequest request) throws SQLException {
        Map<String, Object> result = new HashMap<>();
        List<DouyinList5Json> list;

        HttpSession session = request.getSession();
        list = (List<DouyinList5Json>) session.getAttribute("dataList5");

        if (list == null) {
            list = new ArrayList<>();
            Statement statement = druidDataSource.getConnection().createStatement();
            StringBuffer sql = new StringBuffer();
            sql.append("select t.* from ");
            sql.append("(select count(*) as countNum, type, province from ").append(tableName).append(" group by type, province) t");
            sql.append(" order by t.countNum desc");
            System.out.println(sql.toString());

            ResultSet res = statement.executeQuery(sql.toString());
            int i = 0;
            while (res.next() && i < 3) {
                i++;
                DouyinList5Json json = new DouyinList5Json();
                json.setCountNum(res.getInt(1));
                json.setType(res.getString(2));
                json.setProvince(res.getString(3));
                list.add(json);
            }
            session.setAttribute("dataList5", list);
        }

        result.put("dataList5", list);
        result.put(RESULT_CODE, SUCCESS);

        return result;
    }

    @PostMapping("/list6")
    public Map<String, Object> list6(HttpServletRequest request) throws SQLException {
        Map<String, Object> result = new HashMap<>();
        List<DouyinBaseListJson> list;

        HttpSession session = request.getSession();
        list = (List<DouyinBaseListJson>) session.getAttribute("dataList6");

        if (list == null) {
            list = new ArrayList<>();
            Statement statement = druidDataSource.getConnection().createStatement();
            StringBuffer sql = new StringBuffer();
            sql.append("select t.* from ");
            sql.append("(select name, type, province, time from ").append(tableName).append(" order by time desc) t limit 10");
            System.out.println(sql.toString());

            ResultSet res = statement.executeQuery(sql.toString());
            while (res.next()) {
                DouyinBaseListJson json = new DouyinBaseListJson();
                json.setName(res.getString(1));
                json.setType(res.getString(2));
                json.setProvince(res.getString(3));
                json.setTime(res.getString(4));
                list.add(json);
            }
            session.setAttribute("dataList6", list);
        }

        result.put("dataList6", list);
        result.put(RESULT_CODE, SUCCESS);

        return result;
    }
}
