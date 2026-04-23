package sz.mycolonyback.app.auth.adapter.out.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import sz.mycolonyback.auth.application.port.out.PasswordHasher;

@Component
@RequiredArgsConstructor
public class BcryptPasswordHasher implements PasswordHasher {

	private final PasswordEncoder passwordEncoder;

	@Override
	public String hash(String rawPassword) {
		return passwordEncoder.encode(rawPassword);
	}

	@Override
	public boolean matches(String rawPassword, String passwordHash) {
		return passwordEncoder.matches(rawPassword, passwordHash);
	}
}
