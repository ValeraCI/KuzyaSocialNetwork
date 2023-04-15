package org.kuzya.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.kuzya.models.base.BaseActiveEntity;

@Entity
@Table(name = "media_files")
@NoArgsConstructor
@Getter
@Setter
public class MediaFile extends BaseActiveEntity<Long> {
    @Column(name = "path")
    private String path;
    @ManyToOne()
    @JoinColumn(name = "file_type_id")
    private FileType fileType;
}
