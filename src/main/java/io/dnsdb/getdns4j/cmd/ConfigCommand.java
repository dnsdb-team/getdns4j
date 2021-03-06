package io.dnsdb.getdns4j.cmd;

import io.dnsdb.getdns4j.Settings;
import io.dnsdb.getdns4j.utils.Json;
import java.io.IOException;
import java.io.PrintStream;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import net.sourceforge.argparse4j.inf.Subparsers;

/**
 * <code>ConfigCommand</code>类表示<code>config</code>子命令。
 *
 * @author Remonsan
 * @version 1.0
 */
public class ConfigCommand extends SubCommand {

  private PrintStream outPrintSteam = System.out;

  public ConfigCommand(Subparsers subparsers) {
    super(subparsers);
    Settings settings = Settings.newInstance();
    Subparser configParser = subparsers.addParser("config").help("set configurations")
        .description("set configurations").defaultHelp(true);
    configParser.addArgument("--show", "-s").action(Arguments.storeTrue()).setDefault(false)
        .help("show current configuration");
    configParser.addArgument("-i", "--api-id").help("set the default API ID")
        .setDefault(settings.getApiId());
    configParser.addArgument("-k", "--api-key").help("set the default API Key")
        .setDefault(settings.getApiKey());
    configParser.addArgument("--api-url").help("set the default API URL")
        .setDefault(settings.getApiUrl());
    configParser.addArgument("--proxy").help("set the default proxy")
        .setDefault(settings.getProxy());
    configParser.addArgument("-T", "--timeout").help("set the default socket timeout")
        .setDefault(settings.getTimeout());
  }

  @Override
  public String getCommand() {
    return "config";
  }

  @Override
  public int exec(Namespace namespace) {
    Settings settings = Settings.newInstance();
    if (namespace.getBoolean("show")) {
      getOutPrintSteam().println(Json.dumps(settings.toMap(), true));
      return 0;
    }
    String apiId = namespace.get("api_id");
    String apiKey = namespace.get("api_key");
    float timeout = namespace.get("timeout");
    String proxy = namespace.get("proxy");
    String apiUrl = namespace.get("api_url");
    settings.setApiId(apiId);
    settings.setApiKey(apiKey);
    settings.setTimeout(timeout);
    settings.setProxy(proxy);
    settings.setApiUrl(apiUrl);
    try {
      settings.save();
    } catch (IOException e) {
      getErrPrintStream().println(e.getMessage());
      return -1;
    }
    return 0;
  }

  public PrintStream getOutPrintSteam() {
    return outPrintSteam;
  }

  public ConfigCommand setOutPrintSteam(PrintStream outPrintSteam) {
    this.outPrintSteam = outPrintSteam;
    return this;
  }
}
