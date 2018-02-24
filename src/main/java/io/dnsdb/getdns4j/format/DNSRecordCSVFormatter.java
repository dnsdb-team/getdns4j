package io.dnsdb.getdns4j.format;

import io.dnsdb.sdk.DNSRecord;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;

/**
 * <code>DNSRecordCSVFormatter</code>类用于将<code>DNSRecord</code>格式化为CSV。
 *
 * @author Remonsan
 * @version 1.0
 */
public class DNSRecordCSVFormatter implements DNSRecordFormatter {

  private CSVFormat csvFormat = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.ALL);

  public String format(DNSRecord record) {
    return csvFormat.format(record.getHost(), record.getType(), record.getValue());
  }

}
