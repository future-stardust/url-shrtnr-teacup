package edu.kpi.testcourse.rest;

import edu.kpi.testcourse.urlservice.UrlService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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

  public ApiController(UrlService urlService) {
    this.urlService = urlService;
  }

  @Post(value = "/users/signup")
  public HttpResponse<String> signUp(String email, String password) {
    return HttpResponse.status(HttpStatus.NOT_IMPLEMENTED);
  }

  @Post(value = "/users/signin")
  public HttpResponse<String> signIp(String email, String password) {
    return HttpResponse.status(HttpStatus.NOT_IMPLEMENTED);
  }

  @Post(value = "/users/signup")
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
