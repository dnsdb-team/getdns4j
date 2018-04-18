package io.dnsdb.getdns4j.cmd;

/**
 * <code>NoSuchCommandExecException</code>异常表示没有命令执行。
 *
 * @author Remonsan
 * @version 1.0
 */
public class NoSuchCommandExecException extends RuntimeException {

  public NoSuchCommandExecException() {
    super("No such command to execute");
  }
}
