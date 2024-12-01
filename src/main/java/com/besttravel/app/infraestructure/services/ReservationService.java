package com.besttravel.app.infraestructure.services;

import com.besttravel.app.api.models.requests.ReservationRequest;
import com.besttravel.app.api.models.responses.HotelResponse;
import com.besttravel.app.api.models.responses.ReservationResponse;
import com.besttravel.app.domain.entities.jpa.CustomerEntity;
import com.besttravel.app.domain.entities.jpa.HotelEntity;
import com.besttravel.app.domain.entities.jpa.ReservationEntity;
import com.besttravel.app.domain.repositories.jpa.CustomerRepository;
import com.besttravel.app.domain.repositories.jpa.HotelRepository;
import com.besttravel.app.domain.repositories.jpa.ReservationRepository;
import com.besttravel.app.infraestructure.abstract_services.IReservationService;
import com.besttravel.app.infraestructure.dtos.CurrencyDTO;
import com.besttravel.app.infraestructure.helpers.ApiCurrencyConnectorHelper;
import com.besttravel.app.infraestructure.helpers.CustomerHelper;
import com.besttravel.app.infraestructure.helpers.EmailHelper;
import com.besttravel.app.util.BestTravelUtil;
import com.besttravel.app.util.constants.CacheConstants;
import com.besttravel.app.util.enums.Tables;
import com.besttravel.app.util.exceptions.IdNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.Objects;
import java.util.UUID;

@Transactional
@Service
@Slf4j
@AllArgsConstructor
public class ReservationService implements IReservationService {
	private final HotelRepository hotelRepository;
	private final CustomerRepository customerRepository;
	private final ReservationRepository reservationRepository;
	private final CustomerHelper customerHelper;
	private final ApiCurrencyConnectorHelper apiCurrencyConnectorHelper;
	private final EmailHelper emailHelper;

	@Override
	public ReservationResponse create(ReservationRequest request) {
		HotelEntity hotel = this.hotelRepository.findById(request.getIdHotel()).orElseThrow(() -> new IdNotFoundException(Tables.hotel.name()));
		CustomerEntity customer = this.customerRepository.findById(request.getIdClient()).orElseThrow(() -> new IdNotFoundException(Tables.customer.name()));

		ReservationEntity reservationToPersist = ReservationEntity.builder()
			.id(UUID.randomUUID())
			.hotel(hotel)
			.customer(customer)
			.totalDays(request.getTotalDays())
			.dateTimeReservation(BestTravelUtil.getRandomLater())
			.dateStart(LocalDate.now())
			.dateEnd(LocalDate.now().plusDays(request.getTotalDays()))
			.price(hotel.getPrice().add(hotel.getPrice().multiply(charger_price_percentage)))
			.build();

		ReservationEntity reservationPersisted = this.reservationRepository.save(reservationToPersist);

		customerHelper.increase(customer.getDni(), ReservationService.class);

		log.info("Reservation saved with id: {}", reservationPersisted.getId());

		if (Objects.nonNull(request.getEmail()))
			this.emailHelper.sendEmail(request.getEmail(), customer.getFullName(), Tables.reservation.name());

		return this.entityToResponse(reservationPersisted);
	}

	@Override
	@Cacheable(value = CacheConstants.RESERVATION_CACHE_NAME)
	public ReservationResponse read(UUID uuid) {
		ReservationEntity reservation = this.reservationRepository.findById(uuid).orElseThrow(() -> new IdNotFoundException(Tables.reservation.name()));

		return this.entityToResponse(reservation);
	}

	@Override
	@CacheEvict(cacheNames = CacheConstants.RESERVATION_CACHE_NAME, allEntries = true)
	public ReservationResponse update(ReservationRequest request, UUID uuid) {
		ReservationEntity reservation = this.reservationRepository.findById(uuid).orElseThrow(() -> new IdNotFoundException(Tables.reservation.name()));
		HotelEntity hotel = this.hotelRepository.findById(request.getIdHotel()).orElseThrow(() -> new IdNotFoundException(Tables.hotel.name()));

		reservation.setHotel(hotel);
		reservation.setTotalDays(request.getTotalDays());
		reservation.setDateTimeReservation(BestTravelUtil.getRandomLater());
		reservation.setDateStart(LocalDate.now());
		reservation.setDateEnd(LocalDate.now().plusDays(request.getTotalDays()));
		reservation.setPrice(hotel.getPrice().add(hotel.getPrice().multiply(charger_price_percentage)));

		ReservationEntity updatedReservation = this.reservationRepository.save(reservation);

		log.info("Reservation updated with id: {}", updatedReservation.getId());

		return this.entityToResponse(updatedReservation);
	}

	@Override
	public void delete(UUID uuid) {
		ReservationEntity reservation = this.reservationRepository.findById(uuid).orElseThrow(() -> new IdNotFoundException(Tables.reservation.name()));

		this.reservationRepository.delete(reservation);
	}

	@Override
	public BigDecimal findPrice(Long idHotel, Currency currency) {
		HotelEntity hotel = this.hotelRepository.findById(idHotel).orElseThrow(() -> new IdNotFoundException(Tables.hotel.name()));
		BigDecimal price = hotel.getPrice().add(hotel.getPrice().multiply(charger_price_percentage));

		if (currency.equals(Currency.getInstance("USD"))) return price;

		CurrencyDTO currencyDTO = this.apiCurrencyConnectorHelper.getCurrency(currency);
		return price.multiply(currencyDTO.getRates().get(currency));
	}

	private ReservationResponse entityToResponse(ReservationEntity reservationEntity) {
		ReservationResponse response = new ReservationResponse();
		BeanUtils.copyProperties(reservationEntity, response);

		HotelResponse hotelResponse = new HotelResponse();
		BeanUtils.copyProperties(reservationEntity.getHotel(), hotelResponse);
		response.setHotel(hotelResponse);

		return response;
	}

	public static final BigDecimal charger_price_percentage = BigDecimal.valueOf(0.20);
}
