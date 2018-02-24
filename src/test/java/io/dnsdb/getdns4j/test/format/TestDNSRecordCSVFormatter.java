package io.dnsdb.getdns4j.test.format;

import static org.junit.Assert.assertEquals;

import io.dnsdb.getdns4j.format.DNSRecordCSVFormatter;
import io.dnsdb.getdns4j.format.DNSRecordFormatter;
import io.dnsdb.sdk.DNSRecord;
import org.junit.Test;

/**
 * <code>TestDNSRecordCSVFormatter</code>测试类用于测试{@link DNSRecordCSVFormatter}。
 *
 * @author Remonsan
 * @version 1.0
 */
public class TestDNSRecordCSVFormatter {

  @Test
  public void testFormat() {
    DNSRecordFormatter formatter = new DNSRecordCSVFormatter();
    DNSRecord record = new DNSRecord().setHost("www.example.com").setType("a").setValue("1.1.1.1");
    String value = formatter.format(record);
    assertEquals("\"www.example.com\",\"a\",\"1.1.1.1\"", value);
  }

}
