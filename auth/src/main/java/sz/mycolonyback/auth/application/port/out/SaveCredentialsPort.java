package sz.mycolonyback.auth.application.port.out;

import sz.mycolonyback.auth.domain.Credentials;

public interface SaveCredentialsPort {

	void save(Credentials credentials);
}
