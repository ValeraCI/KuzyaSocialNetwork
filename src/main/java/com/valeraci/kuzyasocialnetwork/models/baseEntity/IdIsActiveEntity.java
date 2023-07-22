package com.valeraci.kuzyasocialnetwork.models.baseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@MappedSuperclass
@Getter
@Setter
public class IdIsActiveEntity<T extends Serializable> extends IdEntity<T> {
    @Column(name = "is_active")
    private boolean isActive;

    public IdIsActiveEntity() {
        isActive = true;
    }
}
