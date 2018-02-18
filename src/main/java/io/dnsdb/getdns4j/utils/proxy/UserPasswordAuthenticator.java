package io.dnsdb.getdns4j.utils.proxy;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

/**
 * <code>UserPasswordAuthenticator</code>类表示基于用户名密码的验证器。
 *
 * @author Remonsan
 * @version 1.0
 */
public class UserPasswordAuthenticator extends Authenticator {

  private String user = "";
  private String password = "";

  public UserPasswordAuthenticator(String user, String password) {
    this.user = user;
    this.password = password;
  }

  protected PasswordAuthentication getPasswordAuthentication() {
    return new PasswordAuthentication(user, password.toCharArray());
  }
}
