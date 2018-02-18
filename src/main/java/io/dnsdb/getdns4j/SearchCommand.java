package io.dnsdb.getdns4j;

import com.google.common.base.Strings;
import io.dnsdb.getdns4j.format.DNSRecordCSVFormat;
import io.dnsdb.getdns4j.format.DNSRecordCustomFormat;
import io.dnsdb.getdns4j.format.DNSRecordFormat;
import io.dnsdb.getdns4j.format.DNSRecordJsonFormat;
import io.dnsdb.getdns4j.utils.InvalidProxyStringException;
import io.dnsdb.getdns4j.utils.ProgressBar;
import io.dnsdb.getdns4j.utils.proxy.ProxyUtils;
import io.dnsdb.sdk.APIClient;
import io.dnsdb.sdk.APIClientBuilder;
import io.dnsdb.sdk.APIManager;
import io.dnsdb.sdk.DNSRecord;
import io.dnsdb.sdk.DNSRecordResult;
import io.dnsdb.sdk.Query;
import io.dnsdb.sdk.exceptions.APIException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.ArgumentGroup;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import net.sourceforge.argparse4j.inf.Subparsers;

/**
 * <code>SearchCommand</code>类表示<code>search</code>子命令。
 *
 * @author Remonsan
 * @version 1.0
 */
public class SearchCommand extends SubCommand {

  public SearchCommand(Subparsers subparsers) {
    super(subparsers);
    Settings settings = Settings.newInstance();
    String apiUrl = settings.getString("settings", "api-url", "https://api.dnsdb.io");
    String apiId = settings.getString("auth", "api-id", "");
    String apiKey = settings.getString("auth", "api-key", "");
    String proxy = settings.getString("settings", "proxy", "");
    String format_help = "set custom output format. #{host} represents DNS record\"s host, #{type} represents DNS record\"s type, #{value} represents DNS record\"s value. For example: -f \"#{host},#{type},#{value}\"";
    String proxy_help = "set proxy. HTTP proxy: \"http://user:pass@host:port/\", SOCKS5 proxy: \"socks5://user:pass@host:port\"";
    if (!Strings.isNullOrEmpty(proxy)) {
      proxy_help += String.format(", current proxy is \"%s\"", proxy);
    }
    Subparser searchParser = subparsers.addParser("search").help("search DNS records")
        .description("search DNS records").defaultHelp(true);
    searchParser.addArgument("-o", "--output").setDefault("-")
        .help("specify output file, \"-\" represents stdout");
    searchParser.addArgument("-j", "--json").action(Arguments.storeTrue()).setDefault(true)
        .help("set JSON format. This is the default option");
    searchParser.addArgument("-c", "--csv").action(Arguments.storeTrue()).setDefault(false)
        .help("set CSV format");
    searchParser.addArgument("-f", "--format").help(format_help);
    searchParser.addArgument("-m", "--max").type(Long.class)
        .help("set the maximum number of search results to output");
    searchParser.addArgument("-p", "--proxy").help(proxy_help).setDefault(proxy);
    searchParser.addArgument("--api-url").help(String.format("set API URL, default \"%s\"", apiUrl))
        .setDefault(apiUrl);
    ArgumentGroup searchGroup = searchParser.addArgumentGroup("search options");
    searchGroup.addArgument("-d", "--domain").help("search by domain");
    searchGroup.addArgument("-H", "--host").help("search by host");
    searchGroup.addArgument("--ip").help("search by IP");
    searchGroup.addArgument("-t", "--type").help("search by DNS type");
    searchGroup.addArgument("--value-domain").help("search by value_domain");
    searchGroup.addArgument("--value-host").help("search by value_host");
    searchGroup.addArgument("--value-ip").help("search by value_ip");
    searchGroup.addArgument("--email").help("search by email");
    searchGroup.addArgument("--page").help("set query page number").type(Integer.class)
        .setDefault(1);
    searchGroup.addArgument("--page-size").help("set query page size").type(Integer.class)
        .setDefault(50);
    searchGroup.addArgument("-a", "--all").action(Arguments.storeTrue()).setDefault(false)
        .help("retrieve all results, it will ignored [--page] option");
    ArgumentGroup authGroup = searchParser.addArgumentGroup("authentication options");
    authGroup.addArgument("--api-id", "-i").help("set API ID").setDefault(apiId);
    authGroup.addArgument("--api-key", "-k").help("set API key").setDefault(apiKey);
  }

  @Override
  public String getCommand() {
    return "search";
  }


  @Override
  public int exec(Namespace namespace) {
    APIManager.API_BASE_URL = namespace.getString("api_url");
    String proxy = namespace.get("proxy");
    if (!Strings.isNullOrEmpty(proxy)) {
      try {
        ProxyUtils.setProxy(proxy);
      } catch (InvalidProxyStringException e) {
        System.err.println("invalid proxy");
        return -1;
      }
    }
    String apiId = namespace.getString("api_id");
    String apiKey = namespace.getString("api_key");
    String domain = namespace.getString("domain");
    String host = namespace.getString("host");
    String ip = namespace.getString("ip");
    String type = namespace.getString("type");
    String valueDomain = namespace.getString("value_domain");
    String valueHost = namespace.getString("value_host");
    String valueIp = namespace.getString("value_ip");
    String email = namespace.getString("email");
    boolean all = namespace.getBoolean("all");
    int page = namespace.getInt("page");
    int pageSize = namespace.getInt("page_size");
    String output = namespace.getString("output");
    Long max = namespace.get("max");
    String customFormat = namespace.getString("format");
    APIClient client = new APIClientBuilder(apiId, apiKey).build();
    Query query = new Query().setDomain(domain).setHost(host).setIp(ip).setType(type)
        .setValueDomain(valueDomain).setValueHost(valueHost).setValueIp(valueIp).setEmail(email);
    OutputStream outputStream = System.out;
    DNSRecordFormat format;
    if (namespace.getBoolean("csv")) {
      format = new DNSRecordCSVFormat();
    } else if (!Strings.isNullOrEmpty(customFormat)) {
      format = new DNSRecordCustomFormat(customFormat);
    } else {
      format = new DNSRecordJsonFormat();
    }
    try {
      if (!output.equals("-")) {
        outputStream = new FileOutputStream(new File(output));
      }
      DNSRecordResult result;
      if (all) {
        result = client.scan(query, pageSize);
      } else {
        result = client.search(query, page, pageSize);
      }
      ProgressBar bar = null;
      if (!outputStream.equals(System.out)) {
        bar = new ProgressBar("Receiving", result.size());
        bar.setSuffix("(%(current)s/%(max)s)");
      }
      long count = 0;
      for (DNSRecord record : result) {
        if (max != null && count >= max) {
          break;
        }
        String data = format.format(record) + System.lineSeparator();
        outputStream.write(data.getBytes());
        if (bar != null) {
          bar.update();
        }
        count++;
      }
    } catch (APIException | IOException e) {
      System.err.println(e.getMessage());
      return -1;
    } finally {
      if (!outputStream.equals(System.out)) {
        try {
          outputStream.close();
        } catch (IOException e) {
          e.printStackTrace();

        }
      }
    }
    return 0;
  }
}