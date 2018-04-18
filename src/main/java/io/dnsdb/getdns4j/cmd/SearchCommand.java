package io.dnsdb.getdns4j.cmd;

import com.google.common.base.Strings;
import io.dnsdb.getdns4j.Settings;
import io.dnsdb.getdns4j.format.DNSRecordCSVFormatter;
import io.dnsdb.getdns4j.format.DNSRecordCustomFormatter;
import io.dnsdb.getdns4j.format.DNSRecordFormatter;
import io.dnsdb.getdns4j.format.DNSRecordJsonFormatter;
import io.dnsdb.getdns4j.utils.ProgressBar;
import io.dnsdb.sdk.APIClient;
import io.dnsdb.sdk.APIClientBuilder;
import io.dnsdb.sdk.APIManager;
import io.dnsdb.sdk.DNSRecord;
import io.dnsdb.sdk.DNSRecordResult;
import io.dnsdb.sdk.Query;
import io.dnsdb.sdk.exceptions.APIException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.ArgumentGroup;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import net.sourceforge.argparse4j.inf.Subparsers;
import org.apache.http.client.config.RequestConfig;

/**
 * <code>SearchCommand</code>类表示<code>search</code>子命令。
 *
 * @author Remonsan
 * @version 1.0
 */
public class SearchCommand extends APIRequestCommand {

  private ProgressBar progressBar = new ProgressBar();

  public SearchCommand(Subparsers subparsers) {
    super(subparsers);
    Settings settings = Settings.newInstance();
    String apiUrl = settings.getApiUrl();
    String apiId = settings.getApiId();
    String apiKey = settings.getApiKey();
    String proxy = settings.getProxy();
    float timeout = settings.getTimeout();
    String format_help = "set custom output format. #{host} represents DNS record\"s host, #{type} represents DNS record\"s type, #{value} represents DNS record\"s value. For example: -f \"#{host},#{type},#{value}\"";
    String proxy_help = "set proxy. HTTP proxy: \"http://user:pass@host:port/\", SOCKS5 proxy: \"socks5://user:pass@host:port\"";
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
    searchParser.addArgument("-T", "--timeout").help("set the socket timeout").setDefault(timeout)
        .type(Float.class);
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
    PrintStream errPrintStream = getErrPrintStream();
    APIManager.API_BASE_URL = namespace.getString("api_url");
    String proxy = namespace.get("proxy");
    if (!setProxy(proxy)) {
      return -1;
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
    float timeout = namespace.getFloat("timeout");
    APIClient client = buildAPIClient(apiId, apiKey, timeout);
    Query query = new Query().setDomain(domain).setHost(host).setIp(ip).setType(type)
        .setValueDomain(valueDomain).setValueHost(valueHost).setValueIp(valueIp).setEmail(email);
    PrintStream outputStream = null;
    DNSRecordFormatter formatter;
    if (namespace.getBoolean("csv")) {
      formatter = new DNSRecordCSVFormatter();
    } else if (!Strings.isNullOrEmpty(customFormat)) {
      formatter = new DNSRecordCustomFormatter(customFormat);
    } else {
      formatter = new DNSRecordJsonFormatter();
    }
    try {
      outputStream = createOutputStream(output);
      DNSRecordResult result;
      if (all) {
        result = client.scan(query, pageSize);
      } else {
        result = client.search(query, page, pageSize);
      }
      ProgressBar progressBar = null;
      if (!outputStream.equals(System.out)) {
        progressBar = getProgressBar();
        progressBar.setTitle("Receiving");
        progressBar.setMax(result.size());
      }
      long count = 0;
      for (DNSRecord record : result) {
        if (max != null && count >= max) {
          break;
        }
        String data = formatter.format(record);
        outputStream.println(data);
        if (progressBar != null) {
          progressBar.update();
        }
        count++;
      }
    } catch (APIException | IOException e) {
      errPrintStream.println(e.getMessage());
      return -1;
    } finally {
      if (outputStream != null && !outputStream.equals(System.out)) {
        outputStream.close();
      }
    }
    return 0;
  }

  protected APIClient buildAPIClient(String apiId, String apiKey, float timeout) {
    int timeoutMilliseconds = (int) (timeout * 1000);
    RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(timeoutMilliseconds)
        .setConnectionRequestTimeout(timeoutMilliseconds).setSocketTimeout(timeoutMilliseconds)
        .build();
    return new APIClientBuilder(apiId, apiKey).setRequestConfig(requestConfig).build();
  }

  protected PrintStream createOutputStream(String output) throws FileNotFoundException {
    if (output.equals("-")) {
      return System.out;
    } else {
      return new PrintStream(new FileOutputStream(new File(output)));
    }
  }

  public ProgressBar getProgressBar() {
    return progressBar;
  }

  public SearchCommand setProgressBar(ProgressBar progressBar) {
    this.progressBar = progressBar;
    return this;
  }

}
