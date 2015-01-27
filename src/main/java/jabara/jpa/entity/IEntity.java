/**
 * 
 */
package jabara.jpa.entity;

import java.util.Date;

/**
 * @param <E> エンティティの型.
 * @author jabaraster
 */
public interface IEntity<E extends IEntity<E>> {

    /**
     * @return 生成日.
     */
    Date getCreated();

    /**
     * @return ID値.
     * @throws IllegalStateException 永続化されていない場合.
     */
    Id<E> getId();

    /**
     * @return 最終更新日.
     */
    Date getUpdated();
}
