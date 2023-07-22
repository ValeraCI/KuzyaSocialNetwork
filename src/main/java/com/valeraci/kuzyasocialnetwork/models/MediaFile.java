package com.valeraci.kuzyasocialnetwork.models;

import com.valeraci.kuzyasocialnetwork.models.baseEntity.IdIsActiveEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "media_files")
public class MediaFile extends IdIsActiveEntity<Long> {
    @Column(name = "path")
    private String path;

    @ManyToOne
    @JoinColumn(name = "file_type_id")
    private FileType fileType;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
}
