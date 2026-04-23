package sz.mycolonyback.auth.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sz.mycolonyback.auth.application.port.in.LoginUseCase;
import sz.mycolonyback.auth.application.port.out.AccessTokenCodec;
import sz.mycolonyback.auth.application.port.out.LoadCredentialsPort;
import sz.mycolonyback.auth.application.port.out.PasswordHasher;
import sz.mycolonyback.auth.domain.AuthenticationFailedException;
import sz.mycolonyback.player.application.port.in.LoadPlayerUseCase;

@Service
@RequiredArgsConstructor
public class LoginService implements LoginUseCase {

	private final LoadCredentialsPort loadCredentialsPort;
	private final LoadPlayerUseCase loadPlayerUseCase;
	private final PasswordHasher passwordHasher;
	private final AccessTokenCodec accessTokenCodec;

	@Override
	@Transactional(readOnly = true)
	public LoginResult login(LoginCommand command) {
		var normalizedEmail = command.identifier().trim().toLowerCase();
		var credentials = loadCredentialsPort.findByEmail(normalizedEmail)
			.orElseThrow(AuthenticationFailedException::new);
		if (!passwordHasher.matches(command.rawPassword(), credentials.passwordHash())) {
			throw new AuthenticationFailedException();
		}
		var player = loadPlayerUseCase.findById(credentials.playerId())
			.orElseThrow(AuthenticationFailedException::new);
		return new LoginResult(player.id(), accessTokenCodec.issueFor(player));
	}
}
