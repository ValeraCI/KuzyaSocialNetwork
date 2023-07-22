package com.valeraci.kuzyasocialnetwork.models;

import com.valeraci.kuzyasocialnetwork.models.baseEntity.IdIsActiveEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "posts")
public class Post extends IdIsActiveEntity<Long> {
    public Post() {
        date = LocalDateTime.now();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "date")
    private LocalDateTime date;
    @Column(name = "title")
    private String title;
    @Column(name = "text")
    private String text;
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Set<MediaFile> mediaFiles;
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private Set<Comment> comments;
}
