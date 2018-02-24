package io.dnsdb.getdns4j.utils.proxy;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

/**
 * <code>ProxyAuthenticator</code>类表示代理身份验证器。
 *
 * @author Remonsan
 * @version 1.0
 */
public class ProxyAuthenticator extends Authenticator {

  private String user = "";
  private String password = "";

  public ProxyAuthenticator(String user, String password) {
    this.user = user;
    this.password = password;
  }

  @Override
  protected PasswordAuthentication getPasswordAuthentication() {
    return new PasswordAuthentication(user, password.toCharArray());
  }
}
