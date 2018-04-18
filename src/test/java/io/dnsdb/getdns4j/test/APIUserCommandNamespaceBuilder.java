package io.dnsdb.getdns4j.test;


import io.dnsdb.getdns4j.cmd.APIUserCommand;
import net.sourceforge.argparse4j.inf.Namespace;

/**
 * <code>APIUserCommandNamespaceBuilder</code>类用于辅助测试{@link APIUserCommand}。在测试{@link
 * APIUserCommand#exec(Namespace)}方法时生成模拟的<code>Namespace</code>对象。
 *
 * @author Remonsan
 * @version 1.0
 */
public class APIUserCommandNamespaceBuilder {

  private boolean show = false;
  private String apiId = null;
  private String apiKey = null;
  private String apiUrl = null;
  private String proxy = null;
  private float timeout = 15;

  public Namespace build() {
    return MockNamespaceBuilder.build(this);
  }

  public boolean isShow() {
    return show;
  }

  public APIUserCommandNamespaceBuilder setShow(boolean show) {
    this.show = show;
    return this;
  }

  public String getApiId() {
    return apiId;
  }

  public APIUserCommandNamespaceBuilder setApiId(String apiId) {
    this.apiId = apiId;
    return this;
  }

  public String getApiKey() {
    return apiKey;
  }

  public APIUserCommandNamespaceBuilder setApiKey(String apiKey) {
    this.apiKey = apiKey;
    return this;
  }

  public String getApiUrl() {
    return apiUrl;
  }

  public APIUserCommandNamespaceBuilder setApiUrl(String apiUrl) {
    this.apiUrl = apiUrl;
    return this;
  }

  public String getProxy() {
    return proxy;
  }

  public APIUserCommandNamespaceBuilder setProxy(String proxy) {
    this.proxy = proxy;
    return this;
  }

  public float getTimeout() {
    return timeout;
  }

  public APIUserCommandNamespaceBuilder setTimeout(float timeout) {
    this.timeout = timeout;
    return this;
  }
}
