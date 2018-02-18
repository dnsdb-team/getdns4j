package io.dnsdb.getdns4j.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * <code>Json</code>类是用于序列化JSON数据的工具类。
 *
 * @author Remonsan
 * @version 1.0
 */
public class Json {

  private final static ObjectMapper objectMapper = new ObjectMapper();

  public static String dumps(Object object) {
    return dumps(object, false);
  }

  public static String dumps(Object object, boolean pretty) {
    try {
      if (pretty) {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
      } else {
        return objectMapper.writeValueAsString(object);
      }
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return null;
  }

}
