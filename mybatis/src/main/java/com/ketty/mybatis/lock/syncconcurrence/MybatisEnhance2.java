package com.ketty.mybatis.lock.syncconcurrence;import com.google.common.collect.Lists;import lombok.Data;import java.util.HashMap;import java.util.List;import java.util.Map;/** * @Author ketty bluce * @Create 2023/5/24 * @Version 1.0 * <p> * sync并行流处理 */public class MybatisEnhance2 {    private static int size = 10000000;    private static Map<Object, Object> objectMap = new HashMap<>(size);    @Data    static class MapperScanner {        private String mapperLocation;        public List<Object> scanMapper() {            List<Object> objects = Lists.newArrayList();            for (int i = 0; i < size; i++) {                objects.add(new Object());            }            return objects;        }    }    public static synchronized void put(Object obj) {        objectMap.put(obj, obj);    }    public static void main(String[] args) {        String mapperLocation = "classpath:mapper/*/xml";        MapperScanner mapperScanner = new MapperScanner();        mapperScanner.setMapperLocation(mapperLocation);        List<Object> objects = mapperScanner.scanMapper();        long start = System.currentTimeMillis();        objects.parallelStream().forEach(obj -> put(obj));        long end = System.currentTimeMillis();        System.out.println(end - start);    }}