package sz.mycolonyback.auth.domain;

public class DuplicateEmailException extends RuntimeException {

	public DuplicateEmailException(String email) {
		super("email is already registered: " + email);
	}
}
