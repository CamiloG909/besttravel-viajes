package com.besttravel.app.api.controllers;

import com.besttravel.app.api.models.requests.TicketRequest;
import com.besttravel.app.api.models.responses.TicketResponse;
import com.besttravel.app.infraestructure.abstract_services.ITicketService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(path = "ticket")
@AllArgsConstructor
@Tag(name = "Ticket")
public class TicketController {
	private final ITicketService ticketService;

	@GetMapping(path = "{id}")
	public ResponseEntity<TicketResponse> get(@PathVariable UUID id) {
		return ResponseEntity.ok(this.ticketService.read(id));
	}

	@GetMapping
	public ResponseEntity<Map<String, BigDecimal>> getFlyPrice (@RequestParam Long idFly) {
		return ResponseEntity.ok(Collections.singletonMap("flyPrice", this.ticketService.findPrice(idFly)));
	}

	@PostMapping
	public ResponseEntity<TicketResponse> post(@Valid @RequestBody TicketRequest request) {
		return ResponseEntity.ok(this.ticketService.create(request));
	}

	@PutMapping(path = "{id}")
	public ResponseEntity<TicketResponse> put(@PathVariable UUID id, @Valid @RequestBody TicketRequest request) {
		return ResponseEntity.ok(this.ticketService.update(request, id));
	}

	@DeleteMapping(path = "{id}")
	public ResponseEntity<Map<String, String>> delete(@PathVariable UUID id) {
		this.ticketService.delete(id);
		return ResponseEntity.ok(Collections.singletonMap("message", "Deleted successfully"));
	}
}
