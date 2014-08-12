package jabara.jpa.db_kind;

/**
 * @author jabaraster
 */
public final class PostgreSQL9 implements DbKind {
    private static final long       serialVersionUID   = 746029894468027021L;
    /**
     * 
     */
    public static final String      DRIVER_CLASS_NAME  = "org.postgresql.Driver";                   //$NON-NLS-1$
    /**
     * 
     */
    public static final String      DIALECT_CLASS_NAME = "org.hibernate.dialect.PostgreSQL9Dialect"; //$NON-NLS-1$

    /**
     * 
     */
    public static final PostgreSQL9 INSTANCE           = new PostgreSQL9();

    private PostgreSQL9() {
        // 処理なし
    }

    /**
     * @see jabara.jpa.db_kind.DbKind#getDialectClassName()
     */
    @Override
    public String getDialectClassName() {
        return DIALECT_CLASS_NAME;
    }

    /**
     * @see jabara.jpa.db_kind.DbKind#getDriverClassName()
     */
    @Override
    public String getDriverClassName() {
        return DRIVER_CLASS_NAME;
    }
}