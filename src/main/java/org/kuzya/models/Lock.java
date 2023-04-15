package org.kuzya.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Formula;
import org.kuzya.models.base.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "locks")
@NoArgsConstructor
@Getter
@Setter
public class Lock extends BaseEntity<Integer> {
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "beginning")
    private LocalDateTime beginning;
    @Column(name = "days")
    private int days;

    @Formula("beginning + days")
    private LocalDateTime ending;
    @Column(name = "reason")
    private String reason;
}
