package sz.mycolonyback.colony.domain;

import sz.mycolonyback.shared.domain.AggregateVersion;
import sz.mycolonyback.shared.domain.AuditFields;
import sz.mycolonyback.shared.domain.PlayerId;

public record Colony(
	ColonyId id,
	PlayerId playerId,
	String name,
	AuditFields audit,
	AggregateVersion version
) {
}
