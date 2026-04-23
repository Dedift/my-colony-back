package sz.mycolonyback.app.auth.adapter.out.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;
import sz.mycolonyback.auth.application.port.out.AccessTokenCodec;
import sz.mycolonyback.player.domain.Player;
import sz.mycolonyback.shared.domain.PlayerId;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtAccessTokenCodec implements AccessTokenCodec {

	private final JwtEncoder jwtEncoder;
	private final JwtDecoder jwtDecoder;
	private final JwtProperties jwtProperties;

	@Override
	public String issueFor(Player player) {
		var now = Instant.now();
		var claims = JwtClaimsSet.builder()
			.issuer("my-colony-back")
			.issuedAt(now)
			.expiresAt(now.plus(jwtProperties.accessTokenTtl()))
			.subject(player.id().value().toString())
			.claim("username", player.username())
			.claim("role", player.role().name())
			.build();
		return jwtEncoder.encode(JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims))
			.getTokenValue();
	}

	@Override
	public Optional<PlayerId> parse(String accessToken) {
		try {
			var jwt = jwtDecoder.decode(accessToken);
			return Optional.of(new PlayerId(UUID.fromString(jwt.getSubject())));
		}
		catch (RuntimeException ex) {
			return Optional.empty();
		}
	}
}
