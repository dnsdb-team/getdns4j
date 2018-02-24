package io.dnsdb.getdns4j.test;

import static org.mockito.Mockito.mock;

import com.google.common.collect.Lists;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

/**
 * <code>MockPrintSteam</code>类表示一个模拟的<code>PrintSteam</code>，用于辅助测试。
 *
 * @author Remonsan
 * @version 1.0
 */
public class MockPrintStream extends PrintStream {

  private List<String> lines = Lists.newArrayList();

  public MockPrintStream() {
    super(mock(OutputStream.class));
  }

  @Override
  public void println(String x) {
    lines.add(x);
  }


  public List<String> getLines() {
    return lines;
  }

  public MockPrintStream setLines(List<String> lines) {
    this.lines = lines;
    return this;
  }

  public void clear() {
    lines.clear();
  }

}
