package io.dnsdb.getdns4j.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.dnsdb.getdns4j.APIUserWrapper;
import io.dnsdb.getdns4j.cmd.APIUserCommand;
import io.dnsdb.getdns4j.utils.Json;
import io.dnsdb.sdk.APIClient;
import io.dnsdb.sdk.APIUser;
import io.dnsdb.sdk.DefaultAPIClient;
import io.dnsdb.sdk.exceptions.APIException;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.Namespace;
import org.junit.Before;
import org.junit.Test;

/**
 * <code>TestAPIUserCommand</code>测试类用于测试{@link APIUserCommand}。
 *
 * @author Remonsan
 */
public class TestAPIUserCommand extends APIUserCommand {

  private boolean useMockAPIClient = true;
  private MockPrintStream output;
  private Exception throwException;
  private APIUser apiUser;

  public TestAPIUserCommand() {
    super(ArgumentParsers.newFor("getdns4j").build().addSubparsers());
  }

  @Before
  public void setup() {
    output = new MockPrintStream();
    useMockAPIClient = true;
    throwException = null;
    String apiId = UUID.randomUUID().toString().replace("-", "");
    apiUser = new APIUser(apiId, "admin", 1000, new Date(), new Date());
  }

  @Override
  protected APIClient buildAPIClient(String apiId, String apiKey, float timeout) {
    if (!useMockAPIClient) {
      return super.buildAPIClient(apiId, apiKey, timeout);
    } else {
      APIClient apiClient = mock(APIClient.class);
      try {
        if (throwException == null) {
          when(apiClient.getAPIUser()).thenReturn(apiUser);
        } else {
          doThrow(throwException).when(apiClient).getAPIUser();
        }
        return apiClient;
      } catch (APIException | IOException e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  @Test
  public void testExec() {
    setOutPrintSteam(output);
    Namespace namespace = new APIUserCommandNamespaceBuilder().build();
    assertEquals(0, exec(namespace));
    assertEquals(Json.dumps(new APIUserWrapper(apiUser), true), output.getLines().get(0));
    throwException = new APIException(10001, "unauthorized");
    MockPrintStream err = new MockPrintStream();
    setErrPrintStream(err);
    assertEquals(-1, exec(namespace));
    assertEquals("error code: 10001, message: unauthorized", err.getLines().get(0));

    err.clear();
    namespace = new APIUserCommandNamespaceBuilder().setProxy("wrongSocks5proxy").build();
    assertEquals(-1, exec(namespace));
    assertEquals("invalid proxy", err.getLines().get(0));
  }

  @Test
  public void testBuildAPIClient() {
    useMockAPIClient = false;
    String apiId = UUID.randomUUID().toString().replace("-", "");
    String apiKey = UUID.randomUUID().toString().replace("-", "");
    float timeout = 20;
    APIClient apiClient = buildAPIClient(apiId, apiKey, timeout);
    assertTrue(apiClient instanceof DefaultAPIClient);
  }

  @Test
  public void testGetCommand() {
    assertEquals("api-user", getCommand());
  }
}
