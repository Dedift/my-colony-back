package sz.mycolonyback.colony.application.port.out;

import sz.mycolonyback.colony.domain.Colony;
import sz.mycolonyback.shared.domain.PlayerId;

import java.util.Optional;

public interface LoadColonyPort {

	Optional<Colony> findByPlayerId(PlayerId playerId);
}
