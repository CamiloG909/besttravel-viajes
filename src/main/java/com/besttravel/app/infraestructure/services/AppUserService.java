package com.besttravel.app.infraestructure.services;

import com.besttravel.app.domain.entities.documents.AppUserDocument;
import com.besttravel.app.domain.repositories.documents.AppUsersRepository;
import com.besttravel.app.infraestructure.abstract_services.ModifyUserService;
import com.besttravel.app.util.enums.Documents;
import com.besttravel.app.util.exceptions.UsernameNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class AppUserService implements ModifyUserService, UserDetailsService {
	private final AppUsersRepository appUsersRepository;

	@Override
	public Map<String, Boolean> enabled(String username) {
		AppUserDocument userFound = this.appUsersRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(Documents.app_users.name()));
		userFound.setEnabled(!userFound.isEnabled());

		AppUserDocument updatedUser = this.appUsersRepository.save(userFound);
		return Collections.singletonMap(updatedUser.getUsername(), updatedUser.isEnabled());
	}

	@Override
	public Map<String, Set<String>> addRole(String username, String role) {
		AppUserDocument userFound = this.appUsersRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(Documents.app_users.name()));
		userFound.getRole().getGrantedAuthorities().add(role);

		AppUserDocument updatedUser = this.appUsersRepository.save(userFound);
		return Collections.singletonMap(updatedUser.getUsername(), updatedUser.getRole().getGrantedAuthorities());
	}

	@Override
	public Map<String, Set<String>> removeRole(String username, String role) {
		AppUserDocument userFound = this.appUsersRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(Documents.app_users.name()));
		userFound.getRole().getGrantedAuthorities().remove(role);

		AppUserDocument updatedUser = this.appUsersRepository.save(userFound);
		return Collections.singletonMap(updatedUser.getUsername(), updatedUser.getRole().getGrantedAuthorities());
	}

	@Transactional(readOnly = true)
	@Override
	public UserDetails loadUserByUsername(String username) {
		var user = this.appUsersRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(Documents.app_users.name()));
		return mapUserToUserDetails(user);
	}

	private static UserDetails mapUserToUserDetails(AppUserDocument user) {
		Set<GrantedAuthority> authorities = user.getRole()
			.getGrantedAuthorities()
			.stream()
			.map(SimpleGrantedAuthority::new)
			.collect(Collectors.toSet());
		System.out.println("Authority from db" + authorities);
		return new User(
			user.getUsername(),
			user.getPassword(),
			user.isEnabled(),
			true,
			true,
			true,
			authorities
		);
	}
}
