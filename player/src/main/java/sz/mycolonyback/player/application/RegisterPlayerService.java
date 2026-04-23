package sz.mycolonyback.player.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sz.mycolonyback.player.application.port.in.LoadPlayerUseCase;
import sz.mycolonyback.player.application.port.in.RegisterPlayerUseCase;
import sz.mycolonyback.player.application.port.out.LoadPlayerPort;
import sz.mycolonyback.player.application.port.out.SavePlayerPort;
import sz.mycolonyback.player.domain.DuplicateUsernameException;
import sz.mycolonyback.player.domain.Player;
import sz.mycolonyback.shared.domain.AggregateVersion;
import sz.mycolonyback.shared.domain.AuditFields;
import sz.mycolonyback.shared.domain.PlayerId;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RegisterPlayerService implements RegisterPlayerUseCase, LoadPlayerUseCase {

	private final LoadPlayerPort loadPlayerPort;
	private final SavePlayerPort savePlayerPort;

	@Override
	public Player register(RegisterPlayerCommand command) {
		var normalizedUsername = command.username().trim();
		if (loadPlayerPort.existsByUsername(normalizedUsername)) {
			throw new DuplicateUsernameException(normalizedUsername);
		}

		var now = Instant.now();
		var player = new Player(
			command.playerId(),
			normalizedUsername,
			command.displayName().trim(),
			command.role(),
			new AuditFields(now, now),
			new AggregateVersion(0L)
		);
		savePlayerPort.save(player);
		return player;
	}

	@Override
	public Optional<Player> findById(PlayerId playerId) {
		return loadPlayerPort.findById(playerId);
	}
}
