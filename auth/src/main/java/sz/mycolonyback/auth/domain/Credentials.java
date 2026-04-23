package sz.mycolonyback.auth.domain;

import sz.mycolonyback.shared.domain.AuditFields;
import sz.mycolonyback.shared.domain.PlayerId;

public record Credentials(
	PlayerId playerId,
	String email,
	String passwordHash,
	boolean emailVerified,
	AuditFields audit
) {
}
