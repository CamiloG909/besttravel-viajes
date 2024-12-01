package com.besttravel.app.api.controllers;

import com.besttravel.app.api.models.requests.TourRequest;
import com.besttravel.app.api.models.responses.TourResponse;
import com.besttravel.app.infraestructure.abstract_services.ITourService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(path = "tour")
@AllArgsConstructor
@Tag(name = "Tour")
public class TourController {
	private final ITourService tourService;

	@Operation(summary = "Get tour with ID")
	@GetMapping(path = "{id}")
	public ResponseEntity<TourResponse> get(@PathVariable Long id) {
		return ResponseEntity.ok(this.tourService.read(id));
	}

	@Operation(summary = "Save tour")
	@PostMapping
	public ResponseEntity<TourResponse> post(@RequestBody TourRequest request) {
		return ResponseEntity.ok(this.tourService.create(request));
	}

	@Operation(summary = "Add ticket with ID")
	@PatchMapping(path = "{tourId}/add_ticket/{flyId}")
	public ResponseEntity<Map<String, UUID>> addTicket(@PathVariable Long tourId, @PathVariable Long flyId) {
		Map<String, UUID> response = Collections.singletonMap("ticketId", this.tourService.addTicket(tourId, flyId));
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "Remove ticket with ID")
	@PatchMapping(path = "{tourId}/remove_ticket/{ticketId}")
	public ResponseEntity<Map<String, String>> removeTicket(@PathVariable Long tourId, @PathVariable UUID ticketId) {
		this.tourService.removeTicket(tourId, ticketId);
		return ResponseEntity.ok(Collections.singletonMap("message", "Deleted successfully"));
	}

	@Operation(summary = "Remove hotel with ID")
	@PatchMapping(path = "{tourId}/add_reservation/{hotelId}")
	public ResponseEntity<Map<String, UUID>> addReservation(@PathVariable Long tourId,
																													@PathVariable Long hotelId,
																													@RequestParam Integer totalDays) {
		Map<String, UUID> response = Collections.singletonMap("reservationId", this.tourService.addReservation(tourId, hotelId, totalDays));
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "Remove reservation with ID")
	@PatchMapping(path = "{tourId}/remove_reservation/{reservationId}")
	public ResponseEntity<Map<String, String>> removeReservation(@PathVariable Long tourId, @PathVariable UUID reservationId) {
		this.tourService.removeReservation(tourId, reservationId);
		return ResponseEntity.ok(Collections.singletonMap("message", "Deleted successfully"));
	}

	@Operation(summary = "Delete tour with ID")
	@DeleteMapping(path = "{id}")
	public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
		this.tourService.delete(id);
		return ResponseEntity.ok(Collections.singletonMap("message", "Deleted successfully"));
	}
}
