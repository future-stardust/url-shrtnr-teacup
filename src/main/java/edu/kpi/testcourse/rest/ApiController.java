package edu.kpi.testcourse.rest;

import edu.kpi.testcourse.Main;
import edu.kpi.testcourse.auth.JwtHelper;
import edu.kpi.testcourse.auth.PasswordHash;
import edu.kpi.testcourse.dataservice.DataService;
import edu.kpi.testcourse.dataservice.User;
import edu.kpi.testcourse.urlservice.UrlService;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
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
import java.security.Principal;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.util.Optional;
import javax.inject.Inject;

/**
 * REST API controller that provides logic for Micronaut framework.
 */
@Controller
public class ApiController {

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
   * @param email user username used as unique identifier
   * @param password user password
   * @return HttpResponse 200 OK or 422 with error message
   */
  @Secured(SecurityRule.IS_ANONYMOUS)
  @Post(value = "/users/signup")
  public HttpResponse<String> signUp(String email, String password) {
    if (dataService.getUser(email) != null) {
      return HttpResponse.unprocessableEntity().body("User already exists!");
    }
    String passwordHash = null;
    try {
      passwordHash = PasswordHash.createHash(password);
    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      e.printStackTrace();
      return HttpResponse.serverError("Server error!");
    }

    User newUser = new User(email, passwordHash);
    dataService.addUser(newUser);

    return HttpResponse.ok();
  }

  /**
   * Creates an alias for the URL.
   *
   * @param url link which has to be shortened
   * @param alias optional, desired alias for {@code url}
   * @return generated or passed alias. In case of error could return status code
   *  <p>400 (Bad request) if {@code alias} isn't an alphanumeric string</p>
   *  <p>406 (Not acceptable) if user with this {@code username} doesn't exist</p>
   *  <p>409 (Conflict) if record with the same {@code alias} already exists</p>
   */
  @Secured(SecurityRule.IS_AUTHENTICATED)
  @Post(value = "/urls/shorten")
  public HttpResponse<String> addUrl(HttpRequest<?> request, String url, Optional<String> alias) {
    try {
      var username = JwtHelper.getUsernameFromRequest(request);
      try {
        if (alias.isEmpty()) {
          return HttpResponse.ok(urlService.addUrl(url, username));
        }

        if (urlService.isUserAliasValid(alias.get())) {
          if (urlService.addUrl(alias.get(), url, username)) {
            return HttpResponse.ok(alias.get());
          } else {
            return HttpResponse.status(HttpStatus.CONFLICT,
              "Record with the same alias already exists");
          }
        } else {
          return HttpResponse.status(HttpStatus.BAD_REQUEST,
            "Alias should be an alphanumeric string");
        }

      } catch (IllegalArgumentException e) {
        return HttpResponse.status(HttpStatus.NOT_ACCEPTABLE,
          String.format("User %s doesn't exist, cannot create alias.", username));

      } catch (IOException e) {
        e.printStackTrace();
        return HttpResponse.serverError();
      }

    } catch (ParseException e) {
      e.printStackTrace();
      return HttpResponse.serverError();
    }
  }

  /**
   * Returns all aliases, created by current user.
   *
   * @return json list of created aliases
   *     (model is {@link edu.kpi.testcourse.urlservice.AliasInfo}).
   */
  @Secured(SecurityRule.IS_AUTHENTICATED)
  @Get(value = "/urls", produces = MediaType.APPLICATION_JSON)
  public HttpResponse<String> getUserUrls(HttpRequest<?> request) {
    try {
      var username = JwtHelper.getUsernameFromRequest(request);
      var result = urlService.getUserAliases(username);
      return HttpResponse.ok(Main.getGson().toJson(result));
    } catch (ParseException e) {
      e.printStackTrace();
      return HttpResponse.serverError();
    }
  }

  /**
   * Deletes specified alias, created by current user.
   *
   * @param alias to be deleted
   * @return 200 (Ok) status code. In case of error could return status code
   *  <p>400 (Bad request) if {@code alias} doesn't exist or wasn't created by current user</p>
   */
  @Secured(SecurityRule.IS_AUTHENTICATED)
  @Delete(value = "urls/delete/{alias}")
  public HttpResponse<String> deleteAlias(HttpRequest<?> request, String alias) {
    try {
      var username = JwtHelper.getUsernameFromRequest(request);
      if (urlService.deleteAlias(alias, username)) {
        return HttpResponse.ok();
      } else {
        return HttpResponse.badRequest(
          String.format("Alias %s doesn't exist or wasn't created by current user.", alias));
      }
    } catch (ParseException e) {
      e.printStackTrace();
      return HttpResponse.serverError();
    }
  }

  /**
   * Redirects by a shortened URL.
   *
   * @return 301 (Moved permanently) - successful redirection status code.
   *     In case of error could return status code
   *  <p>400 (Bad request) if {@code alias} doesn't exist</p>
   */
  @Secured(SecurityRule.IS_ANONYMOUS)
  @Get(value = "/r/{alias}")
  public HttpResponse<String> redirect(String alias) {
    var url = urlService.getUrl(alias);
    if (url == null) {
      return HttpResponse.badRequest(
        String.format("Alias %s doesn't exist.", alias));
    }
    URI location = null;
    try {
      location = new URI(url);
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    return HttpResponse.redirect(location);
  }
}
