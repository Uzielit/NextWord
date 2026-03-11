package com.nextword.backend.feature.user.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Entity
@Table(name = "perfil_estudiante")
@Getter
@Setter
@NoArgsConstructor
public class StudentProfile {
    @Id
    @Column(name = "id_usuario", length = 36, nullable = false)
    private String id;
    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate dateOfBirth;
    @Column(name = "nombre_tutor", length = 150, nullable = false)
    private String tutorName;
    @Column(name = "contacto_tutor", length = 200, nullable = false)
    private String tutorContact;

}
