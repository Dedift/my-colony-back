package sz.mycolonyback.app.colony.adapter.out.persistence;

import org.jooq.DSLContext;
import org.springframework.stereotype.Component;
import sz.mycolonyback.colony.application.port.out.LoadColonyPort;
import sz.mycolonyback.colony.application.port.out.SaveColonyPort;
import sz.mycolonyback.colony.domain.Colony;
import sz.mycolonyback.colony.domain.ColonyId;
import sz.mycolonyback.shared.domain.AggregateVersion;
import sz.mycolonyback.shared.domain.AuditFields;
import sz.mycolonyback.shared.domain.PlayerId;

import java.time.ZoneOffset;
import java.util.Optional;

import static sz.mycolonyback.jooq.Tables.COLONY;

@Component
public class JooqColonyAdapter implements SaveColonyPort, LoadColonyPort {

	private final DSLContext dslContext;

	public JooqColonyAdapter(DSLContext dslContext) {
		this.dslContext = dslContext;
	}

	@Override
	public void save(Colony colony) {
		dslContext.insertInto(COLONY)
			.set(COLONY.ID, colony.id().value())
			.set(COLONY.PLAYER_ID, colony.playerId().value())
			.set(COLONY.NAME, colony.name())
			.set(COLONY.CREATED_AT, colony.audit().createdAt().atOffset(ZoneOffset.UTC))
			.set(COLONY.UPDATED_AT, colony.audit().updatedAt().atOffset(ZoneOffset.UTC))
			.set(COLONY.VERSION, colony.version().value())
			.execute();
	}

	@Override
	public Optional<Colony> findByPlayerId(PlayerId playerId) {
		return dslContext.selectFrom(COLONY)
			.where(COLONY.PLAYER_ID.eq(playerId.value()))
			.fetchOptional(record -> new Colony(
				new ColonyId(record.getId()),
				new PlayerId(record.getPlayerId()),
				record.getName(),
				new AuditFields(record.getCreatedAt().toInstant(), record.getUpdatedAt().toInstant()),
				new AggregateVersion(record.getVersion())
			));
	}
}
