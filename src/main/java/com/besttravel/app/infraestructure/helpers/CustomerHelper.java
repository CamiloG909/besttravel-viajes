package com.besttravel.app.infraestructure.helpers;

import com.besttravel.app.domain.entities.jpa.CustomerEntity;
import com.besttravel.app.domain.repositories.jpa.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
@AllArgsConstructor
public class CustomerHelper {
	private final CustomerRepository customerRepository;

	public void increase(String customerId, Class<?> type) {
		CustomerEntity customer = this.customerRepository.findById(customerId).orElseThrow();

		switch (type.getSimpleName()) {
			case "TourService" -> customer.setTotalTours(customer.getTotalTours() + 1);
			case "TicketService" -> customer.setTotalFlights(customer.getTotalFlights() + 1);
			case "ReservationService" -> customer.setTotalLodgings(customer.getTotalLodgings() + 1);
		}

		this.customerRepository.save(customer);
	};
}
