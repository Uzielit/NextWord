package com.nextword.backend.feature.user.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Rol")
@Getter
@Setter
@NoArgsConstructor
public class Role {
    @Id
    @Column(name="id_rol",nullable = false)
    private Integer id;
    @Column(name="nombre",length = 50,nullable = false)
    private String name;
}
