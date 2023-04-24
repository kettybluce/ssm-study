package com.ketty.mybatis.demo;

import com.google.common.collect.Maps;
import org.apache.ibatis.annotations.Select;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Author ketty bluce
 * @Create 2023/4/20
 * @Version 1.0
 */

// 接口
interface UserMapper {
  // 查询所有User对象
  @Select("SELECT * FROM blog WHERE id = #{id} ")
  List<User> selectUserList(int id, String name);
}

public class Application1 {
  public static void main(String[] args) {

    UserMapper userMapper = (UserMapper) Proxy.newProxyInstance(Application1.class.getClassLoader(), new Class[]{UserMapper.class}, new InvocationHandler() {

      @Override
      public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Select annotation = method.getAnnotation(Select.class);
        Map<String, Object> nameArgMap = buildMethodArgNameMap(method, args);

        System.out.println(Arrays.toString(args));

        if (annotation != null) {
          String[] value = annotation.value();
          String sql = value[0];
          parseSql(sql, nameArgMap);
          System.out.println(sql);
          // 获取方法的返回值泛型
          System.out.println(method.getReturnType());
          // 获取方法的返回值对象类型
          System.out.println(method.getGenericParameterTypes());
        }
        System.out.println(method.getName());
        return null;
      }
    });
    userMapper.selectUserList(1, "zhangSan");
  }


  /**
   * 解析sql
   *
   * @param sql
   * @param nameArgMap
   * @return
   */
  public static String parseSql(String sql, Map<String, Object> nameArgMap) {
    StringBuilder stringBuilder = new StringBuilder();
    int length = sql.length();
    for (int i = 0; i < length; i++) {
      char c = sql.charAt(i);
      if (c == '#') {
        int nextIndex = i + 1;
        char nextChar = sql.charAt(nextIndex);
        if (nextChar != '{') {
          throw new RuntimeException(String.format("这里应该为#{\nsql:%s\nindex:%d", stringBuilder.toString(), nextIndex));
        }
        StringBuilder argsB = new StringBuilder();
        i = parseArg(argsB, sql, nextIndex);
        String argsAName = argsB.toString();
        Object argsValue = nameArgMap.get(argsAName);
        if (argsValue == null) {
          throw new RuntimeException(String.format("找不到参数值sql:%s", argsAName));
        }
        stringBuilder.append(argsValue.toString());
        continue;
      }
      stringBuilder.append(c);
    }
    return stringBuilder.toString();
  }

  private static int parseArg(StringBuilder argsB, String sql, int nextIndex) {
    nextIndex++;
    for (; nextIndex < sql.length(); nextIndex++) {
      char c = sql.charAt(nextIndex);
      if (c != '}') {
        argsB.append(c);
        continue;
      }
      if (c == '}') {
        return nextIndex;
      }
    }
    throw new RuntimeException(String.format("缺少右括号\nindex：%d", nextIndex));
  }

  public static Map<String, Object> buildMethodArgNameMap(Method method, Object[] args) {
    Map<String, Object> nameArgMap = Maps.newHashMap();
    Parameter[] parameters = method.getParameters();
    int index[] = {0};
    Arrays.asList(parameters).stream().forEach(parameter -> {
      String name = parameter.getName();
      nameArgMap.put(name, args[index[0]]);
      index[0]++;
    });
    return nameArgMap;
  }

}
