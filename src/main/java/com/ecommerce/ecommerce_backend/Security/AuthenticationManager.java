package com.ecommerce.ecommerce_backend.Security;

import com.ecommerce.ecommerce_backend.Document.User;
import com.ecommerce.ecommerce_backend.Repository.userRepository;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

import static com.ecommerce.ecommerce_backend.util.Constant.AUTHORITIES_KEY;

/**
 * @author Dhiraj Ray
 *
 */
@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {

	@Autowired
	private TokenProvider tokenProvider;

	@Autowired
	private userRepository userRepository;

	@Override
	@SuppressWarnings("unchecked")
	public Mono<Authentication> authenticate(Authentication authentication) {
		String authToken = authentication.getCredentials().toString();
		String username;
		try {
			username = tokenProvider.getUsernameFromToken(authToken);
		} catch (Exception e) {
			username = null;
		}
		if (username != null && ! tokenProvider.isTokenExpired(authToken)) {
			Claims claims = tokenProvider.getAllClaimsFromToken(authToken);
			Mono<User> byEmail = userRepository.findByEmail(username);
			List<String> roles = claims.get(AUTHORITIES_KEY, List.class);
			List<SimpleGrantedAuthority> authorities = roles.stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
			Mono<UsernamePasswordAuthenticationToken> map = byEmail.map(user -> {
				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null, authorities);
				return auth;
			});
//			UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, username, authorities);
			SecurityContextHolder.getContext().setAuthentication(new AuthenticatedUser(username, authorities));
			return map.cast(Authentication.class);
		} else {
			return Mono.empty();
		}
	}
}
