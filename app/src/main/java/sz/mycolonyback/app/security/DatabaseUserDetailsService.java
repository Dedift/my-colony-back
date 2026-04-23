package sz.mycolonyback.app.security;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sz.mycolonyback.auth.application.port.out.LoadCredentialsPort;
import sz.mycolonyback.player.application.port.in.LoadPlayerUseCase;

@Service
@ConditionalOnBean({LoadCredentialsPort.class, LoadPlayerUseCase.class})
@RequiredArgsConstructor
public class DatabaseUserDetailsService implements UserDetailsService {

	private final LoadCredentialsPort loadCredentialsPort;
	private final LoadPlayerUseCase loadPlayerUseCase;

	@Override
	public UserDetails loadUserByUsername(String username) {
		var normalizedEmail = username.trim().toLowerCase();
		var credentials = loadCredentialsPort.findByEmail(normalizedEmail)
			.orElseThrow(() -> new UsernameNotFoundException("User with email '" + normalizedEmail + "' not found"));
		var player = loadPlayerUseCase.findById(credentials.playerId())
			.orElseThrow(() -> new UsernameNotFoundException("Player for email '" + normalizedEmail + "' not found"));

		UserBuilder builder = User.builder()
			.username(credentials.email())
			.password(credentials.passwordHash())
			.authorities(new SimpleGrantedAuthority("ROLE_" + player.role().name()));
		if (!credentials.emailVerified()) {
			builder.disabled(true);
		}
		return builder.build();
	}
}
