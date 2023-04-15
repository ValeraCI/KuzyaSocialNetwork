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
import org.kuzya.models.enums.FileTypeTitle;

@Entity
@Table(name = "file_types")
@NoArgsConstructor
@Getter
@Setter
public class FileType extends BaseEntity<Integer> {
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private FileTypeTitle type;
}
