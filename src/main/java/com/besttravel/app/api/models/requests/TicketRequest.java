package com.besttravel.app.api.models.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TicketRequest {
	@Size(min = 18, max = 20, message = "El ID debe contener entre 18 y 20 caracteres")
	@NotBlank(message = "El ID es obligatorio")
	private String idClient;
	private Long idFly;

	@Email(message = "El email no es válido")
	private String email;
}
