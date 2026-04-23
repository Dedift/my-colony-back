package sz.mycolonyback.auth.domain;

public class AuthenticationFailedException extends RuntimeException {

	public AuthenticationFailedException() {
		super("invalid credentials");
	}
}
