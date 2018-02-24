package io.dnsdb.getdns4j.test.format;

import static org.junit.Assert.assertEquals;

import io.dnsdb.getdns4j.format.DNSRecordFormatter;
import io.dnsdb.getdns4j.format.DNSRecordJsonFormatter;
import io.dnsdb.sdk.DNSRecord;
import org.junit.Test;

/**
 * <code>TestDNSRecordJsonFormatter</code>测试类用于测试{@link DNSRecordJsonFormatter}。
 *
 * @author Remonsan
 * @version 1.0
 */
public class TestDNSRecordJsonFormatter {

  @Test
  public void testFormat() {
    DNSRecordFormatter formatter = new DNSRecordJsonFormatter();
    DNSRecord record = new DNSRecord().setHost("www.example.com").setType("a").setValue("1.1.1.1");
    String value = formatter.format(record);
    assertEquals("{\"host\":\"www.example.com\",\"type\":\"a\",\"value\":\"1.1.1.1\"}", value);
  }
}
