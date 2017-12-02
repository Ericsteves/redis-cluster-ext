package com.cn.eric.rediscluster;

import com.cn.eric.rediscluster.configuration.RedisConfiguration;
import com.cn.eric.rediscluster.utils.JedisClusterExt;
import com.cn.eric.rediscluster.utils.RedisUtils;
import org.apache.log4j.Logger;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author:xiaguilin
 * @Date:2017/11/08
 * @Email:ericsteves@outlook.com
 * JedisClusterManager是JedisClusterExt的核心管理类，用于配置并初始化JedisClusterExt
 * JedisClusterManager是单例
 * */

public class JedisClusterManager {
  private Logger logger = Logger.getLogger(JedisClusterManager.class);

  private static volatile JedisClusterManager jedisClusterManager = null;
  protected JedisClusterExt jedisCluster = null;


  private JedisClusterManager(){

    Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
    for(Map<String,String> map : RedisUtils.getHostAndPort(RedisConfiguration.getRedisServer())){
      jedisClusterNodes.add(new HostAndPort(map.get("host"),Integer.parseInt(map.get("port"))));
    }

    JedisPoolConfig poolConfig = new JedisPoolConfig();
    poolConfig.setMaxTotal(Integer.parseInt(RedisConfiguration.getMaxTotal()));
    poolConfig.setMaxIdle(Integer.parseInt(RedisConfiguration.getMaxIdle()));
    poolConfig.setMinIdle(Integer.parseInt(RedisConfiguration.getMinIdle()));
    poolConfig.setMaxWaitMillis(Integer.parseInt(RedisConfiguration.getMaxWaitMillis()));

    jedisCluster = new JedisClusterExt(jedisClusterNodes,poolConfig);

    jedisClusterNodes = null;//help gc work
    poolConfig = null;//help gc work
  };

  //线程安全单例
  public static JedisClusterManager getInstance(){
    if(jedisClusterManager==null){
      synchronized(JedisClusterManager.class){
        if(jedisClusterManager==null){//double check
          jedisClusterManager = new JedisClusterManager();//volatile防止此处发生指令重排
        }
      }
    }
    return jedisClusterManager;
  }

  public JedisClusterExt getJedisCluster() {
    return jedisCluster;
  }

  public void setJedisCluster(JedisClusterExt jedisCluster) {
    this.jedisCluster = jedisCluster;
  }

}