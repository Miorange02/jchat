package edu.csust.util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBHelper {
    static String databaseName = "jchat";
    static  String username = "root";
    static String password = "123456";
    static String url = "jdbc:mysql://localhost:3306/" + databaseName + "?characterEncoding=utf-8";

    // 静态代码块，当类被加载的时候会调用，只会调用一次
    static{
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @return 数据库的连接对象
     */

    public static Connection getConnection(){
        try {
            System.out.println("尝试连接数据库，URL: " + url);
            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("数据库连接成功");
            return connection;
        } catch (SQLException e) {
            System.err.println("数据库连接失败:");
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param statement
     * @param connection
     * 关闭资源
     */
    public static void close(ResultSet resultSet,PreparedStatement statement, Connection connection){
        if(resultSet != null){
            try {
                resultSet.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        if(statement != null){
            try {
                statement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        if(connection != null){
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 增删改的通用方法
     * @param sql 增删改的sql语句
     * @param params sql语句中占位符所对应的值
     * @return 数据库中受影响的行数
     */
    public static int executeUpdate(String sql, Object... params){
        // Object... 可变长度的参数
        Connection connection = getConnection();
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            if(params != null){
                // 给 sql语句中的 占位符 进行赋值
                for (int i = 0; i < params.length; i++) {
                    statement.setObject(i + 1, params[i]);
                }
            }
            // 真正去执行sql语句
            int i = statement.executeUpdate();
            return i;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            close(null,statement,connection);
        }
    }

    /**
     * 通用的查询方法
     * @param sql 查询的 SQL 语句
     * @param rowMapper 行映射器，用于将 ResultSet 中的一行数据映射为一个对象
     * @param params SQL 语句中占位符所对应的值
     * @param <T> 映射对象的类型
     * @return 查询结果列表
     */
    public static <T> List<T> query(String sql, RowMapper<T> rowMapper, Object... params) {
        List<T> resultList = new ArrayList<>();
        Connection connection = getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(sql);
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    statement.setObject(i + 1, params[i]);
                }
            }
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                T item = rowMapper.mapRow(resultSet);
                resultList.add(item);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(resultSet, statement, connection);
        }
        return resultList;
    }

    /**
     * 行映射器接口，用于将 ResultSet 中的一行数据映射为一个对象
     * @param <T> 映射对象的类型
     */
    public interface RowMapper<T> {
        T mapRow(ResultSet rs) throws SQLException;
    }

}
