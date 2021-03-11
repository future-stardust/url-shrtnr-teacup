package edu.kpi.testcourse.rest;

import edu.kpi.testcourse.auth.PasswordHash;
import edu.kpi.testcourse.dataservice.DataService;
import edu.kpi.testcourse.dataservice.User;
import edu.kpi.testcourse.urlservice.UrlService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Optional;
import javax.inject.Inject;

/**
 * REST API controller that provides logic for Micronaut framework.
 */
@Controller
public class ApiController {
  private final String defaultUser = "John Doe";

  @Inject
  private final UrlService urlService;

  @Inject
  private final DataService dataService;

  public ApiController(UrlService urlService, DataService dataService) {
    this.urlService = urlService;
    this.dataService = dataService;
  }

  @Secured(SecurityRule.IS_ANONYMOUS)
  @Post(value = "/users/signup")
  public HttpResponse<String> signUp(String email, String password)
      throws InvalidKeySpecException, NoSuchAlgorithmException {
    if (dataService.getUser(email) != null) {
      return HttpResponse.unprocessableEntity();
    }
    String passwordHash = PasswordHash.createHash(password);
    User newUser = new User(email, passwordHash);
    dataService.addUser(newUser);

    return HttpResponse.ok();
  }


  @Secured(SecurityRule.IS_AUTHENTICATED)
  @Post(value = "/users/signout")
  public HttpResponse<String> signOut() {
    return HttpResponse.status(HttpStatus.NOT_IMPLEMENTED);
  }

  /**
   * Creates an alias for the URL.
   *
   * @param url link which has to be shortened
   * @param alias optional, desired alias for {@code url}
   * @return generated or passed alias
   */
  @Post(value = "/urls/shorten")
  public HttpResponse<String> addUrl(String url, Optional<String> alias) {
    if (alias.isEmpty()) {
      try {
        return HttpResponse.ok(urlService.addUrl(url, defaultUser));
      } catch (IOException e) {
        e.printStackTrace();
        return HttpResponse.serverError();
      }
    }
    urlService.addUrl(alias.get(), url, defaultUser);
    return HttpResponse.ok(alias.get());
  }

  @Get(value = "/urls")
  public HttpResponse<String> getUserUrls() {
    return HttpResponse.status(HttpStatus.NOT_IMPLEMENTED);
  }

  /**
   * Delete alias created by current user.
   *
   * @param alias alias to be deleted
   */
  @Delete(value = "urls/delete/{alias}")
  public HttpResponse<String> deleteAlias(String alias) {
    return HttpResponse.status(HttpStatus.NOT_IMPLEMENTED);
  }

  /**
   * Redirects by a shortened URL.
   *
   * @param alias alias of the URL
   */
  @Get(value = "/r/{alias}")
  public HttpResponse<String> redirect(String alias) {
    var url = urlService.getUrl(alias);
    URI location = null;
    try {
      location = new URI(url);
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    return HttpResponse.redirect(location);
  }
}
