package io.dnsdb.getdns4j.format;

import io.dnsdb.sdk.DNSRecord;

/**
 * <code>DNSRecordFormat</code>接口定义了格式化<code>DNSRecord</code>类的方法。
 *
 * @author Remonsan
 * @version 1.0
 */
public interface DNSRecordFormat {

  /**
   * 格式化<code>DNSRecord</code>对象。
   *
   * @param record 需要格式化的<code>DNSRecord</code>对象。
   * @return 格式化后的字符串。
   */
  String format(DNSRecord record);

}
