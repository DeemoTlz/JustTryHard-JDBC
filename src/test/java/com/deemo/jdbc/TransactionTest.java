package com.deemo.jdbc;

import org.junit.Test;

import java.sql.*;
import java.util.List;

/**
 * MySQL：MyISAM引擎不支持事务，InnoDB支持事务
 */
public class TransactionTest {

    @Test
    public void textTransactions() throws Exception {
        Connection conn = getConn();

        try {
            // MySQL默认事务隔离级别：可重复读
            conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
            // conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            // 开始事务
            conn.setAutoCommit(false);

            // 处理业务
            select(conn);
            // 当某事务正在查询某个字段时，不允许其他事务对其进行修改
            // 能修改成功
            update();

            // 其他事务修改后，重新读取，仍然是修改前的值
            select(conn);

            // 提交事务
            conn.commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            conn.rollback();
        }
    }

    @Test
    public void textTransactionsRollBack() throws Exception {
        Connection conn = getConn();

        try {
            // MySQL默认事务隔离级别：可重复读
            conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
            // conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            // 开始事务
            conn.setAutoCommit(false);

            // 处理业务
            PreparedStatement preparedStatement = conn.prepareStatement("UPDATE person SET `name` = ? WHERE `id` = 1");
            preparedStatement.setString(1, "小张三");
            preparedStatement.executeUpdate();

            int i = 10 / 0;

            // 提交事务
            conn.commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            conn.rollback();
        }
    }

    private static void update() throws Exception {
        Connection conn = getConn();
        conn.setAutoCommit(false);
        conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
        //conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

        PreparedStatement preparedStatement = conn.prepareStatement("UPDATE person SET `name` = ? WHERE `id` = 1");
        preparedStatement.setString(1, "张三2");
        preparedStatement.executeUpdate();

        conn.commit();

        preparedStatement.close();
        conn.close();
    }

    private static void insert() throws Exception {
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

        while (resultSet.next()) {
            Object generatedKey = resultSet.getObject("GENERATED_KEY");
            System.out.println("key: " + generatedKey);
        }
    }

    private static void select(Connection conn) throws Exception {
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT `name` FROM person where `id` = 1");

        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            System.out.println("name: " + resultSet.getObject(1));
        }
    }

    private static Connection getConn() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");

        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1/deemo", "root", "a12345");

        System.out.println(connection);

        return connection;
    }
}
