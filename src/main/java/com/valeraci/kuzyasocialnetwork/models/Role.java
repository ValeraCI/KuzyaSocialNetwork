package com.valeraci.kuzyasocialnetwork.models;

import com.valeraci.kuzyasocialnetwork.models.baseEntity.IdEntity;
import com.valeraci.kuzyasocialnetwork.models.enums.RoleTitle;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role extends IdEntity<Integer> {
    @Enumerated(EnumType.STRING)
    @Column(name = "title")
    private RoleTitle title;
}
