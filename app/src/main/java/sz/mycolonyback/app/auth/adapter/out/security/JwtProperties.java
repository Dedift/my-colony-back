package sz.mycolonyback.app.auth.adapter.out.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "auth.jwt")
public record JwtProperties(
	String secret,
	Duration accessTokenTtl
) {
}
