package io.dnsdb.getdns4j.test.format;

import static org.junit.Assert.assertEquals;

import io.dnsdb.getdns4j.format.DNSRecordCustomFormatter;
import io.dnsdb.getdns4j.format.DNSRecordFormatter;
import io.dnsdb.sdk.DNSRecord;
import org.junit.Test;

/**
 * <code>TestDNSRecordCustomFormatter</code>测试类用于测试{@link DNSRecordCustomFormatter}。
 *
 * @author Remonsan
 */
public class TestDNSRecordCustomFormatter {

  @Test
  public void testFormat() {
    DNSRecordFormatter formatter = new DNSRecordCustomFormatter("#{host}|#{type}|#{value}");
    DNSRecord record = new DNSRecord().setHost("www.example.com").setType("a").setValue("1.1.1.1");
    String value = formatter.format(record);
    assertEquals("www.example.com|a|1.1.1.1", value);
  }

  @Test
  public void testGetterSetter() {
    String format = "#{host}|#{type}|#{value}";
    DNSRecordCustomFormatter formatter = new DNSRecordCustomFormatter();
    formatter.setCustomFormat(format);
    assertEquals(format, formatter.getCustomFormat());
  }

}
