package org.mrbag.AdminSpringBoot;

import java.io.IOException;
import java.util.Collection;

import org.mrbag.AdminSpringBoot.User.UserDetalisWithConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/users")
public class UsersUpdateList {

	@Autowired
	UserDetalisWithConfig detalis;

	@PostMapping("/refresh")
	public ResponseEntity<?> updateUsers() {
		try {
			detalis.update();
			return ResponseEntity.ok("Update from file succefull");
		} catch (IOException e) {
			return ResponseEntity.badRequest().body(e);
		}
	}

	@GetMapping("/logins")
	public Collection<String> getLogins() {
		return detalis.getKey();
	}
}
