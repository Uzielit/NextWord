package com.nextword.backend.feature.user.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "Usuario")
@Getter
@Setter
@NoArgsConstructor
public class User implements UserDetails {

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

    @Column(name = "reset_token")
    private String resetToken;

    @Column(name = "reset_token_expiration")
    private LocalDateTime resetTokenExpiration;



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return switch (this.roleId) {
            case 1 -> List.of(new SimpleGrantedAuthority("ROLE_ESTUDIANTE"));
            case 2 -> List.of(new SimpleGrantedAuthority("ROLE_PROFESOR"));
            case 3 -> List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
            default -> List.of(new SimpleGrantedAuthority("ROLE_ESTUDIANTE"));
        };
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {return true;}
}
      