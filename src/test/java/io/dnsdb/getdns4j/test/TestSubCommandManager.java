package io.dnsdb.getdns4j.test;

import static org.mockito.Mockito.mock;

import io.dnsdb.getdns4j.cmd.NoSuchCommandExecException;
import io.dnsdb.getdns4j.cmd.SubCommand;
import io.dnsdb.getdns4j.cmd.SubCommandManager;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparsers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * <code>TestSubCommandManager</code>测试类用于测试{@link SubCommandManager}。
 *
 * @author Remonsan
 */
public class TestSubCommandManager {

  private SubCommandManager manager;

  @Before
  public void setup() {
    manager = new SubCommandManager();
    manager.add(new MockSubCommand(mock(Subparsers.class)));
  }


  @Test
  public void testExec() {
    Assert.assertEquals(5, manager.exec("mock", mock(Namespace.class)));
  }

  @Test(expected = NoSuchCommandExecException.class)
  public void testExecWithException() {
    manager.exec("test", mock(Namespace.class));
  }

  class MockSubCommand extends SubCommand {

    public MockSubCommand(Subparsers subparsers) {
      super(subparsers);
    }

    @Override
    public String getCommand() {
      return "mock";
    }

    @Override
    public int exec(Namespace namespace) {
      return 5;
    }
  }

}
