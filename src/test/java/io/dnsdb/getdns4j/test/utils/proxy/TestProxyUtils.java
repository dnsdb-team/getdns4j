package io.dnsdb.getdns4j.test.utils.proxy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import io.dnsdb.getdns4j.utils.proxy.InvalidProxyStringException;
import io.dnsdb.getdns4j.utils.proxy.ProxyAuthenticator;
import io.dnsdb.getdns4j.utils.proxy.ProxyUtils;
import java.lang.reflect.Field;
import java.net.Authenticator;
import java.util.Properties;
import org.junit.Before;
import org.junit.Test;

/**
 * <code>TestProxyUtils</code>测试类用测试{@link ProxyUtils}。
 *
 * @author Remonsan
 * @version 1.0
 */
public class TestProxyUtils extends ProxyUtils {

  public static Object getFieldValue(Class clazz, String fieldName) {
    try {
      Field field = clazz.getDeclaredField(fieldName);
      field.setAccessible(true);
      return field.get(clazz);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Before
  public void setup() {
    Authenticator.setDefault(null);
  }

  @Test
  public void testSetSocks5() {
    assertNotNull(SOCKS_PROXY_HOST_KEY);
    assertNotNull(SOCKS_PROXY_PORT_KEY);
    String host = "localhost";
    int port = 1080;
    String user = "user";
    String pass = "123456";
    Properties properties = System.getProperties();
    setSocks5(host, port);
    assertEquals(host, properties.getProperty(SOCKS_PROXY_HOST_KEY));
    assertEquals(port, Integer.parseInt(properties.getProperty(SOCKS_PROXY_PORT_KEY)));
    assertNull(getFieldValue(Authenticator.class, "theAuthenticator"));
    setSocks5(host, port, user, pass);
    assertEquals(host, properties.getProperty(SOCKS_PROXY_HOST_KEY));
    assertEquals(port, Integer.parseInt(properties.getProperty(SOCKS_PROXY_PORT_KEY)));
    Object authenticator = getFieldValue(Authenticator.class, "theAuthenticator");
    assertTrue(authenticator instanceof ProxyAuthenticator);
    Authenticator.setDefault(null);
    setSocks5(host, port, user, null);
    assertNull(getFieldValue(Authenticator.class, "theAuthenticator"));
    Authenticator.setDefault(null);
    setSocks5(host, port, null, pass);
    assertNull(getFieldValue(Authenticator.class, "theAuthenticator"));
  }

  @Test
  public void testSetHttpProxy() {
    assertNotNull(HTTP_PROXY_HOST_KEY);
    assertNotNull(HTTP_PROXY_PORT_KEY);
    String host = "localhost";
    int port = 8080;
    String user = "user";
    String pass = "123456";
    Properties properties = System.getProperties();
    setHttpProxy(host, port);
    assertEquals(host, properties.getProperty(HTTP_PROXY_HOST_KEY));
    assertEquals(port, Integer.parseInt(properties.getProperty(HTTP_PROXY_PORT_KEY)));
    assertNull(getFieldValue(Authenticator.class, "theAuthenticator"));
    setHttpProxy(host, port, user, pass);
    assertEquals(host, properties.getProperty(HTTP_PROXY_HOST_KEY));
    assertEquals(port, Integer.parseInt(properties.getProperty(HTTP_PROXY_PORT_KEY)));
    Object authenticator = getFieldValue(Authenticator.class, "theAuthenticator");
    assertTrue(authenticator instanceof ProxyAuthenticator);
    Authenticator.setDefault(null);
    setHttpProxy(host, port, user, null);
    assertNull(getFieldValue(Authenticator.class, "theAuthenticator"));
    Authenticator.setDefault(null);
    setHttpProxy(host, port, null, pass);
    assertNull(getFieldValue(Authenticator.class, "theAuthenticator"));

  }

  @Test
  public void testSetProxy() throws InvalidProxyStringException {
    assertNotNull(HTTP_PROXY_HOST_KEY);
    assertNotNull(HTTP_PROXY_PORT_KEY);
    assertNotNull(SOCKS_PROXY_HOST_KEY);
    assertNotNull(SOCKS_PROXY_PORT_KEY);
    Properties properties = System.getProperties();
    setProxy("http://user:123456@localhost:8080");
    assertEquals("localhost", properties.getProperty(HTTP_PROXY_HOST_KEY));
    assertEquals(8080, Integer.parseInt(properties.getProperty(HTTP_PROXY_PORT_KEY)));
    setProxy("socks5://user:123456@localhost:1080");
    assertEquals("localhost", properties.getProperty(SOCKS_PROXY_HOST_KEY));
    assertEquals(1080, Integer.parseInt(properties.getProperty(SOCKS_PROXY_PORT_KEY)));
  }

  @Test(expected = InvalidProxyStringException.class)
  public void testSetProxyThrowException() throws InvalidProxyStringException {
    setProxy("socks5://user:123456localhost:1080");
  }


}
