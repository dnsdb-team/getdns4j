package io.dnsdb.getdns4j;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.ini4j.Config;
import org.ini4j.Ini;
import org.ini4j.Profile;

/**
 * <code>Settings</code>类表示设置。
 *
 * @author Remonsan
 * @version 1.0
 */
public class Settings {

  private Ini ini;
  private static final String CONFIG = System.getProperty("user.home") + "/" + ".getdns";
  private static final Settings settings = new Settings(CONFIG);

  private Settings(String path) {
    try {
      File file = new File(path);
      Config config = new Config();
      config.setEscape(false);
      if (file.exists()) {
        ini = new Ini(new File(path));
      } else {
        ini = new Ini();
      }
      ini.setConfig(config);
    } catch (IOException ignored) {
      ignored.printStackTrace();
    }
  }

  public static Settings newInstance() {
    return settings;
  }

  public void save() {
    try {
      ini.store(new File(CONFIG));
    } catch (IOException e) {
      e.printStackTrace();
    }
    ;
  }

  public String getString(String sectionName, String optionName, String defaultValue) {
    if (ini != null) {
      Profile.Section section = ini.get(sectionName);
      if (section != null) {
        String value = section.get(optionName);
        if (value != null) {
          return value;
        }
      }
    }
    return defaultValue;
  }

  public int getInt(String sectionName, String optionName, int defaultValue) {
    String value = getString(sectionName, optionName, null);
    if (value != null) {
      return Integer.parseInt(value);
    }
    return defaultValue;
  }

  public float getFloat(String sectionName, String optionName, float defaultValue) {
    String value = getString(sectionName, optionName, null);
    if (value != null) {
      return Float.parseFloat(value);
    }
    return defaultValue;
  }

  public String getApiId() {
    return getString("auth", "api-id", "");
  }

  public void setApiId(String apiId) {
    ini.put("auth", "api-id", apiId);
  }

  public String getApiKey() {
    return getString("auth", "api-key", "");
  }

  public void setApiKey(String apiKey) {
    ini.put("auth", "api-key", apiKey);
  }

  public String getApiUrl() {
    return getString("settings", "api-url", "https://api.dnsdb.io");
  }

  public void setApiUrl(String apiUrl) {
    ini.put("settings", "api-url", apiUrl);
  }

  public String getProxy() {
    return getString("settings", "proxy", "");
  }

  public void setProxy(String proxy) {
    ini.put("settings", "proxy", proxy);
  }

  public Float getTimeout() {
    return getFloat("settings", "timeout", 15);
  }

  public void setTimeout(float timeout) {
    ini.put("settings", "timeout", timeout);
  }

  public Map<String, Object> toMap() {
    Map<String, Object> map = new HashMap<>();
    map.put("api_id", settings.getApiId());
    map.put("api_key", settings.getApiKey());
    map.put("proxy", settings.getProxy());
    map.put("api_url", settings.getApiUrl());
    map.put("api_timeout", settings.getTimeout());
    return map;
  }


  public static void main(String[] args) {
    Settings settings = Settings.newInstance();
    System.out.println(settings.getString("auth", "api-id", ""));
    settings.setApiUrl("http://localhost:8000/api");
    settings.save();
  }
}
