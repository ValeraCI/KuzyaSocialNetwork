package org.kuzya.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.kuzya.models.base.BaseActiveEntity;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "posts")
public class Post extends BaseActiveEntity<Long> {
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "date")
    private LocalDateTime date;
    @Column(name = "title")
    private String title;
    @Column(name = "text")
    private String text;
    @OneToMany(mappedBy = "post",
            fetch = FetchType.EAGER)
    private Set<Comment> comments;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "posts_media_files",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "media_file_id")
    )
    private Set<MediaFile> mediaFiles;
}
