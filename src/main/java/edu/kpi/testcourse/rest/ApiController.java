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

  /**
   * Creates new user.
   *
   * @param username user username used as unique identifier
   * @param password user password
   * @return HttpResponse 200 OK or 422 with error message
   */
  @Secured(SecurityRule.IS_ANONYMOUS)
  @Post(value = "/users/signup")
  public HttpResponse<String> signUp(String username, String password) {
    if (dataService.getUser(username) != null) {
      return HttpResponse.unprocessableEntity().body("User already exists!");
    }
    String passwordHash = null;
    try {
      passwordHash = PasswordHash.createHash(password);
    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      e.printStackTrace();
      return HttpResponse.serverError("Server error!");
    }

    User newUser = new User(username, passwordHash);
    dataService.addUser(newUser);

    return HttpResponse.ok();
  }

  /**
   * Creates an alias for the URL.
   *
   * @param url link which has to be shortened
   * @param alias optional, desired alias for {@code url}
   * @return generated or passed alias
   */
  @Secured(SecurityRule.IS_AUTHENTICATED)
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

  @Secured(SecurityRule.IS_AUTHENTICATED)
  @Get(value = "/urls")
  public HttpResponse<String> getUserUrls() {
    return HttpResponse.status(HttpStatus.NOT_IMPLEMENTED);
  }

  /**
   * Delete alias created by current user.
   *
   * @param alias alias to be deleted
   */
  @Secured(SecurityRule.IS_AUTHENTICATED)
  @Delete(value = "urls/delete/{alias}")
  public HttpResponse<String> deleteAlias(String alias) {
    return HttpResponse.status(HttpStatus.NOT_IMPLEMENTED);
  }

  /**
   * Redirects by a shortened URL.
   *
   * @param alias alias of the URL
   */
  @Secured(SecurityRule.IS_ANONYMOUS)
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
