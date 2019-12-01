package com.deemo.jdbc;

import com.sun.org.apache.bcel.internal.generic.GETFIELD;
import org.junit.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class driveTest {

    @Test
    public void testInsert() throws Exception {
        String sql = "INSERT INTO person (`name`, `age`, `love`, `address`) VALUES (?, ?, ?, ?)";

        Connection conn = getConn();

        PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        preparedStatement.setString(1, "李四");
        preparedStatement.setInt(2, 22);
        preparedStatement.setString(3, "小红");
        preparedStatement.setString(4, "高新区");

        int i = preparedStatement.executeUpdate();
        System.out.println(i);
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        List<String> colLabels = getColLabels(resultSet);

        while (resultSet.next()) {
            Object generatedKey = resultSet.getObject("GENERATED_KEY");
            System.out.println("key: " + generatedKey);
        }
    }

    @Test
    public void testSelect() throws Exception {
        String sql = "SELECT id AS idd, `name` as xingming FROM person";

        Connection conn = getConn();

        PreparedStatement preparedStatement = conn.prepareStatement(sql);

        ResultSet resultSet = preparedStatement.executeQuery();
        List<String> colLabels = getColLabels(resultSet);
    }

    public <T> T get(Class<T> clazz, String sql, Object ... args) throws Exception {
        Connection conn = getConn();

        PreparedStatement preparedStatement = conn.prepareStatement(sql);

        for (int i = 0; i < args.length; i++) {
            preparedStatement.setObject(i + 1, args[i]);
        }

        ResultSet resultSet = preparedStatement.executeQuery();
        List<String> colLabels = getColLabels(resultSet);


        while (resultSet.next()) {
            for (String colLabel : colLabels) {
                Object value = resultSet.getObject(colLabel);


            }
        }

        return null;
    }

    private List<String> getColLabels(ResultSet rs) throws SQLException {
        List<String> list = new ArrayList<>();

        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        for (int i = 0; i < columnCount; ) {
            System.out.println("label: " + metaData.getColumnLabel(++i));
            System.out.println("name: " + metaData.getColumnName(i));
            System.out.println("typeName: " + metaData.getColumnTypeName(i));
            System.out.println("className: " + metaData.getColumnClassName(i));
            System.out.println();
            list.add(metaData.getColumnLabel(i));
        }

        return list;
    }

    private static Connection getConn() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");

        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1/deemo", "root", "a12345");

        System.out.println(connection);

        return connection;
    }



    // 重载测试
    public void getTlz() {
        getTlz("");
        // getTlz("", "");
        getTlz("", "", "");
        getTlz("", "", 1);
    }

    public static void getTlz(String name) {

    }

    public static void getTlz(String name, String ... args) {

    }

    public static void getTlz(String name, String year, int ... args) {

    }
}
