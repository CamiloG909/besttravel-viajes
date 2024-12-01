package com.besttravel.app.infraestructure.services;

import com.besttravel.app.api.models.responses.HotelResponse;
import com.besttravel.app.domain.entities.jpa.HotelEntity;
import com.besttravel.app.domain.repositories.jpa.HotelRepository;
import com.besttravel.app.infraestructure.abstract_services.IHotelService;
import com.besttravel.app.util.constants.CacheConstants;
import com.besttravel.app.util.enums.SortType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
@Slf4j
@AllArgsConstructor
public class HotelService implements IHotelService {
	private final HotelRepository hotelRepository;

	@Override
	@Cacheable(value = CacheConstants.HOTEL_CACHE_NAME)
	public Page<HotelResponse> readAll(Integer page, Integer size, SortType sortType) {
		PageRequest pageRequest = null;

		switch (sortType) {
			case NONE -> pageRequest = PageRequest.of(page, size);
			case LOWER -> pageRequest = PageRequest.of(page, size, Sort.by(FIELD_BY_SORT).ascending());
			case UPPER -> pageRequest = PageRequest.of(page, size, Sort.by(FIELD_BY_SORT).descending());
		}

		return this.hotelRepository.findAll(pageRequest).map(this::entityToResponse);
	}

	@Override
	@Cacheable(value = CacheConstants.HOTEL_CACHE_NAME)
	public Set<HotelResponse> readLessPrice(BigDecimal price) {
		return this.hotelRepository.findByPriceLessThanEqual(price).stream().map(this::entityToResponse).collect(Collectors.toSet());
	}

	@Override
	@Cacheable(value = CacheConstants.HOTEL_CACHE_NAME)
	public Set<HotelResponse> readBetweenPrice(BigDecimal min, BigDecimal max) {
		return this.hotelRepository.findByPriceBetween(min, max).stream().map(this::entityToResponse).collect(Collectors.toSet());
	}

	@Override
	@Cacheable(value = CacheConstants.HOTEL_CACHE_NAME)
	public Set<HotelResponse> readRatingGreatherThan(Integer rating) {
		return this.hotelRepository.findByRatingGreaterThanEqual(rating).stream().map(this::entityToResponse).collect(Collectors.toSet());
	}

	private HotelResponse entityToResponse(HotelEntity hotelEntity) {
		HotelResponse response = new HotelResponse();
		BeanUtils.copyProperties(hotelEntity, response);

		return response;
	}
}
