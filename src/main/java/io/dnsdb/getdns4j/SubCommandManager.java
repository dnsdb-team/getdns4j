package io.dnsdb.getdns4j;

import java.util.ArrayList;
import java.util.List;
import net.sourceforge.argparse4j.inf.Namespace;


/**
 * <code>SubCommandManager</code>类比表示子命令管理器。
 *
 * @author Remonsan
 * @version 1.0
 */
public class SubCommandManager {

  private List<SubCommand> subCommands = new ArrayList<>();

  public SubCommandManager add(SubCommand subCommand) {
    subCommands.add(subCommand);
    return this;
  }

  public void exec(String command, Namespace namespace) {
    for (SubCommand subCommand : subCommands) {
      if (subCommand.getCommand().equals(command)) {
        subCommand.exec(namespace);
      }
    }
  }

}
