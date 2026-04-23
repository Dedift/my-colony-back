package sz.mycolonyback.player.application.port.in;

import sz.mycolonyback.player.domain.Player;
import sz.mycolonyback.player.domain.PlayerRole;
import sz.mycolonyback.shared.domain.PlayerId;

public interface RegisterPlayerUseCase {

	Player register(RegisterPlayerCommand command);

	record RegisterPlayerCommand(
		PlayerId playerId,
		String username,
		String displayName,
		PlayerRole role
	) {
	}
}
