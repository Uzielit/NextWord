package com.nextword.backend.feature.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RoleRequest(
        @NotNull(message = "Este campo no puede ser nulo ") Integer idRol,
        @NotBlank(message = "Este campo no puede estar blanco ") String Nombre
) {

}
