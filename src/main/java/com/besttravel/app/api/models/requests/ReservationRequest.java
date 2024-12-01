package com.besttravel.app.api.models.requests;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ReservationRequest {
	@Size(min = 18, max = 20, message = "El ID debe contener entre 18 y 20 caracteres")
	@NotBlank(message = "El ID es obligatorio")
	private String idClient;

	@Positive(message = "El ID no es válido")
	@NotNull(message = "El ID es obligatorio")
	private Long idHotel;

	@Min(value = 1, message = "Mínimo 1 día de reservación")
	@Max(value = 30, message = "Máximo 30 días de reservación")
	@NotNull(message = "El campo es obligatorio")
	private Integer totalDays;

	@Email(message = "El email no es válido")
	@NotNull(message = "El email es obligatorio")
	private String email;
}
