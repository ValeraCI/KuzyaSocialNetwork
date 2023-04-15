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
import org.kuzya.models.enums.FamilyStatusTitle;

@Entity
@Table(name = "family_statuses")
@NoArgsConstructor
@Getter
@Setter
public class FamilyStatus extends BaseEntity<Integer> {
    @Column(name = "title")
    @Enumerated(EnumType.STRING)
    private FamilyStatusTitle title;
}
