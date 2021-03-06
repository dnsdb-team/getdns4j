package io.dnsdb.getdns4j.cmd;

import java.io.PrintStream;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparsers;

/**
 * <code>SubCommand</code>类表示一个子命令。
 *
 * @author Remonsan
 * @version 1.0
 */
public abstract class SubCommand {

  private PrintStream errPrintStream = System.err;

  public SubCommand(Subparsers subparsers) {
  }

  public abstract String getCommand();

  public abstract int exec(Namespace namespace);

  public PrintStream getErrPrintStream() {
    return errPrintStream;
  }

  public SubCommand setErrPrintStream(PrintStream errPrintStream) {
    this.errPrintStream = errPrintStream;
    return this;
  }
}
