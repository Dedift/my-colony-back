package sz.mycolonyback.app.auth.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;
import sz.mycolonyback.auth.application.port.out.LoadCredentialsPort;
import sz.mycolonyback.auth.application.port.out.SaveCredentialsPort;
import sz.mycolonyback.auth.domain.Credentials;
import sz.mycolonyback.shared.domain.AuditFields;
import sz.mycolonyback.shared.domain.PlayerId;
import sz.mycolonyback.jooq.tables.records.PlayerCredentialsRecord;

import java.util.Optional;

import static sz.mycolonyback.jooq.Tables.PLAYER_CREDENTIALS;

@Component
@RequiredArgsConstructor
public class JdbcCredentialsAdapter implements LoadCredentialsPort, SaveCredentialsPort {

	private final DSLContext dslContext;

	@Override
	public Optional<Credentials> findByPlayerId(PlayerId playerId) {
		return dslContext.selectFrom(PLAYER_CREDENTIALS)
			.where(PLAYER_CREDENTIALS.PLAYER_ID.eq(playerId.value()))
			.fetchOptional(this::mapCredentials);
	}

	@Override
	public Optional<Credentials> findByEmail(String email) {
		return dslContext.selectFrom(PLAYER_CREDENTIALS)
			.where(PLAYER_CREDENTIALS.EMAIL.equalIgnoreCase(email.trim()))
			.fetchOptional(this::mapCredentials);
	}

	@Override
	public void save(Credentials credentials) {
		dslContext.insertInto(PLAYER_CREDENTIALS)
			.set(PLAYER_CREDENTIALS.PLAYER_ID, credentials.playerId().value())
			.set(PLAYER_CREDENTIALS.EMAIL, credentials.email())
			.set(PLAYER_CREDENTIALS.PASSWORD_HASH, credentials.passwordHash())
			.set(PLAYER_CREDENTIALS.EMAIL_VERIFIED, credentials.emailVerified())
			.set(PLAYER_CREDENTIALS.CREATED_AT, credentials.audit().createdAt().atOffset(java.time.ZoneOffset.UTC))
			.set(PLAYER_CREDENTIALS.UPDATED_AT, credentials.audit().updatedAt().atOffset(java.time.ZoneOffset.UTC))
			.execute();
	}

	private Credentials mapCredentials(PlayerCredentialsRecord record) {
		return new Credentials(
			new PlayerId(record.getPlayerId()),
			record.getEmail(),
			record.getPasswordHash(),
			record.getEmailVerified(),
			new AuditFields(record.getCreatedAt().toInstant(), record.getUpdatedAt().toInstant())
		);
	}
}
