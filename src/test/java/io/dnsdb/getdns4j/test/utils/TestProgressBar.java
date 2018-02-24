package io.dnsdb.getdns4j.test.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import com.google.common.collect.Lists;
import io.dnsdb.getdns4j.utils.ProgressBar;
import java.io.PrintStream;
import java.text.NumberFormat;
import java.util.List;
import org.junit.Test;

/**
 * <code>TestProgressBar</code>测试类用于测试{@link io.dnsdb.getdns4j.utils.ProgressBar}。
 *
 * @author Remonsan
 */
public class TestProgressBar {

  private final static double DELTA = 0.001;

  @Test
  public void testUpdate() {
    ProgressBar progressBar = new ProgressBar().setPrintStream(mock(PrintStream.class));
    while (!progressBar.isFinished()) {
      progressBar.update();
    }
    assertEquals(progressBar.getMax(), progressBar.getCurrent());
    progressBar.reset();
    progressBar.setCurrent(100).setMax(100);
    progressBar.update();
    assertEquals(100, progressBar.getCurrent());
    progressBar.reset();
    progressBar.setMax(100000);
    assertEquals(0, progressBar.getCurrentStep());
    progressBar.update();
    assertEquals(0, progressBar.getCurrentStep());
  }

  @Test
  public void testReset() {
    ProgressBar progressBar = new ProgressBar().setPrintStream(mock(PrintStream.class));
    progressBar.update();
    assertEquals(1, progressBar.getCurrent());
    assertEquals(2, progressBar.getCurrentStep());
    assertEquals(0.01d, progressBar.getProgress(), DELTA);
    progressBar.reset();
    assertEquals(0, progressBar.getCurrent());
    assertEquals(0, progressBar.getCurrentStep());
    assertEquals(0, progressBar.getProgress(), DELTA);

  }

  @Test
  public void testGetterGetter() {
    ProgressBar progressBar = new ProgressBar();
    String suffix = "(%(current)s/%(max)s)";
    NumberFormat numberFormat = NumberFormat.getPercentInstance();
    char emptyFill = '-';
    String title = "My Progress";
    int width = 45;
    int max = 120;
    List<Character> phases = Lists.newArrayList('#');
    long current = 5;
    int currentStep = 5;
    double progress = 0.1;
    String barPrefix = " [";
    String barSuffix = "]";
    char cleanChar = 'c';
    PrintStream printStream = mock(PrintStream.class);
    progressBar.setSuffix(suffix);
    progressBar.setProgressFormat(numberFormat);
    progressBar.setEmptyFill(emptyFill);
    progressBar.setTitle(title);
    progressBar.setWidth(width);
    progressBar.setMax(max);
    progressBar.setPhases(phases);
    progressBar.setCurrent(current);
    progressBar.setCurrentStep(currentStep);
    progressBar.setProgress(progress);
    progressBar.setBarPrefix(barPrefix);
    progressBar.setBarSuffix(barSuffix);
    progressBar.setCleanChar(cleanChar);
    progressBar.setPrintStream(printStream);
    assertEquals(suffix, progressBar.getSuffix());
    assertEquals(numberFormat, progressBar.getProgressFormat());
    assertEquals(emptyFill, progressBar.getEmptyFill());
    assertEquals(title, progressBar.getTitle());
    assertEquals(width, progressBar.getWidth());
    assertEquals(max, progressBar.getMax());
    assertArrayEquals(phases.toArray(), progressBar.getPhases().toArray());
    assertEquals(current, progressBar.getCurrent());
    assertEquals(currentStep, progressBar.getCurrentStep());
    assertEquals(progress, progressBar.getProgress(), Double.NaN);
    assertEquals(barPrefix, progressBar.getBarPrefix());
    assertEquals(barSuffix, progressBar.getBarSuffix());
    assertEquals(cleanChar, progressBar.getCleanChar());
    assertEquals(printStream, progressBar.getPrintStream());
  }
}
