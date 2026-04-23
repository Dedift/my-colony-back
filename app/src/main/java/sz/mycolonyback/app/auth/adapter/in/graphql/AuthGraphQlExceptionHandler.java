package sz.mycolonyback.app.auth.adapter.in.graphql;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Component;
import sz.mycolonyback.auth.domain.AuthenticationFailedException;
import sz.mycolonyback.auth.domain.DuplicateEmailException;
import sz.mycolonyback.auth.domain.UnauthenticatedException;
import sz.mycolonyback.player.domain.DuplicateUsernameException;

@Component
public class AuthGraphQlExceptionHandler extends DataFetcherExceptionResolverAdapter {

	@Override
	protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
		if (ex instanceof DuplicateEmailException || ex instanceof DuplicateUsernameException) {
			return GraphqlErrorBuilder.newError(env)
				.errorType(ErrorType.BAD_REQUEST)
				.message(ex.getMessage())
				.build();
		}
		if (ex instanceof AuthenticationFailedException || ex instanceof UnauthenticatedException) {
			return GraphqlErrorBuilder.newError(env)
				.errorType(ErrorType.UNAUTHORIZED)
				.message(ex.getMessage())
				.build();
		}
		return null;
	}
}
