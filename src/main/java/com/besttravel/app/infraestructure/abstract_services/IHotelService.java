package com.besttravel.app.infraestructure.abstract_services;

import com.besttravel.app.api.models.responses.HotelResponse;

import java.util.Set;

public interface IHotelService extends CatalogService<HotelResponse> {
	Set<HotelResponse> readRatingGreatherThan(Integer rating);
}
