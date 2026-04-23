package sz.mycolonyback.player.application.port.out;

import sz.mycolonyback.player.domain.Player;
import sz.mycolonyback.shared.domain.PlayerId;

import java.util.Optional;

public interface LoadPlayerPort {

	Optional<Player> findById(PlayerId playerId);

	boolean existsByUsername(String username);
}
