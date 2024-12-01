package com.besttravel.app.domain.repositories.documents;

import com.besttravel.app.domain.entities.documents.AppUserDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AppUsersRepository extends MongoRepository<AppUserDocument, String> {
	Optional<AppUserDocument> findByUsername(String username);
}
