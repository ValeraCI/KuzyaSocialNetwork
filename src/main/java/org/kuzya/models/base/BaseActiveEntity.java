package org.kuzya.models.base;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public class BaseActiveEntity<PK extends Number> extends BaseEntity<PK> {
    @Column(name = "is_active")
    private boolean isActive;
}
