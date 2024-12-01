package com.besttravel.app.infraestructure.abstract_services;

import com.besttravel.app.api.models.requests.TicketRequest;
import com.besttravel.app.api.models.responses.TicketResponse;

import java.math.BigDecimal;
import java.util.UUID;

public interface ITicketService extends CrudService<TicketRequest, TicketResponse, UUID> {
	BigDecimal findPrice(Long idFly);
}
