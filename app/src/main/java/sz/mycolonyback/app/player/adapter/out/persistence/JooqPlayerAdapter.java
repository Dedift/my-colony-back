package sz.mycolonyback.app.player.adapter.out.persistence;

import org.jooq.DSLContext;
import org.springframework.stereotype.Component;
import sz.mycolonyback.player.application.port.out.LoadPlayerPort;
import sz.mycolonyback.player.application.port.out.SavePlayerPort;
import sz.mycolonyback.player.domain.Player;
import sz.mycolonyback.player.domain.PlayerRole;
import sz.mycolonyback.shared.domain.AggregateVersion;
import sz.mycolonyback.shared.domain.AuditFields;
import sz.mycolonyback.shared.domain.PlayerId;

import java.time.ZoneOffset;
import java.util.Optional;

import static sz.mycolonyback.jooq.Tables.PLAYER;

@Component
public class JooqPlayerAdapter implements LoadPlayerPort, SavePlayerPort {

	private final DSLContext dslContext;

	public JooqPlayerAdapter(DSLContext dslContext) {
		this.dslContext = dslContext;
	}

	@Override
	public Optional<Player> findById(PlayerId playerId) {
		return dslContext.selectFrom(PLAYER)
			.where(PLAYER.ID.eq(playerId.value()))
			.fetchOptional(record -> new Player(
				new PlayerId(record.getId()),
				record.getUsername(),
				record.getDisplayName(),
				PlayerRole.valueOf(record.getRole()),
				new AuditFields(record.getCreatedAt().toInstant(), record.getUpdatedAt().toInstant()),
				new AggregateVersion(record.getVersion())
			));
	}

	@Override
	public boolean existsByUsername(String username) {
		return dslContext.fetchExists(
			dslContext.selectOne()
				.from(PLAYER)
				.where(PLAYER.USERNAME.equalIgnoreCase(username.trim()))
		);
	}

	@Override
	public void save(Player player) {
		dslContext.insertInto(PLAYER)
			.set(PLAYER.ID, player.id().value())
			.set(PLAYER.USERNAME, player.username())
			.set(PLAYER.DISPLAY_NAME, player.displayName())
			.set(PLAYER.ROLE, player.role().name())
			.set(PLAYER.CREATED_AT, player.audit().createdAt().atOffset(ZoneOffset.UTC))
			.set(PLAYER.UPDATED_AT, player.audit().updatedAt().atOffset(ZoneOffset.UTC))
			.set(PLAYER.VERSION, player.version().value())
			.execute();
	}
}
