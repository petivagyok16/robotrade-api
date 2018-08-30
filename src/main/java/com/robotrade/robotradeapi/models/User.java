package com.robotrade.robotradeapi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.*;

@Document(collection = "users")
@Data
public class User implements UserDetails {

	@Id
	private String id;

	@NotBlank(message = "Username cannot be empty!")
	private String username;

	@Size(min = 4, message = "Password must be at least 4 characters long!")
	private String password;

	@NotNull(message = "Cash cannot be null")
	@Min(0)
	private Double cash;

	private Double stock;

	private List<UserTransaction> transactionHistory;

	// These are needed for Spring basic authentication flow
	private List<String> roles;

	private boolean enabled;


	public User(
					@JsonProperty("id") String id,
					@JsonProperty("username") String username,
					@JsonProperty("password") String password,
					@JsonProperty("cash") Double cash
	) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.cash = cash;
		this.transactionHistory = new ArrayList<>();

		// TODO: authentication works currently with roles, so that all the users will get admin role, but we dont use roles in the client application
		this.roles = Collections.singletonList("ROLE_ADMIN");
		this.enabled = true;
	}

	@Override
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return AuthorityUtils.createAuthorityList(roles.toArray(new String[roles.size()]));
	}

	@JsonIgnore
	public boolean isEnabled() {
		return this.enabled;
	}

	@JsonIgnore
	public List<String> getRoles() {
		return this.roles;
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	@JsonIgnore
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@JsonIgnore
	public String getPassword() {
		return password;
	}

	@JsonIgnore
	public List<UserTransaction> getTransactionHistory() {
		return this.transactionHistory;
	}

}
