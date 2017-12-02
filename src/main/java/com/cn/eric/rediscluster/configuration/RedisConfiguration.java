package com.cn.eric.rediscluster.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author:xiaguilin
 * @Date:2017/11/08
 *
 * RedisConfiguration用于加载ReidsCluster的各种属性
 * */

public class RedisConfiguration {

  private final static String PROPERTY_FILE_NAME = "redis-cluster.properties";
  private final static Properties properties = new Properties();
  static {
    InputStream is = RedisConfiguration.class.getClassLoader()
        .getResourceAsStream(PROPERTY_FILE_NAME);
    try {
      // 加载输入流
      properties.load(is);
      // 获得配置的各个属性
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static String getProperty(String name){
    return properties.getProperty(name);
  }

  public static String getExpireTime(){
    return properties.getProperty("redis.expire.time");
  }

  public static String getRedisServer(){
    return properties.getProperty("redis.server");
  }

  public static String getMaxTotal(){
    return properties.getProperty("redis.config.maxTotal");
  }

  public static String getMaxIdle(){
    return properties.getProperty("redis.config.maxIdle");
  }

  public static String getMinIdle(){
    return properties.getProperty("redis.config.minIdle");
  }

  public static String getMaxWaitMillis(){
    return properties.getProperty("redis.config.maxWaitMillis");
  }

  public static String getPassword(){
    return properties.getProperty("redis.cluster.password");
  }

  public static Integer getTimeOut(){
    return Integer.parseInt(properties.getProperty("redis.timeout"));
  }

}
