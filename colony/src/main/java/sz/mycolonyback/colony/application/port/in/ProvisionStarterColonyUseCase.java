package sz.mycolonyback.colony.application.port.in;

import sz.mycolonyback.colony.domain.Colony;
import sz.mycolonyback.colony.domain.ColonyId;
import sz.mycolonyback.shared.domain.PlayerId;

public interface ProvisionStarterColonyUseCase {

	Colony provision(ProvisionStarterColonyCommand command);

	record ProvisionStarterColonyCommand(
		ColonyId colonyId,
		PlayerId playerId,
		String name
	) {
	}
}
