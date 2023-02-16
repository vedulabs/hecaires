package com.hecaires.rest.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        },
        indexes = {
                @Index(columnList = "id"),
                @Index(name = "username_index", columnList = "username"),
                @Index(name = "email_index", columnList = "email"),
                @Index(name = "username_email_index", columnList = "username, email")
        }
)
@Data
@NoArgsConstructor
public class User {
    @Id
    @GenericGenerator(name = "IdGenerator", strategy = "com.hecaires.rest.utils.UUIDGenerator")
    @GeneratedValue(generator = "IdGenerator")
    @Column(columnDefinition = "varchar(32)")
    private String id;

    @NotBlank
    @Size(max = 256)
    private String username;

    @NotBlank
    @Size(max = 256)
    @Email
    private String email;

    @NotBlank
    @Size(max = 120)
    private String password;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", columnDefinition = "varchar(32)"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}