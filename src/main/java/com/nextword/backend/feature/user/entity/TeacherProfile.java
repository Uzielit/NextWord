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
@Table(name ="perfil_profesor")
@Getter
@Setter
@NoArgsConstructor
public class TeacherProfile {
    @Id
    @Column(name = "id_usuario", length = 36, nullable = false)
    private String id;
    @Column(name = "especializacion", length = 100, nullable = false)
    private String specialization;
    @Column(name ="anios_experiencia", nullable = false)
    private Integer yearsOfExperience;
    @Column(name="descripcion_profesional", length = 2000, nullable = false)
    private String professionalDescription;
    @Column(name = "certificaciones", length = 1000, nullable = false)
    private String certifications;
    @Column(name = "estatus_cuenta", length = 20, nullable = false)
    private String accountStatus;
    @Column(name = "calificacion_promedio", nullable = false)
    private Double averageRating;


}
