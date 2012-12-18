/**
 * 
 */
package jabara.jpa.util;

import jabara.general.IProducer;
import jabara.jpa.PersistenceXmlPropertyNames;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.postgresql.Driver;

/**
 * @author jabaraster
 */
public class SystemPropertyToPostgreJpaPropertiesParser implements IProducer<Map<String, String>> {

    /**
     * 
     */
    public static final String KEY_DATABASE_URL           = "database.url";          //$NON-NLS-1$
    /**
     * 
     */
    public static final String ENV_HIBERNATE_HBM2DDL_AUTO = "HIBERNATE_HBM2DDL_AUTO"; //$NON-NLS-1$

    /**
     * @see jabara.general.IProducer#produce()
     */
    @Override
    public Map<String, String> produce() {
        final String databaseUrl = System.getProperty(KEY_DATABASE_URL);
        if (databaseUrl == null) {
            final Map<String, String> ret = new HashMap<String, String>();
            ret.put(PersistenceXmlPropertyNames.DRIVER, Driver.class.getName());
            return Collections.emptyMap();
        }

        try {
            final URI dbUri = new URI(databaseUrl);

            final String userInfo = dbUri.getUserInfo();
            final Credential credential = parseCredential(userInfo);

            final String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + dbUri.getPath() + ":" + dbUri.getPort(); //$NON-NLS-1$ //$NON-NLS-2$

            final Map<String, String> ret = new HashMap<String, String>();
            ret.put(PersistenceXmlPropertyNames.DRIVER, Driver.class.getName());
            ret.put(PersistenceXmlPropertyNames.JDBC_URL, dbUrl);
            ret.put(PersistenceXmlPropertyNames.JDBC_USER, credential.getUserName());
            ret.put(PersistenceXmlPropertyNames.JDBC_PASSWORD, credential.getPassword());

            final String hbm2ddl = System.getenv(ENV_HIBERNATE_HBM2DDL_AUTO);
            if (hbm2ddl != null) {
                ret.put(PersistenceXmlPropertyNames.Hibernate.HBM2DDL_AUTO, hbm2ddl);
            }

            return ret;

        } catch (final URISyntaxException e) {
            throw createIllegalStateException(databaseUrl);
        }
    }

    private static IllegalStateException createIllegalStateException(final String pDatabaseUrl) {
        throw new IllegalStateException("JVM引数'" + KEY_DATABASE_URL + "'の値が不正です -> " + pDatabaseUrl); //$NON-NLS-1$ //$NON-NLS-2$
    }

    @SuppressWarnings("synthetic-access")
    private static Credential parseCredential(final String pUserInfo) {
        if (pUserInfo == null) {
            return Credential.ANONYMOUS;
        }
        final String[] tokens = pUserInfo.split(":"); //$NON-NLS-1$
        if (tokens.length == 0) {
            return Credential.ANONYMOUS;
        }

        final String userName;
        final String password;
        if (tokens.length < 2) {
            userName = tokens[0];
            password = ""; //$NON-NLS-1$
        } else {
            userName = tokens[0];
            password = tokens[1];
        }
        return new Credential(userName, password);
    }

    private static final class Credential {
        private static final Credential ANONYMOUS = new Credential("", ""); //$NON-NLS-1$//$NON-NLS-2$

        private final String            userName;
        private final String            password;

        Credential(final String pUserName, final String pPassword) {
            this.userName = pUserName;
            this.password = pPassword;
        }

        String getPassword() {
            return this.password;
        }

        String getUserName() {
            return this.userName;
        }

    }

}
