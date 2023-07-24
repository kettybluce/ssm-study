package com.ketty.mybatis.demo;

/**
 * @Author ketty bluce
 * @Create 2023/4/20
 * @Version 1.0
 */
public class Test {

  public static void main(String[] args) throws Exception {
//    获取Class 对象

    Class<?> aClass = Class.forName("test.User");

    Class<User> userClass = User.class;

    Class<? extends User> aClass1 = new User().getClass();
//  创建对象
    User user = userClass.newInstance();
  }
}
