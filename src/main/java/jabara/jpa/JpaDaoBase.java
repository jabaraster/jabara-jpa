/**
 * 
 */
package jabara.jpa;

import jabara.general.ArgUtil;
import jabara.general.NotFound;
import jabara.general.Sort;
import jabara.general.SortRule;
import jabara.jpa.entity.EntityBase;
import jabara.jpa.entity.IEntity;
import jabara.jpa.entity.Id;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;

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
     * @param pEntityType
     * @return -
     */
    public long countAll(final Class<?> pEntityType) {
        ArgUtil.checkNull(pEntityType, "pEntityType"); //$NON-NLS-1$
        final EntityManager em = getEntityManager();
        final CriteriaBuilder builder = em.getCriteriaBuilder();
        final CriteriaQuery<Long> query = builder.createQuery(Long.class);
        final Root<?> root = query.from(pEntityType);
        query.select(builder.count(root));
        return em.createQuery(query).getSingleResult().longValue();
    }

    /**
     * @param pEntityType チェック対象エンティティの型.
     * @param pCheckProperty -
     * @param pValue -
     * @param <E> チェック対象エンティティの型.
     * @return 重複データが存在すればtrue.
     */
    public <E extends EntityBase<E>> boolean existsDuplicationForInsert( //
            final Class<E> pEntityType //
            , final SingularAttribute<E, String> pCheckProperty //
            , final String pValue //
    ) {

        ArgUtil.checkNull(pEntityType, "pEntityType"); //$NON-NLS-1$
        ArgUtil.checkNull(pCheckProperty, "pCheckProperty"); //$NON-NLS-1$

        final EntityManager em = getEntityManager();
        final CriteriaBuilder builder = em.getCriteriaBuilder();
        final CriteriaQuery<String> query = builder.createQuery(String.class);
        final Root<E> root = query.from(pEntityType);

        selectDummy(builder, query);
        query.where(builder.equal(root.get(pCheckProperty), pValue));

        return doCheckDuplication(em.createQuery(query));
    }

    /**
     * @param pEntityType チェック対象エンティティの型.
     * @param pUpdateEntityId -
     * @param pCheckProperty -
     * @param pValue -
     * @param <E> チェック対象エンティティの型.
     * @param <V> 重複チェック対象の値の型.
     * @return 重複データが存在すればtrue.
     */
    public <E extends EntityBase<E>, V> boolean existsDuplicationForUpdate( //
            final Class<E> pEntityType //
            , final long pUpdateEntityId //
            , final SingularAttribute<E, V> pCheckProperty //
            , final V pValue //
    ) {

        ArgUtil.checkNull(pEntityType, "pEntityType"); //$NON-NLS-1$
        ArgUtil.checkNull(pCheckProperty, "pCheckProperty"); //$NON-NLS-1$

        final EntityManager em = getEntityManager();
        final CriteriaBuilder builder = em.getCriteriaBuilder();
        final CriteriaQuery<String> query = builder.createQuery(String.class);
        final Root<E> root = query.from(pEntityType);

        selectDummy(builder, query);
        query.where( //
                builder.notEqual(root.get("id"), Long.valueOf(pUpdateEntityId)) // //$NON-NLS-1$
                , builder.equal(root.get(pCheckProperty), pValue) //
        );

        return doCheckDuplication(em.createQuery(query));
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
    public <E extends IEntity<E>> E getByIdCore(final Class<E> pEntityType, final Id<E> pId) throws NotFound {
        ArgUtil.checkNull(pEntityType, "pEntityType"); //$NON-NLS-1$
        ArgUtil.checkNull(pId, "pId"); //$NON-NLS-1$

        final E ret = getEntityManager().find(pEntityType, pId.getValueAsObject());
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
     * ソート条件を型変換します.
     * 
     * @param pSort -
     * @param pCriteriaBuilder -
     * @param pPath -
     * @return 変換後のソート条件.
     */
    public static Order convertOrder(final Sort pSort, final CriteriaBuilder pCriteriaBuilder, final Path<?> pPath) {
        ArgUtil.checkNull(pSort, "pSort"); //$NON-NLS-1$
        ArgUtil.checkNull(pCriteriaBuilder, "pCriteriaBuilder"); //$NON-NLS-1$
        ArgUtil.checkNull(pPath, "pPath"); //$NON-NLS-1$

        final Path<?> exp = resolvePath(pSort.getColumnName(), pPath);
        if (pSort.getSortRule() == SortRule.ASC) {
            return pCriteriaBuilder.asc(exp);
        }
        return pCriteriaBuilder.desc(exp);
    }

    /**
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
            ret.add(convertOrder(s, pCriteriaBuilder, pPath));
        }
        return ret;
    }

    /**
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
     * 結果が高々１件のクエリを実行して結果を返します. <br>
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
     * @param pEntities -
     * @return -
     */
    public static <E extends IEntity<E>> Map<Id<E>, E> toMap(final List<E> pEntities) {
        ArgUtil.checkNull(pEntities, "pEntities"); //$NON-NLS-1$
        final Map<Id<E>, E> ret = new HashMap<Id<E>, E>(pEntities.size());
        for (final E entity : pEntities) {
            if (entity == null) {
                throw new IllegalArgumentException("cannot process null."); //$NON-NLS-1$
            }
            ret.put(entity.getId(), entity);
        }
        return ret;
    }

    private static <V> boolean doCheckDuplication( //
            final TypedQuery<?> pQuery //
    ) {
        try {
            pQuery.getSingleResult();
            // 重複有り(チェックNG)
            return false;
        } catch (final NoResultException e) {
            return true;
        }
    }

    private static Path<?> resolvePath(final String pColumnNames, final Path<?> pRoot) {
        Path<?> path = pRoot;
        for (final String columnName : pColumnNames.split("\\.")) { //$NON-NLS-1$
            path = path.get(columnName);
        }
        return path;
    }

    private static void selectDummy(final CriteriaBuilder pCriteriaBuilder, final CriteriaQuery<String> pQuery) {
        pQuery.select(pCriteriaBuilder.literal("X").alias("DUMMY")); //$NON-NLS-1$//$NON-NLS-2$
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
