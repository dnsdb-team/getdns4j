package io.dnsdb.getdns4j.utils;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import java.text.NumberFormat;
import java.util.List;

/**
 * <code>ProgressBar</code>类用于在控制台中显示进度条。
 *
 * @author Remonsan
 * @version 1.0
 */
public class ProgressBar {

  final List<Character> phases = Lists.newArrayList('▏', '▎', '▍', '▌', '▋', '▊', '▉', '█');
  private String title = "Progress";
  private String suffix;
  private long max;
  private long current = 0;
  private int width = 32;
  private int currentStep = 0;
  private double progress = 0;
  private String barPrefix = " |";
  private String barSuffix = "|";
  private String emptyFill = " ";
  private NumberFormat progressFormat = NumberFormat.getPercentInstance();


  public ProgressBar() {
    this("Progress");
  }

  public ProgressBar(String title) {
    this("Progress", 100);
  }

  private void clearLine() {
    System.out.print("\r");
  }

  public ProgressBar(String title, long max) {
    this.title = title;
    this.max = max;
    progressFormat.setMinimumFractionDigits(2);
  }


  public boolean isFinished() {
    return current == max;
  }

  public void update() {
    if (current > max) {
      return;
    }
    current++;
    progress = current * 1.0 / max;
    int maxStep = width * phases.size();
    int nextStep = (int) (current * 1.0 / max * maxStep);
    if (nextStep == currentStep) {
      return;
    }
    currentStep = nextStep;
    clearLine();
    System.out.print(title);
    System.out.print(barPrefix);
    int filledWidth = 0;
    for (int i = 0; i < currentStep / phases.size(); i++) {
      System.out.print(phases.get(phases.size() - 1));
      filledWidth++;
    }
    int phaseIndex = currentStep % phases.size() - 1;
    if (phaseIndex > 0) {
      System.out.print(phases.get(phaseIndex));
      filledWidth++;
    }
    for (int i = 0; i < width - filledWidth; i++) {
      System.out.print(emptyFill);
    }
    System.out.print(barSuffix);
    String suffixContent = Strings.nullToEmpty(suffix);
    suffixContent = suffixContent.replace("%(max)s", max + "")
        .replace("%(current)s", current + "")
        .replace("%(progress)s", progressFormat.format(progress));
    System.out.print(suffixContent);
  }

  public String getTitle() {
    return title;
  }

  public ProgressBar setTitle(String title) {
    this.title = title;
    return this;
  }

  public String getSuffix() {
    return suffix;
  }

  public ProgressBar setSuffix(String suffix) {
    this.suffix = suffix;
    return this;
  }

  public long getMax() {
    return max;
  }

  public ProgressBar setMax(long max) {
    this.max = max;
    return this;
  }

  public long getCurrent() {
    return current;
  }

  public ProgressBar setCurrent(long current) {
    this.current = current;
    return this;
  }

  public int getWidth() {
    return width;
  }

  public ProgressBar setWidth(int width) {
    this.width = width;
    return this;
  }

  public int getCurrentStep() {
    return currentStep;
  }

  public ProgressBar setCurrentStep(int currentStep) {
    this.currentStep = currentStep;
    return this;
  }

  public double getProgress() {
    return progress;
  }

  public ProgressBar setProgress(double progress) {
    this.progress = progress;
    return this;
  }

  public String getBarPrefix() {
    return barPrefix;
  }

  public ProgressBar setBarPrefix(String barPrefix) {
    this.barPrefix = barPrefix;
    return this;
  }

  public String getBarSuffix() {
    return barSuffix;
  }

  public ProgressBar setBarSuffix(String barSuffix) {
    this.barSuffix = barSuffix;
    return this;
  }

  public String getEmptyFill() {
    return emptyFill;
  }

  public ProgressBar setEmptyFill(String emptyFill) {
    this.emptyFill = emptyFill;
    return this;
  }

  public NumberFormat getProgressFormat() {
    return progressFormat;
  }

  public ProgressBar setProgressFormat(NumberFormat progressFormat) {
    this.progressFormat = progressFormat;
    return this;
  }
}
