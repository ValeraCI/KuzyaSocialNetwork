package org.kuzya.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.kuzya.models.base.BaseEntity;
import org.kuzya.models.enums.RoleTitle;

@Entity
@Table(name = "roles")
@NoArgsConstructor
@Getter
@Setter
public class Role extends BaseEntity<Integer> {
    @Enumerated(EnumType.STRING)
    @Column(name = "title")
    private RoleTitle title;
}
