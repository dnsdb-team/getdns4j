package io.dnsdb.getdns4j.test;

import io.dnsdb.getdns4j.Application;
import io.dnsdb.getdns4j.cmd.SubCommandManager;
import org.junit.Assert;
import org.junit.Test;

/**
 * <code>TestApplication</code>测试类用于测试{@link Application}
 *
 * @author Remonsan
 */
public class TestApplication {

  @Test
  public void testGetParser() {
    SubCommandManager commandManager = new SubCommandManager();
    Application.getParser(commandManager);
    Assert.assertEquals(3, commandManager.getSubCommands().size());
    Assert.assertEquals("search", commandManager.getSubCommands().get(0).getCommand());
    Assert.assertEquals("api-user", commandManager.getSubCommands().get(1).getCommand());
    Assert.assertEquals("config", commandManager.getSubCommands().get(2).getCommand());
  }

}
