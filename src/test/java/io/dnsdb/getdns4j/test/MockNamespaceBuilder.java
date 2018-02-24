package io.dnsdb.getdns4j.test;

import static org.mockito.Mockito.when;

import com.google.common.base.CaseFormat;
import java.lang.reflect.Field;
import net.sourceforge.argparse4j.inf.Namespace;
import org.mockito.Mockito;

/**
 * <code>MockNamespaceBuilder</code>类用于创建模拟的<code>net.sourceforge.argparse4j.inf.Namespace</code>。
 *
 * @author Remonsan
 * @version 1.0
 */
public class MockNamespaceBuilder {

  public static Namespace build(Object param) {
    Namespace namespace = Mockito.mock(Namespace.class);
    Field[] fields = param.getClass().getDeclaredFields();
    for (Field field : fields) {
      field.setAccessible(true);
      String name = field.getName();
      name = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name);
      try {
        Object value = field.get(param);
        when(namespace.get(name)).thenReturn(field.get(param));
        if (value instanceof Integer) {
          when(namespace.getInt(name)).thenReturn(field.getInt(param));
        } else if (value instanceof Long) {
          when(namespace.getLong(name)).thenReturn((Long) field.get(param));
        } else if (value instanceof Float) {
          when(namespace.getFloat(name)).thenReturn(field.getFloat(param));
        } else if (value instanceof Boolean) {
          when(namespace.getBoolean(name)).thenReturn(field.getBoolean(param));
        } else if (value instanceof String) {
          when(namespace.getString(name)).thenReturn((String) field.get(param));
        }
      } catch (IllegalAccessException ignored) {
      }

    }
    return namespace;
  }
}
