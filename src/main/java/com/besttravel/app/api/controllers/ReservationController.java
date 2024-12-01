package com.besttravel.app.api.controllers;

import com.besttravel.app.api.models.requests.ReservationRequest;
import com.besttravel.app.api.models.responses.ReservationResponse;
import com.besttravel.app.infraestructure.abstract_services.IReservationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping(path = "reservation")
@AllArgsConstructor
@Tag(name = "Reservation")
public class ReservationController {
	private final IReservationService reservationService;

	@GetMapping(path = "{id}")
	public ResponseEntity<ReservationResponse> get(@PathVariable UUID id) {
		return ResponseEntity.ok(this.reservationService.read(id));
	}

	@GetMapping
	public ResponseEntity<Map<String, BigDecimal>> getReservationPrice(@RequestParam Long idHotel,
																																		 @RequestHeader(required = false) Currency currency) {
		if (Objects.isNull(currency)) currency = Currency.getInstance("USD");
		return ResponseEntity.ok(Collections.singletonMap("reservationPrice", this.reservationService.findPrice(idHotel, currency)));
	}

	@PostMapping
	public ResponseEntity<ReservationResponse> post(@Valid @RequestBody ReservationRequest request) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		return ResponseEntity.ok(this.reservationService.create(request));
	}

	@PutMapping(path = "{id}")
	public ResponseEntity<ReservationResponse> put(@PathVariable UUID id, @Valid @RequestBody ReservationRequest request) {
		return ResponseEntity.ok(this.reservationService.update(request, id));
	}

	@DeleteMapping(path = "{id}")
	public ResponseEntity<Map<String, String>> delete(@PathVariable UUID id) {
		this.reservationService.delete(id);
		return ResponseEntity.ok(Collections.singletonMap("message", "Deleted successfully"));
	}
}
