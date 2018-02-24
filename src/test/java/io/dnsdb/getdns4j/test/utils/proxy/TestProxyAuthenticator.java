package io.dnsdb.getdns4j.test.utils.proxy;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import io.dnsdb.getdns4j.utils.proxy.ProxyAuthenticator;
import java.net.PasswordAuthentication;
import org.junit.Test;

/**
 * <code>TestProxyAuthenticator</code>测试类用于测试{@link ProxyAuthenticator}。
 *
 * @author Remonsan
 */
public class TestProxyAuthenticator extends ProxyAuthenticator {

  private static final String USER = "admin";
  private static final String PASSWORD = "123456";

  public TestProxyAuthenticator() {
    super(USER, PASSWORD);
  }

  @Test
  public void testGetPasswordAuthentication() {
    PasswordAuthentication authentication = getPasswordAuthentication();
    assertEquals(USER, authentication.getUserName());
    assertArrayEquals(PASSWORD.toCharArray(), authentication.getPassword());
  }

}
