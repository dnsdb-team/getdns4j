package io.dnsdb.getdns4j.format;

import io.dnsdb.getdns4j.utils.Json;
import io.dnsdb.sdk.DNSRecord;

/**
 * <code>DNSRecordJsonFormat</code>类用于将<code>DNSRecord</code>格式化为JSON。
 *
 * @author Remonsan
 */
public class DNSRecordJsonFormat implements DNSRecordFormat {

  @Override
  public String format(DNSRecord record) {
    return Json.dumps(record);
  }
}
