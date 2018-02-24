package io.dnsdb.getdns4j.test;

import net.sourceforge.argparse4j.inf.Namespace;

/**
 * <code>SearchCommandNamespaceBuilder</code>类用于辅助测试{@link io.dnsdb.getdns4j.SearchCommand}。在测试{@link
 * io.dnsdb.getdns4j.SearchCommand#exec(Namespace)}方法时生成模拟的<code>Namespace</code>对象。
 *
 * @author Remonsan
 * @version 1.0
 */
public class SearchCommandNamespaceBuilder {

  private String output = "-";
  private boolean json = true;
  private boolean csv = false;
  private String format = null;
  private Long max = null;
  private String proxy = null;
  private String apiUrl = "https://api.dnsdb.io";
  private float timeout = 15;
  private String domain = null;
  private String host = null;
  private String ip = null;
  private String type = null;
  private String valueDomain = null;
  private String valueHost = null;
  private String valueIp = null;
  private String email = null;
  private int page = 1;
  private int pageSize = 50;
  private boolean all = false;
  private String apiKey = null;
  private String apiId = null;

  public Namespace build() {
    return MockNamespaceBuilder.build(this);
  }

  public String getOutput() {
    return output;
  }

  public SearchCommandNamespaceBuilder setOutput(String output) {
    this.output = output;
    return this;
  }

  public boolean isJson() {
    return json;
  }

  public SearchCommandNamespaceBuilder setJson(boolean json) {
    this.json = json;
    return this;
  }

  public boolean isCsv() {
    return csv;
  }

  public SearchCommandNamespaceBuilder setCsv(boolean csv) {
    this.csv = csv;
    return this;
  }

  public String getFormat() {
    return format;
  }

  public SearchCommandNamespaceBuilder setFormat(String format) {
    this.format = format;
    return this;
  }

  public Long getMax() {
    return max;
  }

  public SearchCommandNamespaceBuilder setMax(Long max) {
    this.max = max;
    return this;
  }

  public String getProxy() {
    return proxy;
  }

  public SearchCommandNamespaceBuilder setProxy(String proxy) {
    this.proxy = proxy;
    return this;
  }

  public String getApiUrl() {
    return apiUrl;
  }

  public SearchCommandNamespaceBuilder setApiUrl(String apiUrl) {
    this.apiUrl = apiUrl;
    return this;
  }

  public float getTimeout() {
    return timeout;
  }

  public SearchCommandNamespaceBuilder setTimeout(float timeout) {
    this.timeout = timeout;
    return this;
  }

  public String getDomain() {
    return domain;
  }

  public SearchCommandNamespaceBuilder setDomain(String domain) {
    this.domain = domain;
    return this;
  }

  public String getHost() {
    return host;
  }

  public SearchCommandNamespaceBuilder setHost(String host) {
    this.host = host;
    return this;
  }

  public String getIp() {
    return ip;
  }

  public SearchCommandNamespaceBuilder setIp(String ip) {
    this.ip = ip;
    return this;
  }

  public String getType() {
    return type;
  }

  public SearchCommandNamespaceBuilder setType(String type) {
    this.type = type;
    return this;
  }

  public String getValueDomain() {
    return valueDomain;
  }

  public SearchCommandNamespaceBuilder setValueDomain(String valueDomain) {
    this.valueDomain = valueDomain;
    return this;
  }

  public String getValueHost() {
    return valueHost;
  }

  public SearchCommandNamespaceBuilder setValueHost(String valueHost) {
    this.valueHost = valueHost;
    return this;
  }

  public String getValueIp() {
    return valueIp;
  }

  public SearchCommandNamespaceBuilder setValueIp(String valueIp) {
    this.valueIp = valueIp;
    return this;
  }

  public String getEmail() {
    return email;
  }

  public SearchCommandNamespaceBuilder setEmail(String email) {
    this.email = email;
    return this;
  }

  public int getPage() {
    return page;
  }

  public SearchCommandNamespaceBuilder setPage(int page) {
    this.page = page;
    return this;
  }

  public int getPageSize() {
    return pageSize;
  }

  public SearchCommandNamespaceBuilder setPageSize(int pageSize) {
    this.pageSize = pageSize;
    return this;
  }

  public boolean isAll() {
    return all;
  }

  public SearchCommandNamespaceBuilder setAll(boolean all) {
    this.all = all;
    return this;
  }

  public String getApiKey() {
    return apiKey;
  }

  public SearchCommandNamespaceBuilder setApiKey(String apiKey) {
    this.apiKey = apiKey;
    return this;
  }

  public String getApiId() {
    return apiId;
  }

  public SearchCommandNamespaceBuilder setApiId(String apiId) {
    this.apiId = apiId;
    return this;
  }
}
