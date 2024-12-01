package com.besttravel.app.infraestructure.helpers;

import com.besttravel.app.domain.entities.jpa.*;
import com.besttravel.app.domain.repositories.jpa.ReservationRepository;
import com.besttravel.app.domain.repositories.jpa.TicketRepository;
import com.besttravel.app.infraestructure.services.ReservationService;
import com.besttravel.app.infraestructure.services.TicketService;
import com.besttravel.app.util.BestTravelUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Transactional
@Component
@AllArgsConstructor
public class TourHelper {
	private final TicketRepository ticketRepository;
	private final ReservationRepository reservationRepository;

	public Set<TicketEntity> createTickets(Set<FlyEntity> flights, CustomerEntity customer) {
		HashSet<TicketEntity> response = new HashSet<>(flights.size());

		flights.forEach(flyEntity -> {
			TicketEntity ticketToPersist = TicketEntity.builder()
				.id(UUID.randomUUID())
				.fly(flyEntity)
				.customer(customer)
				.price(flyEntity.getPrice().add(flyEntity.getPrice().multiply(TicketService.charger_price_percentage)))
				.purchaseDate(BestTravelUtil.getRandomSoon())
				.arrivalDate(BestTravelUtil.getRandomLater())
				.departureDate(BestTravelUtil.getRandomLater())
				.build();

			response.add(this.ticketRepository.save(ticketToPersist));
		});

		return response;
	}

	public Set<ReservationEntity> createReservations(HashMap<HotelEntity, Integer> hotels, CustomerEntity customer) {
		HashSet<ReservationEntity> response = new HashSet<>(hotels.size());

		hotels.forEach((hotelEntity, totalDays) -> {
			ReservationEntity reservationToPersist = ReservationEntity.builder()
				.id(UUID.randomUUID())
				.hotel(hotelEntity)
				.customer(customer)
				.totalDays(totalDays)
				.dateTimeReservation(BestTravelUtil.getRandomLater())
				.dateStart(LocalDate.now())
				.dateEnd(LocalDate.now().plusDays(totalDays))
				.price(hotelEntity.getPrice().add(hotelEntity.getPrice().multiply(ReservationService.charger_price_percentage)))
				.build();

			response.add(this.reservationRepository.save(reservationToPersist));
		});

		return response;
	}

	public TicketEntity createTicket(FlyEntity fly, CustomerEntity customer) {
		TicketEntity ticketToPersist = TicketEntity.builder()
			.id(UUID.randomUUID())
			.fly(fly)
			.customer(customer)
			.price(fly.getPrice().add(fly.getPrice().multiply(TicketService.charger_price_percentage)))
			.purchaseDate(BestTravelUtil.getRandomSoon())
			.arrivalDate(BestTravelUtil.getRandomLater())
			.departureDate(BestTravelUtil.getRandomLater())
			.build();

		return this.ticketRepository.save(ticketToPersist);
	}

	public ReservationEntity createReservation(HotelEntity hotel, CustomerEntity customer, Integer totalDays) {
		ReservationEntity reservationToPersist = ReservationEntity.builder()
			.id(UUID.randomUUID())
			.hotel(hotel)
			.customer(customer)
			.totalDays(totalDays)
			.dateTimeReservation(BestTravelUtil.getRandomLater())
			.dateStart(LocalDate.now())
			.dateEnd(LocalDate.now().plusDays(totalDays))
			.price(hotel.getPrice().add(hotel.getPrice().multiply(ReservationService.charger_price_percentage)))
			.build();

		return this.reservationRepository.save(reservationToPersist);
	}

}
