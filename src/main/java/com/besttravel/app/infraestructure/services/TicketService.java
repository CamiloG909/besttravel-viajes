package com.besttravel.app.infraestructure.services;

import com.besttravel.app.api.models.requests.TicketRequest;
import com.besttravel.app.api.models.responses.FlyResponse;
import com.besttravel.app.api.models.responses.TicketResponse;
import com.besttravel.app.domain.entities.jpa.CustomerEntity;
import com.besttravel.app.domain.entities.jpa.FlyEntity;
import com.besttravel.app.domain.entities.jpa.TicketEntity;
import com.besttravel.app.domain.repositories.jpa.CustomerRepository;
import com.besttravel.app.domain.repositories.jpa.FlyRepository;
import com.besttravel.app.domain.repositories.jpa.TicketRepository;
import com.besttravel.app.infraestructure.abstract_services.ITicketService;
import com.besttravel.app.infraestructure.helpers.CustomerHelper;
import com.besttravel.app.infraestructure.helpers.EmailHelper;
import com.besttravel.app.util.BestTravelUtil;
import com.besttravel.app.util.enums.Tables;
import com.besttravel.app.util.exceptions.IdNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Transactional
@Service
@Slf4j
@AllArgsConstructor
public class TicketService implements ITicketService {

	private final FlyRepository flyRepository;
	private final CustomerRepository customerRepository;
	private final TicketRepository ticketRepository;
	private final CustomerHelper customerHelper;
		private final EmailHelper emailHelper;

	@Override
	public TicketResponse create(TicketRequest request) {
		FlyEntity fly = flyRepository.findById(request.getIdFly()).orElseThrow(()-> new IdNotFoundException(Tables.fly.name()));
		CustomerEntity customer = customerRepository.findById(request.getIdClient()).orElseThrow(()-> new IdNotFoundException(Tables.customer.name()));

		TicketEntity ticketToPersist = TicketEntity.builder()
			.id(UUID.randomUUID())
			.fly(fly)
			.customer(customer)
			.price(fly.getPrice().add(fly.getPrice().multiply(charger_price_percentage)))
			.purchaseDate(BestTravelUtil.getRandomSoon())
			.arrivalDate(BestTravelUtil.getRandomLater())
			.departureDate(BestTravelUtil.getRandomLater())
			.build();

		TicketEntity ticketPersisted = this.ticketRepository.save(ticketToPersist);

		customerHelper.increase(customer.getDni(), TicketService.class);

		log.info("Ticked saved with id: {}", ticketPersisted.getId());

		if(Objects.nonNull(request.getEmail())) this.emailHelper.sendEmail(request.getEmail(), customer.getFullName(), Tables.ticket.name());

		return this.entityToResponse(ticketPersisted);
	}

	@Override
	public TicketResponse read(UUID uuid) {
		TicketEntity ticket = this.ticketRepository.findById(uuid).orElseThrow(()-> new IdNotFoundException(Tables.ticket.name()));

		return this.entityToResponse(ticket);
	}

	@Override
	public TicketResponse update(TicketRequest request, UUID uuid) {
		TicketEntity ticket = this.ticketRepository.findById(uuid).orElseThrow(()-> new IdNotFoundException(Tables.ticket.name()));
		FlyEntity fly = this.flyRepository.findById(request.getIdFly()).orElseThrow(()-> new IdNotFoundException(Tables.fly.name()));

		ticket.setFly(fly);
		ticket.setPrice(fly.getPrice().add(fly.getPrice().multiply(charger_price_percentage)));
		ticket.setArrivalDate(BestTravelUtil.getRandomSoon());
		ticket.setDepartureDate(BestTravelUtil.getRandomLater());

		TicketEntity updatedTicket = this.ticketRepository.save(ticket);

		log.info("Ticked updated with id: {}", updatedTicket.getId());

		return this.entityToResponse(updatedTicket);
	}

	@Override
	public void delete(UUID uuid) {
		TicketEntity ticket = this.ticketRepository.findById(uuid).orElseThrow(()-> new IdNotFoundException(Tables.ticket.name()));

		this.ticketRepository.delete(ticket);
	}

	@Override
	public BigDecimal findPrice(Long idFly) {
		FlyEntity fly = this.flyRepository.findById(idFly).orElseThrow(()-> new IdNotFoundException(Tables.fly.name()));

		return fly.getPrice().add(fly.getPrice().multiply(charger_price_percentage));
	}

	private TicketResponse entityToResponse(TicketEntity ticketEntity) {
		TicketResponse response = new TicketResponse();
		BeanUtils.copyProperties(ticketEntity, response);

		FlyResponse flyResponse = new FlyResponse();
		BeanUtils.copyProperties(ticketEntity.getFly(), flyResponse);
		response.setFly(flyResponse);

		return response;
	}

	public static final BigDecimal charger_price_percentage = BigDecimal.valueOf(0.25);
}
