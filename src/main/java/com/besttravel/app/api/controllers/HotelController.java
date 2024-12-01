package com.besttravel.app.api.controllers;

import com.besttravel.app.api.models.responses.HotelResponse;
import com.besttravel.app.infraestructure.abstract_services.IHotelService;
import com.besttravel.app.util.enums.SortType;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping(path = "hotel")
@AllArgsConstructor
@Tag(name = "Hotel")
public class HotelController {
	private final IHotelService hotelService;

	@GetMapping
	public ResponseEntity<Page<HotelResponse>> getAll(@RequestParam Integer page,
																										@RequestParam Integer size,
																										@RequestHeader(required = false) SortType sortType) {
		if (Objects.isNull(sortType)) sortType = SortType.NONE;

		Page<HotelResponse> response = this.hotelService.readAll(page, size, sortType);

		return ResponseEntity.ok(response);
	}

	@GetMapping(path = "less_price")
	public ResponseEntity<Set<HotelResponse>> getLessPrice(@RequestParam BigDecimal price) {
		return ResponseEntity.ok(this.hotelService.readLessPrice(price));
	}

	@GetMapping(path = "between_price")
	public ResponseEntity<Set<HotelResponse>> getBetweenPrice(@RequestParam BigDecimal min, @RequestParam BigDecimal max) {
		return ResponseEntity.ok(this.hotelService.readBetweenPrice(min, max));
	}

	@GetMapping(path = "rating")
	public ResponseEntity<Set<HotelResponse>> getRatingGreatherThan(@RequestParam Integer rating) {
		if(rating > 4) rating = 4;
		if(rating < 1) rating = 1;

		return ResponseEntity.ok(this.hotelService.readRatingGreatherThan(rating));
	}
}
