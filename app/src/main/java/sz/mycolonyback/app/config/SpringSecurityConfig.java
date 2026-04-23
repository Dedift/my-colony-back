package sz.mycolonyback.app.config;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import sz.mycolonyback.app.auth.adapter.out.security.JwtProperties;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableConfigurationProperties(JwtProperties.class)
public class SpringSecurityConfig {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
			.cors(AbstractHttpConfigurer::disable)
			.csrf(AbstractHttpConfigurer::disable)
			.exceptionHandling(exceptionHandling -> exceptionHandling
				.authenticationEntryPoint((request, response, authException) ->
					response.setStatus(HttpStatus.UNAUTHORIZED.value()))
				.accessDeniedHandler((request, response, accessDeniedException) ->
					response.setStatus(HttpStatus.FORBIDDEN.value()))
			)
			.authorizeHttpRequests(authorize -> authorize
				.requestMatchers("/graphql").permitAll()
				.requestMatchers("/graphiql/**").permitAll()
				.requestMatchers("/actuator/health/**", "/actuator/info").permitAll()
				.anyRequest().authenticated()
			)
			.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())))
			.httpBasic(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.logout(AbstractHttpConfigurer::disable)
			.build();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(12);
	}

	@Bean
	JwtEncoder jwtEncoder(JwtProperties jwtProperties) {
		var secretKey = new SecretKeySpec(jwtProperties.secret().getBytes(StandardCharsets.UTF_8), "HmacSHA256");
		return new NimbusJwtEncoder(new ImmutableSecret<>(secretKey));
	}

	@Bean
	JwtDecoder jwtDecoder(JwtProperties jwtProperties) {
		var secretKey = new SecretKeySpec(jwtProperties.secret().getBytes(StandardCharsets.UTF_8), "HmacSHA256");
		return NimbusJwtDecoder.withSecretKey(secretKey)
			.macAlgorithm(MacAlgorithm.HS256)
			.build();
	}

	@Bean
	JwtAuthenticationConverter jwtAuthenticationConverter() {
		var converter = new JwtAuthenticationConverter();
		converter.setPrincipalClaimName("sub");
		converter.setJwtGrantedAuthoritiesConverter(this::extractAuthorities);
		return converter;
	}

	@Bean
	@ConditionalOnBean(UserDetailsService.class)
	AuthenticationProvider emailPasswordAuthenticationProvider(
		UserDetailsService userDetailsService,
		PasswordEncoder passwordEncoder
	) {
		var provider = new DaoAuthenticationProvider(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder);
		return provider;
	}

	@Bean
	@ConditionalOnBean(AuthenticationProvider.class)
	AuthenticationManager authenticationManager(AuthenticationProvider emailPasswordAuthenticationProvider) {
		return new ProviderManager(emailPasswordAuthenticationProvider);
	}

	private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
		var role = jwt.getClaimAsString("role");
		if (role == null || role.isBlank()) {
			return List.of();
		}
		return List.of((GrantedAuthority) () -> "ROLE_" + role);
	}
}
