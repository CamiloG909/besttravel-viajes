package com.besttravel.app.api.models.requests;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TourHotelRequest {
	public Long id;

	@Min(value = 1, message = "Mínimo 1 día de reservación")
	@Max(value = 30, message = "Máximo 30 días de reservación")
	@NotNull(message = "El campo es obligatorio")
	private Integer totalDays;
}
