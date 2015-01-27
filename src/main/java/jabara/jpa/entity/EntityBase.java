/**
 * 
 */
package jabara.jpa.entity;

import jabara.general.ExceptionUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @param <E> -
 * @author jabaraster
 */
@MappedSuperclass
public abstract class EntityBase<E extends EntityBase<E>> implements IEntity<E>, Serializable {
    private static final long serialVersionUID = -6285769975070008758L;

    /**
     * 
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long            id;

    /**
     * 
     */
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    protected Date            created;

    /**
     * 
     */
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    protected Date            updated;

    /**
     * @see java.lang.Object#clone()
     */
    @SuppressWarnings("unchecked")
    @Override
    public E clone() {
        try {
            final ByteArrayOutputStream mem = new ByteArrayOutputStream();
            final ObjectOutputStream objOut = new ObjectOutputStream(mem);
            objOut.writeObject(this);

            return (E) new ObjectInputStream(new ByteArrayInputStream(mem.toByteArray())).readObject();

        } catch (final Exception e) {
            throw ExceptionUtil.rethrow(e);
        }
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
        final EntityBase<?> other = (EntityBase<?>) obj;
        if (this.id == null && other.id == null) {
            return false;
        }
        if (this.id != null && other.id == null) {
            return false;
        }
        if (this.id == null && other.id != null) {
            return false;
        }
        return this.id.equals(other.id);
    }

    /**
     * @see jabara.jpa.entity.IEntity#getCreated()
     */
    @Override
    public Date getCreated() {
        return this.created == null ? null : new Date(this.created.getTime());
    }

    /**
     * @see jabara.jpa.entity.IEntity#getId()
     */
    public jabara.jpa.entity.Id<E> getId() {
        if (this.id == null) {
            throw new IllegalStateException("not persisted."); //$NON-NLS-1$
        }
        return new jabara.jpa.entity.Id<E>(this.id.longValue());
    }

    /**
     * @see jabara.jpa.entity.IEntity#getUpdated()
     */
    @Override
    public Date getUpdated() {
        return this.updated == null ? null : new Date(this.updated.getTime());
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (this.id == null ? 0 : this.id.hashCode());
        return result;
    }

    /**
     * @return インスタンスが永続化されていればtrue. <br>
     *         実装的にはidフィールドが非nullの時に永続されていると判断しています. <br>
     */
    public boolean isPersisted() {
        return this.id != null;
    }

    /**
     * 
     */
    @PrePersist
    protected void prePersist() {
        this.created = Calendar.getInstance().getTime();
        this.updated = new Date(this.created.getTime());
    }

    /**
     * 
     */
    @PreUpdate
    protected void preUpdate() {
        this.updated = Calendar.getInstance().getTime();
    }
}
