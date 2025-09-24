package org.mrbag.AdminSpringBoot.User;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserApp implements UserDetails {

	String login;
	
	String password;
	
	Collection<String> roles;
	
	private static final long serialVersionUID = 1L;

	public UserApp(String[] raws) {
		if (raws.length != 3)
			throw new RuntimeException("Wrong user format, Fix String: " + raws); // :-]
		this.login = raws[0];
		// TODO add after other password method's
		this.password = raws[1];
		this.roles = Arrays.stream(raws[2].split(",")).map(r -> r.trim()).filter(r -> !r.isBlank() && !r.isBlank()).toList();
	}
	
	@SuppressWarnings("serial")
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return roles.stream().map(r -> new SimpleGrantedAuthority(r)).toList();
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return login;
	}

}
