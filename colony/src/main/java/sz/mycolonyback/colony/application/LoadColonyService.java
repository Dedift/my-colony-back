package sz.mycolonyback.colony.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sz.mycolonyback.colony.application.port.in.LoadColonyUseCase;
import sz.mycolonyback.colony.application.port.out.LoadColonyPort;
import sz.mycolonyback.colony.domain.Colony;
import sz.mycolonyback.shared.domain.PlayerId;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoadColonyService implements LoadColonyUseCase {

	private final LoadColonyPort loadColonyPort;

	@Override
	public Optional<Colony> findByPlayerId(PlayerId playerId) {
		return loadColonyPort.findByPlayerId(playerId);
	}
}
