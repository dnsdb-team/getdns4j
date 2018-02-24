package io.dnsdb.getdns4j.format;

import com.google.common.base.Strings;
import io.dnsdb.sdk.DNSRecord;

/**
 * <code>DNSRecordCustomFormatter</code>类用于自定义格式化<code>DNSRecord</code>。
 *
 * @author Remonsan
 * @version 1.0
 */
public class DNSRecordCustomFormatter implements DNSRecordFormatter {

  private String customFormat;

  public DNSRecordCustomFormatter() {
    this(null);
  }

  public DNSRecordCustomFormatter(String formatString) {
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

  public DNSRecordCustomFormatter setCustomFormat(String customFormat) {
    this.customFormat = customFormat;
    return this;
  }
}
