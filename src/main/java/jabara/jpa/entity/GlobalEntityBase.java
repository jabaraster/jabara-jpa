/**
 * 
 */
package jabara.jpa.entity;

import jabara.general.ArgUtil;
import jabara.general.TimeZoneUtil;

import java.util.Date;
import java.util.TimeZone;

import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

/**
 * @param <E> エンティティの型.
 * @author jabaraster
 */
@MappedSuperclass
public abstract class GlobalEntityBase<E extends GlobalEntityBase<E>> extends EntityBase<E> {
    private static final long serialVersionUID = 4311325698879627429L;

    /**
     * @param pTimeZone -
     * @return -
     */
    public Date getCreated(final TimeZone pTimeZone) {
        ArgUtil.checkNull(pTimeZone, "pTimeZone"); //$NON-NLS-1$
        return cnv(this.created, pTimeZone);
    }

    /**
     * @param pTimeZone -
     * @return -
     */
    public Date getUpdated(final TimeZone pTimeZone) {
        ArgUtil.checkNull(pTimeZone, "pTimeZone"); //$NON-NLS-1$
        return cnv(this.updated, pTimeZone);
    }

    /**
     * 
     */
    @PrePersist
    @Override
    protected void prePersist() {
        this.created = TimeZoneUtil.getUtcCurrentTime();
        this.updated = new Date(this.created.getTime());
    }

    /**
     * 
     */
    @PreUpdate
    @Override
    protected void preUpdate() {
        this.updated = TimeZoneUtil.getUtcCurrentTime();
    }

    private static Date cnv(final Date pDate, final TimeZone pTimeZone) {
        return pDate == null ? null : TimeZoneUtil.convertTimeZone(pDate, TimeZoneUtil.UTC, pTimeZone);
    }

}
