package sz.mycolonyback.auth.domain;

public class UnauthenticatedException extends RuntimeException {

	public UnauthenticatedException() {
		super("authentication is required");
	}
}
