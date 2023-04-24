package com.ketty.mybatis.demo;


import com.google.common.collect.Lists;

import java.sql.*;
import java.util.ArrayList;

/**
 * @Author ketty bluce
 * @Create 2023/4/19
 * @Version 1.0
 */
public class Application {
  public static void main(String[] args) {
    /**
     * 1.导入jdbc驱动包
     * 2.通过 DriverManager 注册驱动
     * 3.创建连接 ☆
     * 4.创建 Statement 对象  ☆
     * 5.CRUD ☆
     * 6.操作结果集  ☆
     * 7.关闭连接
     */
    Connection connection = null;

    Statement statement = null;

    ResultSet resultSet = null;

    ArrayList<User> list = Lists.newArrayList();
    try {
      // 反射加载驱动
      Class.forName("com.mysql.cj.jdbc.Driver");
      // 获取连接 静态代码块中将所有的资源进行加载
      connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "root");
      // 代表一次SQL的操作
      statement = connection.createStatement();

      resultSet = statement.executeQuery("select * from  User ");

      while (resultSet.next()) {
        Integer id = resultSet.getInt(1);

        String string = resultSet.getString(2);

        Integer age = resultSet.getInt(3);

        User user = new User(id, string, age);

        System.out.println(user);
      }

    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      if (resultSet != null) {
        try {
          resultSet.close();
        } catch (SQLException e) {
          throw new RuntimeException(e);
        }
      }

      if (statement != null) {
        try {
          statement.close();
        } catch (SQLException e) {
          throw new RuntimeException(e);
        }
      }
      if (connection != null) {
        try {
          connection.close();
        } catch (SQLException e) {
          throw new RuntimeException(e);
        }
      }

    }
  }
}
