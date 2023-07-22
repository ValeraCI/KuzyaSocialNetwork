package com.valeraci.kuzyasocialnetwork.models;

import com.valeraci.kuzyasocialnetwork.models.baseEntity.IdEntity;
import com.valeraci.kuzyasocialnetwork.models.enums.FamilyStatusTitle;
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
@Table(name = "family_statuses")
public class FamilyStatus extends IdEntity<Integer> {
    @Enumerated(EnumType.STRING)
    @Column(name = "title")
    private FamilyStatusTitle title;
}
