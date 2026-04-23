package sz.mycolonyback.colony.application.port.in;

import sz.mycolonyback.colony.domain.Colony;
import sz.mycolonyback.shared.domain.PlayerId;

import java.util.Optional;

public interface LoadColonyUseCase {

	Optional<Colony> findByPlayerId(PlayerId playerId);
}
