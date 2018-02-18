package io.dnsdb.getdns4j.format;

import com.google.common.base.Strings;
import io.dnsdb.sdk.DNSRecord;

/**
 * <code>DNSRecordCustomFormat</code>类用于自定义格式化<code>DNSRecord</code>。
 *
 * @author Remonsan
 * @version 1.0
 */
public class DNSRecordCustomFormat implements DNSRecordFormat {

  private String customFormat;

  public DNSRecordCustomFormat() {
    this(null);
  }

  public DNSRecordCustomFormat(String formatString) {
    this.customFormat = formatString;
  }

  @Override
  public String format(DNSRecord record) {
    customFormat = Strings.nullToEmpty(customFormat);
    return customFormat.replace("#{host}", record.getHost()).replace("#{type}", record.getType())
        .replace("#{value}", record.getValue());
  }

  public String getCustomFormat() {
    return customFormat;
  }

  public DNSRecordCustomFormat setCustomFormat(String customFormat) {
    this.customFormat = customFormat;
    return this;
  }
}
