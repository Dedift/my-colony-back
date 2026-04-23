package sz.mycolonyback.shared.domain;

import java.time.Instant;

public record AuditFields(
	Instant createdAt,
	Instant updatedAt
) {
}
