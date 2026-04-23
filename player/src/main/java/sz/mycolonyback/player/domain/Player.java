package sz.mycolonyback.player.domain;

import sz.mycolonyback.shared.domain.AggregateVersion;
import sz.mycolonyback.shared.domain.AuditFields;
import sz.mycolonyback.shared.domain.PlayerId;

public record Player(
	PlayerId id,
	String username,
	String displayName,
	PlayerRole role,
	AuditFields audit,
	AggregateVersion version
) {
}
