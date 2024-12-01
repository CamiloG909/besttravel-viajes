package com.besttravel.app.infraestructure.abstract_services;

import com.besttravel.app.api.models.requests.TourRequest;
import com.besttravel.app.api.models.responses.TourResponse;

import java.util.UUID;

public interface ITourService extends SimpleCrudService<TourRequest, TourResponse, Long> {
	void removeTicket(Long tourId, UUID ticketId);

	UUID addTicket(Long tourId, Long flyId);

	void removeReservation(Long tourId, UUID reservationId);

	UUID addReservation(Long tourId, Long hotelId, Integer totalDays);
}
