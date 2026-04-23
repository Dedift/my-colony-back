package sz.mycolonyback.player.domain;

public class DuplicateUsernameException extends RuntimeException {

	public DuplicateUsernameException(String username) {
		super("username is already taken: " + username);
	}
}
