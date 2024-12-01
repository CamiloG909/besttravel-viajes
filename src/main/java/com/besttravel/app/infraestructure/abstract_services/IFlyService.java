package com.besttravel.app.infraestructure.abstract_services;

import com.besttravel.app.api.models.responses.FlyResponse;

import java.util.Set;

public interface IFlyService extends CatalogService<FlyResponse> {
	Set<FlyResponse> readByOriginDestiny(String origin, String destiny);
}
