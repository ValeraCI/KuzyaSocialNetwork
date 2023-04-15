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

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@NoArgsConstructor
@Getter
@Setter
public class Comment extends BaseActiveEntity<Long> {
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
    @Column(name = "text")
    private String text;
    @Column(name = "date")
    private LocalDateTime date;
}
