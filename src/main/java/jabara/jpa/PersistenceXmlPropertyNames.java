/**
 * 
 */
package jabara.jpa;

/**
 * @author jabaraster
 */
public final class PersistenceXmlPropertyNames {

    /**
     * 
     */
    public static final String DRIVER        = "javax.persistence.driver";       //$NON-NLS-1$
    /**
     * 
     */
    public static final String JDBC_URL      = "javax.persistence.jdbc.url";     //$NON-NLS-1$
    /**
     * 
     */
    public static final String JDBC_USER     = "javax.persistence.jdbc.user";    //$NON-NLS-1$
    /**
     * 
     */
    public static final String JDBC_PASSWORD = "javax.persistence.jdbc.password"; //$NON-NLS-1$

    private PersistenceXmlPropertyNames() {
        // 処理なし
    }

    /**
     * @author jabaraster
     */
    public static final class Hibernate {

        /**
         * hibernate-entitymanagerを使っているときに、JPAエンティティの定義に従ってDBのスキーマを作り替える場合、persistence.xmlにこの名前のプロパティを定義します. <br>
         * 値はcreate-drop、createなどを指定します. <br>
         */
        public static final String HBM2DDL_AUTO                   = "hibernate.hbm2ddl.auto"; //$NON-NLS-1$
        /**
         * JPAエンティティの定義に従ってDBのスキーマを作り替える場合に指定するプロパティの値です. <br>
         */
        public static final String HBM2DDL_AUTO_VALUE_CREATE_DROP = "create-drop";            //$NON-NLS-1$
        /**
         * JPAエンティティの定義に従ってDBのスキーマを作り替える場合に指定するプロパティの値です. <br>
         */
        public static final String HBM2DDL_AUTO_VALUE_CRAETE      = "create";                 //$NON-NLS-1$
        /**
         * JPAエンティティの定義に従ってDBのスキーマを作り替える場合に指定するプロパティの値です. <br>
         */
        public static final String HBM2DDL_AUTO_VALUE_NONE        = "none";                   //$NON-NLS-1$
        /**
         * hibernate-entitymanagerを使っているときに、persistence.xmlにこのプロパティ名で方言(Dialect)クラスを指定します. <br>
         */
        public static final String DIALECT                        = "hibernate.dialect";      //$NON-NLS-1$

        /**
         * C3P0をコネクションプーリングに使用する場合の、プール内の最小接続数. <br>
         * C3P0を使う場合、Mavenを使っているなら次の依存を追加する必要があります. <br>
         * <code>
         * <dependency>
         *     <groupId>org.hibernate</groupId>
         *     <artifactId>hibernate-c3p0</artifactId>
         *     <version>4.3.6.Final</version>
         * </dependency>
         * </code>
         */
        public static final String C3P0_CONNECTION_POOL_MIN_SIZE  = "hibernate.c3p0.min_size"; //$NON-NLS-1$
        /**
         * C3P0をコネクションプーリングに使用する場合の、プール内の最大接続数. <br>
         * 
         * @see #C3P0_CONNECTION_POOL_MIN_SIZE
         */
        public static final String C3P0_CONNECTION_POOL_MAX_SIZE  = "hibernate.c3p0.max_size"; //$NON-NLS-1$

        private Hibernate() {
            // 処理なし
        }
    }
}
