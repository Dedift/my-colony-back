package sz.mycolonyback.colony.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sz.mycolonyback.colony.application.port.in.ProvisionStarterColonyUseCase;
import sz.mycolonyback.colony.application.port.out.SaveColonyPort;
import sz.mycolonyback.colony.domain.Colony;
import sz.mycolonyback.shared.domain.AggregateVersion;
import sz.mycolonyback.shared.domain.AuditFields;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ProvisionStarterColonyService implements ProvisionStarterColonyUseCase {

	private final SaveColonyPort saveColonyPort;

	@Override
	public Colony provision(ProvisionStarterColonyCommand command) {
		var now = Instant.now();
		var colony = new Colony(
			command.colonyId(),
			command.playerId(),
			command.name().trim(),
			new AuditFields(now, now),
			new AggregateVersion(0L)
		);
		saveColonyPort.save(colony);
		return colony;
	}
}
