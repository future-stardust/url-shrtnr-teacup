package edu.kpi.testcourse.auth;

import edu.kpi.testcourse.dataservice.DataService;
import edu.kpi.testcourse.dataservice.User;
import edu.umd.cs.findbugs.annotations.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.AuthenticationException;
import io.micronaut.security.authentication.AuthenticationFailed;
import io.micronaut.security.authentication.AuthenticationProvider;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.authentication.UserDetails;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import java.util.ArrayList;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.reactivestreams.Publisher;

/**
 * Micronaut authentication bean that contains authorization logic: ensures that a user is
 * registered in the system and password is right.
 */
@Singleton
public class AuthenticationProviderUserPassword implements AuthenticationProvider {

  @Inject
  private final DataService dataService;

  public AuthenticationProviderUserPassword(DataService dataService) {
    this.dataService = dataService;
  }

  @Override
  public Publisher<AuthenticationResponse> authenticate(
      @Nullable HttpRequest<?> httpRequest,
      AuthenticationRequest<?, ?> authenticationRequest
  ) {
    String username = (String) authenticationRequest.getIdentity();
    User user = dataService.getUser((username));

    return Flowable.create(emitter -> {
      if (user != null && PasswordHash.validatePassword((String) authenticationRequest.getSecret(),
            user.getPasswordHash())) {
        emitter.onNext(new UserDetails(username, new ArrayList<>()));
        emitter.onComplete();
      } else {
        emitter.onError(new AuthenticationException(
            new AuthenticationFailed("Wrong username or password!"))
        );
      }
    }, BackpressureStrategy.ERROR);
  }
}
