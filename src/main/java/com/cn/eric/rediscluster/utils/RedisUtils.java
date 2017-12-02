package com.cn.eric.rediscluster.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author:xiaguilin
 * @Date:2017/11/08
 * @Email:ericsteves@outlook.com
 * RedisUtils 工具类
 * */

public class RedisUtils {

  public static List<Map> getHostAndPort(String server){

    List<Map> listMap = new ArrayList<Map>();

    String[] hostAndPort = server.split(",");
    for(String s : hostAndPort){
      String[] ss = s.split(":");
      Map map = new HashMap();
      map.put("host",ss[0]);
      map.put("port",ss[1]);
      listMap.add(map);
    }

    return listMap;
  }

  public static String redisKeyTransfor(String origin,String from,String to){
    StringBuffer sb = new StringBuffer(origin);
    sb = new StringBuffer(to).append(sb.substring(from.length()));
    return sb.toString();
  }


}
