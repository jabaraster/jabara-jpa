/**
 * 
 */
package jabara.jpa.entity;

import java.util.Date;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * @author jabaraster
 */
@StaticMetamodel(EntityBase.class)
public class EntityBase_ {
    public static volatile SingularAttribute<EntityBase, Long> id;
    public static volatile SingularAttribute<EntityBase, Date> created;
    public static volatile SingularAttribute<EntityBase, Date> updated;
}
