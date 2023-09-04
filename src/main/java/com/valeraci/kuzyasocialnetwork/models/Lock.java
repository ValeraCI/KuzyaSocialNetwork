package com.valeraci.kuzyasocialnetwork.models;

import com.valeraci.kuzyasocialnetwork.models.baseEntity.IdEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "locks")
public class Lock extends IdEntity<Long> {
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserCredential userCredential;
    @Column(name = "beginning")
    private LocalDateTime beginning;
    @Column(name = "days")
    private int days;
    @Column(name = "reason")
    private String reason;

    public LocalDateTime getEnding(){
        return beginning.plusDays(days);
    }
}
