package de.conet.isd.skima.skimabackend.domain.entities._common;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;


@MappedSuperclass
public abstract class AbstractEntity implements Serializable, EntityWithId<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Version
    protected long version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractEntity that = (AbstractEntity) o;
        return id != null && that.id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public String toString() {
        return getClass().getName() + "@" + id;
    }
}
