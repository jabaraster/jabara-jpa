package jabara.jpa.db_kind;

/**
 * @author jabaraster
 */
public final class H2 implements DbKind {
    private static final long  serialVersionUID   = -368658082394652394L;

    /**
     * 
     */
    public static final String DRIVER_CLASS_NAME  = "org.h2.Driver";                  //$NON-NLS-1$
    /**
     * 
     */
    public static final String DIALECT_CLASS_NAME = "org.hibernate.dialect.H2Dialect"; //$NON-NLS-1$

    /**
     * 
     */
    public static final H2     INSTANCE           = new H2();

    private H2() {
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