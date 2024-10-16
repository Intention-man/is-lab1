package com.example.prac.model.dataEntity;

import com.example.prac.model.authEntity.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class Dragon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    @Min(value = 1, message = "ID must be greater than 0")
    private long id;

    @NotBlank(message = "Name cannot be null or empty")
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "coordinates_id", referencedColumnName = "id", nullable = false)
    @NotNull(message = "Coordinates cannot be null")
    private Coordinates coordinates;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_date", nullable = false, updatable = false)
    private Date creationDate = new Date();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cave_id", referencedColumnName = "id", nullable = false)
    @NotNull(message = "Cave cannot be null")
    private DragonCave cave;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "killer_id", referencedColumnName = "id")
    private Person killer;

    @Min(value = 1, message = "Age must be greater than 0")
    @Column(name = "age")
    private Long age;

    @Enumerated(EnumType.STRING)
    @Column(name = "color", nullable = false)
    private Color color;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private DragonType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "character", nullable = false)
    @NotNull(message = "Character cannot be null")
    private DragonCharacter character;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "head_id", referencedColumnName = "id", nullable = true)
    private DragonHead head;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "owner_id", referencedColumnName = "id", nullable = false)
    @NotNull(message = "Owner cannot be null")
    private User owner;
}
