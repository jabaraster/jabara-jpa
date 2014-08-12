package jabara.jpa.db_kind;

import java.io.Serializable;

/**
 * @author jabaraster
 * 
 */
public interface DbKind extends Serializable {
    /**
     * @return -
     */
    String getDialectClassName();

    /**
     * @return -
     */
    String getDriverClassName();
}