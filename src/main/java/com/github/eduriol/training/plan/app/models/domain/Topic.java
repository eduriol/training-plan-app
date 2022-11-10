package com.github.eduriol.training.plan.app.models.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="topics")
@Data
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Column(name = "created_at")
    private Date createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = new Date();
    }

}
