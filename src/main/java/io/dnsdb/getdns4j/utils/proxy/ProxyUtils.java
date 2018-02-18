package io.dnsdb.getdns4j.utils.proxy;

import io.dnsdb.getdns4j.utils.InvalidProxyStringException;
import java.net.Authenticator;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <code>ProxyUtils</code>类用于设置代理。
 *
 * @author Remonsan
 * @version 1.0
 */
public class ProxyUtils {

  private static final Pattern proxyPattern = Pattern.compile(
      "^(?<type>http|socks5)://((?<user>\\w+):(?<pass>\\w+)@)?(?<host>[\\w\\\\.]+):(?<port>\\d{0,5})/?$");

  public static void setSocks5(String host, int port, String user, String pass) {
    Properties properties = System.getProperties();
    properties.setProperty("socksProxyHost", host);
    properties.setProperty("socksProxyPort", port + "");
    if (user != null || pass != null) {
      Authenticator.setDefault(new UserPasswordAuthenticator(user, pass));
    }
  }

  public static void setHttpProxy(String host, int port, String user, String pass) {
    Properties properties = System.getProperties();
    properties.setProperty("http.proxyHost", host);
    properties.setProperty("http.proxyPort", port + "");
    if (user != null || pass != null) {
      Authenticator.setDefault(new UserPasswordAuthenticator(user, pass));
    }
  }

  public static void setSocks5(String host, int port) {
    setSocks5(host, port, null, null);
  }

  public static void setHttpProxy(String host, int port) {
    setSocks5(host, port, null, null);
  }

  public static void setProxy(String proxyString) throws InvalidProxyStringException {
    Matcher matcher = proxyPattern.matcher(proxyString);
    if (matcher.find()) {
      String type = matcher.group("type");
      String user = matcher.group("user");
      String pass = matcher.group("pass");
      String host = matcher.group("host");
      int port = Integer.parseInt(matcher.group("port"));
      switch (type) {
        case "socks5":
          setSocks5(host, port, user, pass);
          break;
        case "http":
          setHttpProxy(host, port, user, pass);
          break;
      }
    } else {
      throw new InvalidProxyStringException();
    }
  }

}
