package io.dnsdb.getdns4j.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import io.dnsdb.getdns4j.SearchCommand;
import io.dnsdb.getdns4j.format.DNSRecordCustomFormatter;
import io.dnsdb.getdns4j.utils.Json;
import io.dnsdb.getdns4j.utils.ProgressBar;
import io.dnsdb.sdk.APIClient;
import io.dnsdb.sdk.DNSRecord;
import io.dnsdb.sdk.DefaultAPIClient;
import io.dnsdb.sdk.Query;
import io.dnsdb.sdk.ScanResult;
import io.dnsdb.sdk.SearchResult;
import io.dnsdb.sdk.exceptions.APIException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.UUID;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.Namespace;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.Before;
import org.junit.Test;

/**
 * <code>TestSearchCommand</code>测试类用于测试{@link SearchCommand}。
 *
 * @author Remonsan
 * @version 1.0
 */
public class TestSearchCommand extends SearchCommand {

  private MockPrintStream outPrintSteam;
  private boolean useMockOutput = true;
  private boolean useMockAPIClient = true;
  private List<DNSRecord> searchRecords = Lists.newArrayList();
  private List<DNSRecord> scanRecords = Lists.newArrayList();

  public TestSearchCommand() {
    super(ArgumentParsers.newFor("getdns4j").build().addSubparsers());
  }

  @Before
  public void setup() {
    outPrintSteam = new MockPrintStream();
    useMockOutput = true;
    useMockAPIClient = true;
    for (int i = 0; i < 10; i++) {
      searchRecords
          .add(new DNSRecord().setHost(i + ".example.com").setType("a").setValue("1.1.1." + i));
    }
    for (int i = 0; i < 20; i++) {
      scanRecords
          .add(new DNSRecord().setHost(i + ".example.com").setType("a").setValue("1.1.1." + i));
    }
  }

  @Override
  protected APIClient buildAPIClient(String apiId, String apiKey, float timeout) {
    if (!useMockAPIClient) {
      return super.buildAPIClient(apiId, apiKey, timeout);
    }
    SearchResult result = new SearchResult(searchRecords, 1000, searchRecords.size());
    APIClient apiClient = mock(APIClient.class);
    try {
      when(apiClient.search(any(Query.class), any(Integer.class), any(Integer.class)))
          .thenReturn(result);
      doThrow(new APIException(10001, "unauthorized")).when(apiClient)
          .search(any(Query.class), eq(2), any(Integer.class));
      ScanResult scanResult = mock(ScanResult.class);
      when(scanResult.iterator()).thenReturn(scanRecords.iterator());
      when(apiClient.scan(any(Query.class), any(Integer.class))).thenReturn(scanResult);
    } catch (APIException | IOException e) {
      e.printStackTrace();
    }
    return apiClient;
  }

  @Override
  protected PrintStream createOutputStream(String output) throws FileNotFoundException {
    if (useMockOutput) {
      return outPrintSteam;
    } else {
      return super.createOutputStream(output);
    }
  }

  @Test
  public void tesExec() {
    MockPrintStream errPrintSteam = new MockPrintStream();
    setErrPrintStream(errPrintSteam);
    ProgressBar progressBar = new ProgressBar();
    progressBar.setPrintStream(mock(PrintStream.class));
    setProgressBar(progressBar);
    Namespace namespace = new SearchCommandNamespaceBuilder().setDomain("example.com").build();
    exec(namespace);
    assertEquals(searchRecords.size(), outPrintSteam.getLines().size());
    for (int i = 0; i < outPrintSteam.getLines().size(); i++) {
      String line = outPrintSteam.getLines().get(i).trim();
      DNSRecord dnsRecord = searchRecords.get(i);
      assertEquals(line, Json.dumps(dnsRecord));
    }

    outPrintSteam.clear();
    namespace = new SearchCommandNamespaceBuilder().setDomain("example.com")
        .setProxy("socks://user:admin@localhost:1080").build();
    assertEquals(-1, exec(namespace));
    assertEquals("invalid proxy", errPrintSteam.getLines().get(0));

    outPrintSteam.clear();
    namespace = new SearchCommandNamespaceBuilder().setDomain("example.com")
        .setProxy("socks5://user:admin@localhost:1080").setCsv(true).build();
    assertEquals(0, exec(namespace));
    CSVFormat csvFormat = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.ALL);
    for (int i = 0; i < outPrintSteam.getLines().size(); i++) {
      String line = outPrintSteam.getLines().get(i).trim();
      DNSRecord dnsRecord = searchRecords.get(i);
      String value = csvFormat
          .format(dnsRecord.getHost(), dnsRecord.getType(), dnsRecord.getValue());
      assertEquals(value, line);
    }

    outPrintSteam.clear();
    namespace = new SearchCommandNamespaceBuilder().setDomain("example.com")
        .setFormat("#{host}|#{type}|#{value}").build();
    assertEquals(0, exec(namespace));
    DNSRecordCustomFormatter formatter = new DNSRecordCustomFormatter("#{host}|#{type}|#{value}");
    for (int i = 0; i < outPrintSteam.getLines().size(); i++) {
      String line = outPrintSteam.getLines().get(i).trim();
      DNSRecord dnsRecord = searchRecords.get(i);
      assertEquals(formatter.format(dnsRecord), line);
    }

    outPrintSteam.clear();
    long max = searchRecords.size() / 2;
    namespace = new SearchCommandNamespaceBuilder().setDomain("example.com").setMax(max).build();
    assertEquals(0, exec(namespace));
    assertEquals(max, outPrintSteam.getLines().size());

    outPrintSteam.clear();
    errPrintSteam.clear();
    namespace = new SearchCommandNamespaceBuilder().setDomain("example.com").setPage(2).build();
    assertEquals(-1, exec(namespace));
    assertEquals("error code: 10001, message: unauthorized", errPrintSteam.getLines().get(0));

    outPrintSteam.clear();
    namespace = new SearchCommandNamespaceBuilder().setDomain("example.com").setAll(true).build();
    assertEquals(0, exec(namespace));
    assertEquals(scanRecords.size(), outPrintSteam.getLines().size());
    for (int i = 0; i < outPrintSteam.getLines().size(); i++) {
      String line = outPrintSteam.getLines().get(i).trim();
      DNSRecord dnsRecord = scanRecords.get(i);
      assertEquals(line, Json.dumps(dnsRecord));
    }
  }

  @Test
  public void testGetOutputSteam() throws IOException {
    useMockOutput = false;
    PrintStream printStream = createOutputStream("-");
    assertEquals(System.out, printStream);
    printStream = createOutputStream("output.txt");
    printStream.close();
    File file = new File("output.txt");
    assertTrue(file.exists());
    assertTrue(file.delete());
  }

  @Test
  public void testGetErrPrintSteam() {
    assertEquals(System.err, getErrPrintStream());
  }

  @Test
  public void testBuildAPIClient() {
    useMockAPIClient = false;
    String apiId = UUID.randomUUID().toString().replace("-", "");
    String apiKey = UUID.randomUUID().toString().replace("-", "");
    float timeout = 15;
    APIClient apiClient = buildAPIClient(apiId, apiKey, timeout);
    assertTrue(apiClient instanceof DefaultAPIClient);
  }

  @Test
  public void testGetCommand() {
    assertEquals("search", getCommand());
  }
}
