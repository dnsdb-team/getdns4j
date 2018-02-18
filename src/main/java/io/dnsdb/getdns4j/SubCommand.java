package io.dnsdb.getdns4j;

import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparsers;

/**
 * <code>SubCommand</code>类比表示一个子命令。
 *
 * @author Remonsan
 * @version 1.0
 */
public abstract class SubCommand {

  public SubCommand(Subparsers subparsers) {
  }

  public abstract String getCommand();

  public abstract int exec(Namespace namespace);
}
