package sz.mycolonyback.auth.application.port.in;

import sz.mycolonyback.shared.domain.PlayerId;

public interface LoginUseCase {

	LoginResult login(LoginCommand command);

	record LoginCommand(
		String identifier,
		String rawPassword
	) {
	}

	record LoginResult(
		PlayerId playerId,
		String accessToken
	) {
	}
}
