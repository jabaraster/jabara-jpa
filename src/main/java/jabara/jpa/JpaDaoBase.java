/**
 * 
 */
package jabara.jpa;

import jabara.general.ArgUtil;
import jabara.general.NotFound;
import jabara.general.Sort;
import jabara.general.SortRule;
import jabara.jpa.entity.IEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

/**
 * @author jabaraster
 */
public class JpaDaoBase implements Serializable {
    private static final long            serialVersionUID = -6876768771003819068L;

    /**
     * 
     */
    public static final Predicate[]      EMPTY_PREDICATE  = {};

    /**
     * 
     */
    protected final EntityManagerFactory emf;

    /**
     * @param pEntityManagerFactory -
     */
    public JpaDaoBase(final EntityManagerFactory pEntityManagerFactory) {
        ArgUtil.checkNull(pEntityManagerFactory, "pEntityManagerFactory"); //$NON-NLS-1$
        this.emf = pEntityManagerFactory;
    }

    /**
     * ID値が一致するエンティティを検索して返します.
     * 
     * @param pEntityType 結果のエンティティの型.
     * @param pId ID値.
     * @param <E> 結果エンティティオブジェクトの型.
     * @return エンティティオブジェクト.
     * @throws NotFound 該当エンティティがない場合.
     */
    public <E extends IEntity> E findByIdCore(final Class<E> pEntityType, final long pId) throws NotFound {
        ArgUtil.checkNull(pEntityType, "pEntityType"); //$NON-NLS-1$
        final E ret = getEntityManager().find(pEntityType, Long.valueOf(pId));
        if (ret == null) {
            throw NotFound.GLOBAL;
        }
        return ret;
    }

    /**
     * @return {@link EntityManager}オブジェクト.
     */
    public EntityManager getEntityManager() {
        return this.emf.createEntityManager();
    }

    /**
     * <<<<<<< HEAD =======
     * 
     * @param pSort -
     * @param pCriteriaBuilder -
     * @param pPath -
     * @return -
     */
    public static Order convertOrder(final Sort pSort, final CriteriaBuilder pCriteriaBuilder, final Path<?> pPath) {
        ArgUtil.checkNull(pSort, "pSort"); //$NON-NLS-1$
        ArgUtil.checkNull(pCriteriaBuilder, "pCriteriaBuilder"); //$NON-NLS-1$
        ArgUtil.checkNull(pPath, "pPath"); //$NON-NLS-1$

        if (pSort.getSortRule() == SortRule.ASC) {
            return pCriteriaBuilder.asc(pPath.get(pSort.getColumnName()));
        }
        return pCriteriaBuilder.desc(pPath.get(pSort.getColumnName()));
    }

    /**
     * >>>>>>> 47229b324574a7d5104ba5556830fcf65d29a1bc ソート条件を型変換します.
     * 
     * @param pSort -
     * @param pCriteriaBuilder -
     * @param pPath -
     * @return 変換後のソート条件.
     */
    public static List<Order> convertOrders(final Iterable<Sort> pSort, final CriteriaBuilder pCriteriaBuilder, final Path<?> pPath) {
        ArgUtil.checkNull(pSort, "pSort"); //$NON-NLS-1$
        ArgUtil.checkNull(pCriteriaBuilder, "pCriteriaBuilder"); //$NON-NLS-1$

        final List<Order> ret = new ArrayList<Order>();
        for (final Sort s : pSort) {
            final Order order;
            if (s.getSortRule() == SortRule.ASC) {
                order = pCriteriaBuilder.asc(pPath.get(s.getColumnName()));
            } else {
                order = pCriteriaBuilder.desc(pPath.get(s.getColumnName()));
            }
            ret.add(order);
        }
        return ret;
    }

    /**
     * <<<<<<< HEAD =======
     * 
     * @param pValue -
     * @param pParameterName -
     * @return -
     */
    public static int convertToInt(final long pValue, final String pParameterName) {
        if (pValue > Integer.MAX_VALUE) {
            throw new IllegalArgumentException(pParameterName + " is over Interger.MAX_VALUE. value -> " + pValue); //$NON-NLS-1$
        }
        return (int) pValue;
    }

    /**
     * @param pCriteriaBuilder -
     * @return -
     */
    public static Expression<String> getDummyExpression(final CriteriaBuilder pCriteriaBuilder) {
        ArgUtil.checkNull(pCriteriaBuilder, "pCriteriaBuilder"); //$NON-NLS-1$
        return pCriteriaBuilder.literal("X"); //$NON-NLS-1$
    }

    /**
     * >>>>>>> 47229b324574a7d5104ba5556830fcf65d29a1bc 結果が高々１件のクエリを実行して結果を返します. <br>
     * 
     * @param pQuery クエリオブジェクト.
     * @param <E> 結果オブジェクトの型.
     * @return エンティティオブジェクト.
     * @throws NotFound 該当がない場合.
     */
    public static <E> E getSingleResult(final TypedQuery<E> pQuery) throws NotFound {
        try {
            return pQuery.getSingleResult();
        } catch (final NoResultException e) {
            throw NotFound.GLOBAL;
        }
    }

    /**
     * @author jabaraster
     */
    public static class WhereBuilder {

        private final List<Predicate> predicates = new ArrayList<Predicate>();

        /**
         * @param pPredicate -
         * @return -
         */
        public WhereBuilder add(final Predicate pPredicate) {
            ArgUtil.checkNull(pPredicate, "pPredicate"); //$NON-NLS-1$
            this.predicates.add(pPredicate);
            return this;
        }

        /**
         * pConditionがtrueのときにpPredicateを条件に加えます.
         * 
         * @param pCondition -
         * @param pPredicate -
         * @return -
         */
        public WhereBuilder addIf(final boolean pCondition, final Predicate pPredicate) {
            ArgUtil.checkNull(pPredicate, "pPredicate"); //$NON-NLS-1$
            if (pCondition) {
                this.predicates.add(pPredicate);
            }
            return this;
        }

        /**
         * @return -
         */
        public Predicate[] build() {
            return this.predicates.toArray(new Predicate[this.predicates.size()]);
        }
    }
}
