package com.besttravel.app.domain.repositories.jpa;

import com.besttravel.app.domain.entities.jpa.HotelEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.Set;

public interface HotelRepository extends JpaRepository<HotelEntity, Long> {
	Set<HotelEntity> findByPriceLessThanEqual(BigDecimal price);

	Set<HotelEntity> findByPriceBetween(BigDecimal min, BigDecimal max);

	Set<HotelEntity> findByRatingGreaterThanEqual(Integer rating);
}
