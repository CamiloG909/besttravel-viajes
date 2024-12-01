package com.besttravel.app.api.models.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TourRequest {
	public String customerId;
	@Size(min = 1, message = "Cantidad de vuelos inválida")
	public Set<TourFlyRequest> flights;
	public Set<TourHotelRequest> hotels;

	@Email(message = "El email no es válido")
	private String email;
}
