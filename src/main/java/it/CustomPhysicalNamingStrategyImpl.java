package it;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

import java.io.Serializable;
import java.util.Locale;

public class CustomPhysicalNamingStrategyImpl extends PhysicalNamingStrategyStandardImpl implements Serializable {
    public static final CustomPhysicalNamingStrategyImpl INSTANCE = new CustomPhysicalNamingStrategyImpl();

    public CustomPhysicalNamingStrategyImpl() {
    }

    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
        return new Identifier(addUnderscores(name.getText()), name.isQuoted());
    }

    public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment context) {
        return new Identifier(addUnderscores(name.getText()), name.isQuoted());
    }

    private static String addUnderscores(String name) {
        StringBuilder buf = new StringBuilder(name.replace('.', '_'));

        for(int i = 1; i < buf.length() - 1; ++i) {
            if(Character.isLowerCase(buf.charAt(i - 1)) && Character.isUpperCase(buf.charAt(i)) &&
               Character.isLowerCase(buf.charAt(i + 1))) {
                    buf.insert(i++, '_');
            }
        }

        return buf.toString().toLowerCase(Locale.ROOT);
    }
}