package com.besttravel.app.api.controllers;

import com.besttravel.app.api.models.responses.FlyResponse;
import com.besttravel.app.infraestructure.abstract_services.IFlyService;
import com.besttravel.app.util.enums.SortType;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping(path = "fly")
@AllArgsConstructor
@Tag(name = "Fly")
public class FlyController {
	private final IFlyService flyService;

	@GetMapping
	public ResponseEntity<Page<FlyResponse>> getAll(@RequestParam Integer page,
																									@RequestParam Integer size,
																									@RequestHeader(required = false) SortType sortType) {
		if (Objects.isNull(sortType)) sortType = SortType.NONE;

		Page<FlyResponse> response = this.flyService.readAll(page, size, sortType);

		return ResponseEntity.ok(response);
	}

	@GetMapping(path = "less_price")
	public ResponseEntity<Set<FlyResponse>> getLessPrice(@RequestParam BigDecimal price) {
		return ResponseEntity.ok(this.flyService.readLessPrice(price));
	}

	@GetMapping(path = "between_price")
	public ResponseEntity<Set<FlyResponse>> getBetweenPrice(@RequestParam BigDecimal min, @RequestParam BigDecimal max) {
		return ResponseEntity.ok(this.flyService.readBetweenPrice(min, max));
	}

	@GetMapping(path = "origin_destiny")
	public ResponseEntity<Set<FlyResponse>> getOriginDestiny(@RequestParam String origin, @RequestParam String destiny) {
		return ResponseEntity.ok(this.flyService.readByOriginDestiny(origin, destiny));
	}
}
