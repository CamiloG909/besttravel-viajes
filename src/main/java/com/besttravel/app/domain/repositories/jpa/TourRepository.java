package com.besttravel.app.domain.repositories.jpa;

import com.besttravel.app.domain.entities.jpa.TourEntity;
import org.springframework.data.repository.CrudRepository;

public interface TourRepository extends CrudRepository<TourEntity, Long> {
}
