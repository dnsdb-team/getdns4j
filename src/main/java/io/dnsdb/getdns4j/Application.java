package io.dnsdb.getdns4j;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparsers;

/**
 * <code>Application</code>类是程序的入口类。
 *
 * @author Remonsan
 * @version 1.0
 */
public class Application {

  public static void main(String[] args) {
    SubCommandManager subCommands = new SubCommandManager();
    ArgumentParser parser = getParser(subCommands);
    Namespace namespace = parser.parseArgsOrFail(args);
    subCommands.exec(args[0], namespace);
  }

  public static ArgumentParser getParser(SubCommandManager subCommands) {
    ArgumentParser parser = ArgumentParsers.newFor("getdns4j").build().version("${prog} 0.0.1")
        .description("search DNS records using the DNSDB Web API").defaultHelp(true);
    parser.addArgument("--version", "-V").action(Arguments.version()).help("show version");
    Subparsers subparsers = parser.addSubparsers().title("subcommands")
        .description("valid subcommands").help("additional help")
        .metavar("COMMAND");
    subCommands.add(new SearchCommand(subparsers)).add(new APIUserCommand(subparsers))
        .add(new ConfigCommand(subparsers));
    return parser;
  }

}
