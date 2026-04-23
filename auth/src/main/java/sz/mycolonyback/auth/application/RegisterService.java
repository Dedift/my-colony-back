package sz.mycolonyback.auth.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sz.mycolonyback.auth.application.port.in.RegisterUseCase;
import sz.mycolonyback.auth.application.port.out.AccessTokenCodec;
import sz.mycolonyback.auth.application.port.out.LoadCredentialsPort;
import sz.mycolonyback.auth.application.port.out.PasswordHasher;
import sz.mycolonyback.auth.application.port.out.SaveCredentialsPort;
import sz.mycolonyback.auth.domain.DuplicateEmailException;
import sz.mycolonyback.auth.domain.Credentials;
import sz.mycolonyback.colony.application.port.in.ProvisionStarterColonyUseCase;
import sz.mycolonyback.colony.domain.ColonyId;
import sz.mycolonyback.player.application.port.in.RegisterPlayerUseCase;
import sz.mycolonyback.player.domain.PlayerRole;
import sz.mycolonyback.shared.domain.AuditFields;
import sz.mycolonyback.shared.domain.PlayerId;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegisterService implements RegisterUseCase {

	private final LoadCredentialsPort loadCredentialsPort;
	private final SaveCredentialsPort saveCredentialsPort;
	private final PasswordHasher passwordHasher;
	private final AccessTokenCodec accessTokenCodec;
	private final RegisterPlayerUseCase registerPlayerUseCase;
	private final ProvisionStarterColonyUseCase provisionStarterColonyUseCase;

	@Override
	@Transactional
	public RegistrationResult register(RegisterCommand command) {
		var normalizedEmail = command.email().trim().toLowerCase();
		if (loadCredentialsPort.findByEmail(normalizedEmail).isPresent()) {
			throw new DuplicateEmailException(normalizedEmail);
		}
		var playerId = new PlayerId(UUID.randomUUID());
		var player = registerPlayerUseCase.register(new RegisterPlayerUseCase.RegisterPlayerCommand(
			playerId,
			command.username(),
			command.displayName(),
			PlayerRole.USER
		));
		provisionStarterColonyUseCase.provision(new ProvisionStarterColonyUseCase.ProvisionStarterColonyCommand(
			new ColonyId(UUID.randomUUID()),
			playerId,
			command.colonyName()
		));
		var now = Instant.now();
		saveCredentialsPort.save(new Credentials(
			playerId,
			normalizedEmail,
			passwordHasher.hash(command.rawPassword()),
			false,
			new AuditFields(now, now)
		));
		return new RegistrationResult(
			playerId,
			accessTokenCodec.issueFor(player)
		);
	}
}
