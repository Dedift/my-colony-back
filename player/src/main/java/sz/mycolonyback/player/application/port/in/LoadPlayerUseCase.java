package sz.mycolonyback.player.application.port.in;

import sz.mycolonyback.player.domain.Player;
import sz.mycolonyback.shared.domain.PlayerId;

import java.util.Optional;

public interface LoadPlayerUseCase {

	Optional<Player> findById(PlayerId playerId);
}
