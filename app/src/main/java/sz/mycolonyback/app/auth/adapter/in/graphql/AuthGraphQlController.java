package sz.mycolonyback.app.auth.adapter.in.graphql;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import sz.mycolonyback.auth.application.port.in.LoginUseCase;
import sz.mycolonyback.auth.application.port.in.RegisterUseCase;
import sz.mycolonyback.auth.domain.UnauthenticatedException;
import sz.mycolonyback.colony.application.port.in.LoadColonyUseCase;
import sz.mycolonyback.player.application.port.in.LoadPlayerUseCase;
import sz.mycolonyback.shared.domain.PlayerId;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class AuthGraphQlController {

	private final RegisterUseCase registerUseCase;
	private final LoginUseCase loginUseCase;
	private final LoadPlayerUseCase loadPlayerUseCase;
	private final LoadColonyUseCase loadColonyUseCase;

	@MutationMapping
	public RegisterPayload register(@Argument @Valid RegisterInput input) {
		var result = registerUseCase.register(new RegisterUseCase.RegisterCommand(
			input.username(),
			input.displayName(),
			input.email(),
			input.password(),
			input.colonyName()
		));
		return new RegisterPayload(
			result.playerId().value().toString(),
			result.accessToken()
		);
	}

	@MutationMapping
	public LoginPayload login(@Argument @Valid LoginInput input) {
		var result = loginUseCase.login(new LoginUseCase.LoginCommand(
			input.email(),
			input.password()
		));
		return new LoginPayload(
			result.playerId().value().toString(),
			result.accessToken()
		);
	}

	@QueryMapping
	public MePayload me(@AuthenticationPrincipal Jwt jwt) {
		if (jwt == null || jwt.getSubject() == null || jwt.getSubject().isBlank()) {
			throw new UnauthenticatedException();
		}
		var playerId = new PlayerId(UUID.fromString(jwt.getSubject()));
		var player = loadPlayerUseCase.findById(playerId).orElseThrow(UnauthenticatedException::new);
		var colony = loadColonyUseCase.findByPlayerId(playerId).orElse(null);
		return new MePayload(
			player.id().value().toString(),
			player.username(),
			player.displayName(),
			player.role().name(),
			colony == null ? null : new ColonyPayload(colony.id().value().toString(), colony.name())
		);
	}

	public record RegisterInput(
		@NotBlank(message = "username must not be blank")
		String username,
		@NotBlank(message = "displayName must not be blank")
		String displayName,
		@NotBlank(message = "email must not be blank")
		@Email(message = "email has invalid format")
		String email,
		@NotBlank(message = "password must not be blank")
		@Size(min = 8, message = "password must be at least 8 characters")
		String password,
		@NotBlank(message = "colonyName must not be blank")
		String colonyName
	) {
	}

	public record RegisterPayload(
		String playerId,
		String accessToken
	) {
	}

	public record LoginInput(
		@NotBlank(message = "email must not be blank")
		@Email(message = "email has invalid format")
		String email,
		@NotBlank(message = "password must not be blank")
		String password
	) {
	}

	public record LoginPayload(
		String playerId,
		String accessToken
	) {
	}

	public record MePayload(
		String playerId,
		String username,
		String displayName,
		String role,
		ColonyPayload colony
	) {
	}

	public record ColonyPayload(
		String id,
		String name
	) {
	}
}
