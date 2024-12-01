package com.besttravel.app.infraestructure.services;

import com.besttravel.app.api.models.requests.TourRequest;
import com.besttravel.app.api.models.responses.TourResponse;
import com.besttravel.app.domain.entities.jpa.*;
import com.besttravel.app.domain.repositories.jpa.CustomerRepository;
import com.besttravel.app.domain.repositories.jpa.FlyRepository;
import com.besttravel.app.domain.repositories.jpa.HotelRepository;
import com.besttravel.app.domain.repositories.jpa.TourRepository;
import com.besttravel.app.infraestructure.abstract_services.ITourService;
import com.besttravel.app.infraestructure.helpers.CustomerHelper;
import com.besttravel.app.infraestructure.helpers.EmailHelper;
import com.besttravel.app.infraestructure.helpers.TourHelper;
import com.besttravel.app.util.enums.Tables;
import com.besttravel.app.util.exceptions.IdNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Transactional
@Service
@Slf4j
@AllArgsConstructor
public class TourService implements ITourService {
	private final TourRepository tourRepository;
	private final FlyRepository flyRepository;
	private final HotelRepository hotelRepository;
	private final CustomerRepository customerRepository;
	private final TourHelper tourHelper;
	private final CustomerHelper customerHelper;
	private final EmailHelper emailHelper;

	@Override
	public void removeTicket(Long tourId, UUID ticketId) {
		TourEntity tour = this.tourRepository.findById(tourId).orElseThrow(() -> new IdNotFoundException(Tables.tour.name()));
		tour.removeTicket(ticketId);
		this.tourRepository.save(tour);
	}

	@Override
	public UUID addTicket(Long tourId, Long flyId) {
		TourEntity tour = this.tourRepository.findById(tourId).orElseThrow(() -> new IdNotFoundException(Tables.tour.name()));
		FlyEntity fly = this.flyRepository.findById(flyId).orElseThrow(() -> new IdNotFoundException(Tables.fly.name()));
		TicketEntity ticket = this.tourHelper.createTicket(fly, tour.getCustomer());

		tour.addTicket(ticket);
		this.tourRepository.save(tour);
		return ticket.getId();
	}

	@Override
	public void removeReservation(Long tourId, UUID reservationId) {
		TourEntity tour = this.tourRepository.findById(tourId).orElseThrow(() -> new IdNotFoundException(Tables.tour.name()));
		tour.removeReservation(reservationId);
		this.tourRepository.save(tour);
	}

	@Override
	public UUID addReservation(Long tourId, Long hotelId, Integer totalDays) {
		TourEntity tour = this.tourRepository.findById(tourId).orElseThrow(() -> new IdNotFoundException(Tables.tour.name()));
		HotelEntity hotel = this.hotelRepository.findById(hotelId).orElseThrow(() -> new IdNotFoundException(Tables.hotel.name()));
		ReservationEntity reservation = this.tourHelper.createReservation(hotel, tour.getCustomer(), totalDays);

		tour.addReservation(reservation);
		this.tourRepository.save(tour);
		return reservation.getId();
	}

	@Override
	public TourResponse create(TourRequest request) {
		CustomerEntity customer = this.customerRepository.findById(request.getCustomerId()).orElseThrow(() -> new IdNotFoundException(Tables.customer.name()));

		HashSet<FlyEntity> flights = new HashSet<>();
		request.getFlights().forEach(fly -> flights.add(this.flyRepository.findById(fly.getId()).orElseThrow(() -> new IdNotFoundException(Tables.fly.name()))));

		HashMap<HotelEntity, Integer> hotels = new HashMap<>();
		request.getHotels().forEach(hotel -> hotels.put(this.hotelRepository.findById(hotel.getId()).orElseThrow(() -> new IdNotFoundException(Tables.hotel.name())), hotel.getTotalDays()));

		TourEntity tourToPersist = TourEntity.builder()
			.tickets(this.tourHelper.createTickets(flights, customer))
			.reservations(this.tourHelper.createReservations(hotels, customer))
			.customer(customer)
			.build();

		TourEntity tourPersisted = this.tourRepository.save(tourToPersist);

		customerHelper.increase(customer.getDni(), TourService.class);

		if (Objects.nonNull(request.getEmail()))
			this.emailHelper.sendEmail(request.getEmail(), customer.getFullName(), Tables.tour.name());

		return TourResponse.builder()
			.reservationIds(tourPersisted.getReservations().stream().map(ReservationEntity::getId).collect(Collectors.toSet()))
			.ticketIds(tourPersisted.getTickets().stream().map(TicketEntity::getId).collect(Collectors.toSet()))
			.id(tourPersisted.getId())
			.build()
			;
	}

	@Override
	public TourResponse read(Long id) {
		TourEntity tour = this.tourRepository.findById(id).orElseThrow(() -> new IdNotFoundException(Tables.tour.name()));

		return TourResponse.builder()
			.reservationIds(tour.getReservations().stream().map(ReservationEntity::getId).collect(Collectors.toSet()))
			.ticketIds(tour.getTickets().stream().map(TicketEntity::getId).collect(Collectors.toSet()))
			.id(tour.getId())
			.build()
			;
	}

	@Override
	public void delete(Long id) {
		TourEntity tour = this.tourRepository.findById(id).orElseThrow(() -> new IdNotFoundException(Tables.tour.name()));

		this.tourRepository.delete(tour);
	}
}
