package jabara.jpa.entity;

import java.io.Serializable;

/**
 * @param <E> エンティティの型.
 * @author jabaraster
 */
public class Id<E extends IEntity<E>> implements Serializable {
    private static final long serialVersionUID = -5635652442854282245L;

    private final long        value;
    private final int         hash;
    private final String      toString;

    /**
     * @param pValue -
     */
    public Id(final long pValue) {
        this.value = pValue;
        this.hash = computeHash();
        this.toString = buildToString();
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Id<?> other = (Id<?>) obj;
        if (this.value != other.value) {
            return false;
        }
        return true;
    }

    /**
     * @return -
     */
    public long getValue() {
        return this.value;
    }

    /**
     * @return -
     */
    public Long getValueAsObject() {
        return Long.valueOf(this.value);
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return this.hash;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.toString;
    }

    private String buildToString() {
        return "Id [value=" + this.value + "]"; //$NON-NLS-1$ //$NON-NLS-2$
    }

    private int computeHash() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (this.value ^ this.value >>> 32);
        return result;
    }
}