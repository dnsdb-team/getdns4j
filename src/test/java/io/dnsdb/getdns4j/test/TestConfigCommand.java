package io.dnsdb.getdns4j.test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.dnsdb.getdns4j.ConfigCommand;
import io.dnsdb.getdns4j.Settings;
import java.io.File;
import java.util.UUID;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparsers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * <code>TestConfigCommand</code>测试类用于测试{@link ConfigCommand}。
 *
 * @author Remonsan
 */
public class TestConfigCommand {

  private ConfigCommand configCommand;
  private File file = new File(Settings.CONFIG);

  @Before
  public void setup() {
    Subparsers subparsers = ArgumentParsers.newFor("getdns4j").build().addSubparsers();
    configCommand = new ConfigCommand(subparsers);
    if (file.exists()) {
      file.delete();
    }
  }

  @After
  public void tearDown() {
    if (file.exists()) {
      file.delete();
    }
  }

  @Test
  public void testGetCommand() {
    assertEquals("config", configCommand.getCommand());
  }

  @Test
  public void testExec() {
    Namespace namespace = mock(Namespace.class);
    when(namespace.getBoolean(eq("show"))).thenReturn(true);
    MockPrintStream output = new MockPrintStream();
    configCommand.setOutPrintSteam(output);
    configCommand.exec(namespace);
    String content = "{\n"
        + "  \"api_id\" : \"\",\n"
        + "  \"proxy\" : \"\",\n"
        + "  \"api_key\" : \"\",\n"
        + "  \"api_url\" : \"https://api.dnsdb.io\",\n"
        + "  \"api_timeout\" : 15.0\n"
        + "}";
    assertEquals(content, output.getLines().get(0));
    when(namespace.getBoolean(eq("show"))).thenReturn(false);
    String apiId = UUID.randomUUID().toString().replace("-", "");
    String apiKey = UUID.randomUUID().toString().replace("-", "");
    float timeout = 10;
    String proxy = "socks5://localhost@1080";
    String apiUrl = "http://localhost:8080/api";
    when(namespace.get(eq("api_id"))).thenReturn(apiId);
    when(namespace.get(eq("api_key"))).thenReturn(apiKey);
    when(namespace.get(eq("timeout"))).thenReturn(timeout);
    when(namespace.get(eq("proxy"))).thenReturn(proxy);
    when(namespace.get(eq("api_url"))).thenReturn(apiUrl);
    assertFalse(file.exists());
    configCommand.exec(namespace);
    assertTrue(file.exists());
    Settings settings = Settings.newInstance();
    assertEquals(apiId, settings.getApiId());
    assertEquals(apiKey, settings.getApiKey());
    assertEquals(timeout, settings.getTimeout(), 0.01);
    assertEquals(proxy, settings.getProxy());
    assertEquals(apiUrl, settings.getApiUrl());

  }


}
