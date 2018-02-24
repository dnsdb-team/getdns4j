package io.dnsdb.getdns4j.test.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.dnsdb.getdns4j.utils.Json;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;


/**
 * <code>TestJson</code>测试类用于测试{@link Json}。
 *
 * @author Remonsan
 * @version 1.0
 */
public class TestJson extends Json {

  @Test
  public void testDumps() {
    Map<String, Object> map = new HashMap<>();
    map.put("size", 5);
    map.put("color", "red");
    String json = dumps(map);
    assertEquals("{\"size\":5,\"color\":\"red\"}", json);
    json = dumps(map, true);
    String prettyJson = "{\n"
        + "  \"size\" : 5,\n"
        + "  \"color\" : \"red\"\n"
        + "}";
    assertEquals(prettyJson, json);
    Object mockItem = mock(Object.class);
    when(mockItem.toString()).thenReturn(mockItem.getClass().getName());
    assertNull(dumps(mockItem));

  }

}
