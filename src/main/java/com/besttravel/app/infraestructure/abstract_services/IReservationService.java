package com.besttravel.app.infraestructure.abstract_services;

import com.besttravel.app.api.models.requests.ReservationRequest;
import com.besttravel.app.api.models.responses.ReservationResponse;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

public interface IReservationService extends CrudService<ReservationRequest, ReservationResponse, UUID>{
	BigDecimal findPrice(Long idHotel, Currency currency);
}
