package com.besttravel.app.domain.repositories.jpa;

import com.besttravel.app.domain.entities.jpa.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<CustomerEntity, String> {
}
