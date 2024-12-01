package com.besttravel.app.api.controllers;

import com.besttravel.app.infraestructure.abstract_services.ModifyUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping(path = "user")
@AllArgsConstructor
@Tag(name = "Users")
@Transactional
public class AppUsersControllers {
	private final ModifyUserService modifyUserService;

	@Operation(summary = "Enabled or disabled user")
	@PatchMapping(path = "state")
	public ResponseEntity<Map<String, Boolean>> enabledOrDisabled(@RequestParam String username) {
		return ResponseEntity.ok(this.modifyUserService.enabled(username));
	}

	@Operation(summary = "Add role user")
	@PatchMapping(path = "add-role")
	public ResponseEntity<Map<String, Set<String>>> addRoleUser(@RequestParam String username, @RequestParam String role) {
		return ResponseEntity.ok(this.modifyUserService.addRole(username, role));
	}

	@Operation(summary = "Remove role user")
	@PatchMapping(path = "remove-role")
	public ResponseEntity<Map<String, Set<String>>> removeRoleUser(@RequestParam String username, @RequestParam String role) {
		return ResponseEntity.ok(this.modifyUserService.removeRole(username, role));
	}
}
