package com.hecaires.rest.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "devices"
)
@Data
@NoArgsConstructor
public class Device {
    @Id
    @Column(columnDefinition = "varchar(32)")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
    @JoinColumn(name="user_id", columnDefinition = "varchar(32)", nullable=false)
    @JsonIgnore
    private User user;
}