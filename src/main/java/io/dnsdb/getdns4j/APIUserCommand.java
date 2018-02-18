package io.dnsdb.getdns4j;

import io.dnsdb.getdns4j.utils.Json;
import io.dnsdb.sdk.APIClient;
import io.dnsdb.sdk.APIClientBuilder;
import io.dnsdb.sdk.APIManager;
import io.dnsdb.sdk.APIUser;
import io.dnsdb.sdk.exceptions.APIException;
import java.io.IOException;
import net.sourceforge.argparse4j.inf.ArgumentGroup;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import net.sourceforge.argparse4j.inf.Subparsers;

/**
 * <code>APIUserCommand</code>类表示<code>api-user</code>子命令。
 *
 * @author Remonsan
 * @version 1.0
 */
public class APIUserCommand extends SubCommand {

  public APIUserCommand(Subparsers subparsers) {
    super(subparsers);
    Settings settings = Settings.newInstance();
    String apiUrl = settings.getString("settings", "api-url", "https://api.dnsdb.io");
    String apiId = settings.getString("auth", "api-id", "");
    String apiKey = settings.getString("auth", "api-key", "");
    float timeout = settings.getFloat("settings", "timeout", 15);
    Subparser apiUserParser = subparsers.addParser("api-user").help("get API User information")
        .description("get API User information").defaultHelp(true);
    apiUserParser.addArgument("--api-url").help("set API URL").setDefault(apiUrl);
    apiUserParser.addArgument("--timeout", "-T").type(Float.class)
        .help("set the default socket timeout").type(Float.class)
        .setDefault(timeout);
    ArgumentGroup authGroup = apiUserParser.addArgumentGroup("authentication options");
    authGroup.addArgument("--api-id", "-i").help("set API ID").setDefault(apiId);
    authGroup.addArgument("--api-key", "-k").help("set API key").setDefault(apiKey);
  }

  @Override
  public String getCommand() {
    return "api-user";
  }

  @Override
  public int exec(Namespace namespace) {
    APIManager.API_BASE_URL = namespace.getString("api_url");
    String apiId = namespace.getString("api_id");
    String apiKey = namespace.getString("api_key");
    APIClient client = new APIClientBuilder(apiId, apiKey).build();
    try {
      APIUser apiUser = client.getAPIUser();
      System.out.println(Json.dumps(apiUser, true));
    } catch (APIException | IOException e) {
      System.err.println(e.getMessage());
      return -1;
    }
    return 0;
  }
}
