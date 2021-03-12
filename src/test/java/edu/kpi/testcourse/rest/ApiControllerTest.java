package edu.kpi.testcourse.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.kpi.testcourse.dataservice.DataService;
import edu.kpi.testcourse.dataservice.User;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import javax.inject.Inject;

@MicronautTest
public class ApiControllerTest {
  @Inject
  DataService dataService;

  @Inject
  EmbeddedServer embeddedServer;

  @Inject
  @Client("/")
  HttpClient client;

  protected UsernamePasswordCredentials testUserCreds =
      new UsernamePasswordCredentials("testUsername", "testPassword");

  @AfterEach
  void clearDatabase() {dataService.clear();}

  @Test
  void signUpNewUserStatus() {
    HttpRequest<?> req = HttpRequest.POST("/users/signup", new UserCreationRequest("testUsername", "testPassword"));
    int status = client.toBlocking().exchange(req).code();

    assertThat(status).isEqualTo(200);
  }

  @Test
  void newUserInDatabaseAfterSignUp() {
    HttpRequest<?> req = HttpRequest.POST("/users/signup", new UserCreationRequest("testUsername", "testPassword"));
    client.toBlocking().exchange(req);

    User user = dataService.getUser(testUserCreds.getUsername());

    assertThat(user != null).isTrue();
  }

  @Test
  void signUpExistingUserStatus() {
    String message = "";
    HttpRequest<?> req = HttpRequest.POST("/users/signup", new UserCreationRequest("testUsername", "testPassword"));
    client.toBlocking().exchange(req);
    try {
      client.toBlocking().exchange(req).code();
    } catch (HttpClientResponseException e) {
      message = e.getMessage();
    }

    assertThat(message).contains("Unprocessable Entity");
  }

  @Test
  void reachUnauthorizedUrls() {
    HttpRequest<?> req = HttpRequest.GET("/urls");
    String message = "";
    try {
      client.toBlocking().exchange(req).code();
    } catch (HttpClientResponseException e) {
      message = e.getMessage();
    }
    assertThat(message).contains("Unauthorized");
  }

  @Test
  void reachUnauthorizedUrlShorten() {
    HttpRequest<?> req = HttpRequest.POST("/url/shorten", "");
    String message = "";
    try {
      client.toBlocking().exchange(req).code();
    } catch (HttpClientResponseException e) {
      message = e.getMessage();
    }
    assertThat(message).contains("Unauthorized");
  }

  @Test
  void reachUnauthorizedUrlsDeleteAlias() {
    HttpRequest<?> req = HttpRequest.DELETE("/urls/delete/one");
    String message = "";
    try {
      client.toBlocking().exchange(req).code();
    } catch (HttpClientResponseException e) {
      message = e.getMessage();
    }
    assertThat(message).contains("Unauthorized");
  }

  record UserCreationRequest(@JsonProperty("email") String email, @JsonProperty("password") String password) {}
}
