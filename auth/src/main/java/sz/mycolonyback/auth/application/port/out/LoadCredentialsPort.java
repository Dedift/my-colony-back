package sz.mycolonyback.auth.application.port.out;

import sz.mycolonyback.auth.domain.Credentials;
import sz.mycolonyback.shared.domain.PlayerId;

import java.util.Optional;

public interface LoadCredentialsPort {

	Optional<Credentials> findByPlayerId(PlayerId playerId);

	Optional<Credentials> findByEmail(String email);
}
