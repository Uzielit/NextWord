package com.nextword.backend.feature.user.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "Usuario")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @Column(name = "id_usuario", length = 36, nullable = false)
    private String id;

    @Column(name = "correo", length = 255, nullable = false, unique = true)
    private String email;

    @Column(name = "contrasenia_cifrada", length = 255, nullable = false)
    private String password;

    @Column(name = "id_rol", nullable = false)
    private Integer roleId;

    @Column(name = "telefono", length = 20)
    private String phoneNumber;

    @Column(name = "nombre_completo", length = 150, nullable = false)
    private String fullName;

    @Column(name = "foto_perfil", length = 500)
    private String profilePicture;

    @Column(name = "saldo_favor", precision = 10, scale = 2)
    private BigDecimal walletBalance = BigDecimal.ZERO;
}