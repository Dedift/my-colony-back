package sz.mycolonyback.auth.application.port.in;

import sz.mycolonyback.shared.domain.PlayerId;

public interface RegisterUseCase {

	RegistrationResult register(RegisterCommand command);

	record RegisterCommand(
		String username,
		String displayName,
		String email,
		String rawPassword,
		String colonyName
	) {
	}

	record RegistrationResult(
		PlayerId playerId,
		String accessToken
	) {
	}
}
