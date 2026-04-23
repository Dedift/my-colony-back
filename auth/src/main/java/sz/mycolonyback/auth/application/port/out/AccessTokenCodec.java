package sz.mycolonyback.auth.application.port.out;

import sz.mycolonyback.player.domain.Player;
import sz.mycolonyback.shared.domain.PlayerId;

import java.util.Optional;

public interface AccessTokenCodec {

	String issueFor(Player player);

	Optional<PlayerId> parse(String accessToken);
}
