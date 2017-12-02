package com.cn.eric.rediscluster.utils;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.log4j.Logger;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
*
* @author:xiaguilin
* @Date:2017/11/08
* @Email:ericsteves@outlook.com
* JedisClusterExt为JedisCluster的扩展类
* 备注：JedisCluster中没有查询keys通配符的方法
* 另外，没有pipeline的方法，扩展类是为了实现这两个功能
*
* */
public class JedisClusterExt extends JedisCluster{
  Logger logger = Logger.getLogger(JedisClusterExt.class);

  public JedisClusterExt(HostAndPort node) {
    super(node);
  }

  public JedisClusterExt(HostAndPort node, int timeout) {
    super(node, timeout);
  }

  public JedisClusterExt(HostAndPort node, int timeout, int maxAttempts) {
    super(node, timeout, maxAttempts);
  }

  public JedisClusterExt(HostAndPort node, GenericObjectPoolConfig poolConfig) {
    super(node, poolConfig);
  }

  public JedisClusterExt(HostAndPort node, int timeout, GenericObjectPoolConfig poolConfig) {
    super(node, timeout, poolConfig);
  }

  public JedisClusterExt(HostAndPort node, int timeout, int maxAttempts, GenericObjectPoolConfig poolConfig) {
    super(node, timeout, maxAttempts, poolConfig);
  }

  public JedisClusterExt(HostAndPort node, int connectionTimeout, int soTimeout, int maxAttempts, GenericObjectPoolConfig poolConfig) {
    super(node, connectionTimeout, soTimeout, maxAttempts, poolConfig);
  }

  public JedisClusterExt(HostAndPort node, int connectionTimeout, int soTimeout, int maxAttempts, String password, GenericObjectPoolConfig poolConfig) {
    super(node, connectionTimeout, soTimeout, maxAttempts, password, poolConfig);
  }

  public JedisClusterExt(Set<HostAndPort> nodes) {
    super(nodes);
  }

  public JedisClusterExt(Set<HostAndPort> nodes, int timeout) {
    super(nodes, timeout);
  }

  public JedisClusterExt(Set<HostAndPort> nodes, int timeout, int maxAttempts) {
    super(nodes, timeout, maxAttempts);
  }

  public JedisClusterExt(Set<HostAndPort> nodes, GenericObjectPoolConfig poolConfig) {
    super(nodes, poolConfig);
  }

  public JedisClusterExt(Set<HostAndPort> nodes, int timeout, GenericObjectPoolConfig poolConfig) {
    super(nodes, timeout, poolConfig);
  }

  public JedisClusterExt(Set<HostAndPort> jedisClusterNode, int timeout, int maxAttempts, GenericObjectPoolConfig poolConfig) {
    super(jedisClusterNode, timeout, maxAttempts, poolConfig);
  }

  public JedisClusterExt(Set<HostAndPort> jedisClusterNode, int connectionTimeout, int soTimeout, int maxAttempts, GenericObjectPoolConfig poolConfig) {
    super(jedisClusterNode, connectionTimeout, soTimeout, maxAttempts, poolConfig);
  }

  public JedisClusterExt(Set<HostAndPort> jedisClusterNode, int connectionTimeout, int soTimeout, int maxAttempts, String password, GenericObjectPoolConfig poolConfig) {
    super(jedisClusterNode, connectionTimeout, soTimeout, maxAttempts, password, poolConfig);
  }

  //线程安全的incr操作
  public Long incrWithSafeThread(String key){
    Long i = 0L;
    Lock lock = new ReentrantLock();
    lock.lock();
    try{
      i = incr(key);
    }finally {
      lock.unlock();
    }
    return i;
  }

  /**
   * @param pattern
   * @return TreeSet<String>
   * @auth xiaguilin
   * 返回集群中符合条件的keys
   * */
  public TreeSet<String> keys(String pattern){
    TreeSet<String> keys = new TreeSet<String>();
    Map<String, JedisPool> clusterNodes = this.getClusterNodes();
    for(String k : clusterNodes.keySet()){
      JedisPool jp = clusterNodes.get(k);
      Jedis connection = jp.getResource();
      try {
        keys.addAll(connection.keys(pattern));
      } catch(Exception e){
        logger.error("Getting keys error: {}", e);
      } finally{
        logger.debug("Connection closed.");
        connection.close();//用完一定要close这个链接！！！
      }
    }
    return keys;
  }


  /**
   * 根据jedisCluster实例生成对应的JedisClusterPipeline
   * @param
   * @return JedidsClusterPipeline
   */
  public JedisClusterPipeline pipelined() {
    JedisClusterPipeline pipeline = new JedisClusterPipeline(this);
    return pipeline;
  }


}
