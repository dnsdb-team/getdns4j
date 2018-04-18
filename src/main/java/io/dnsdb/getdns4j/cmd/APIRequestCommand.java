package io.dnsdb.getdns4j.cmd;

import com.google.common.base.Strings;
import io.dnsdb.getdns4j.utils.proxy.InvalidProxyStringException;
import io.dnsdb.getdns4j.utils.proxy.ProxyUtils;
import java.io.PrintStream;
import net.sourceforge.argparse4j.inf.Subparsers;

/**
 * <code>APIRequestCommand</code>抽象类表示用于请求API的命令。
 *
 * @author Remonsan
 * @version 1.0
 */
public abstract class APIRequestCommand extends SubCommand {

  public APIRequestCommand(Subparsers subparsers) {
    super(subparsers);
  }

  protected boolean setProxy(String proxy) {
    PrintStream err = getErrPrintStream();
    if (!Strings.isNullOrEmpty(proxy)) {
      try {
        ProxyUtils.setProxy(proxy);
      } catch (InvalidProxyStringException e) {
        err.println("invalid proxy");
        return false;
      }
    }
    return true;
  }
}
