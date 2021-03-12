package edu.kpi.testcourse.dataservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UrlAliasTests extends DataServiceImplTest {

  @BeforeEach
  void addTestUser() {
    dataService.addUser(testUser);
  }

  @Test
  void addAlias() {
    var result = dataService.addUrlAlias(testUrlAlias);

    assertThat(result).isTrue();
  }

  @Test
  void addAliasIfAliasExists(){
    var firstResult = dataService.addUrlAlias(testUrlAlias);
    var secondResult = dataService.addUrlAlias(testUrlAlias);

    assertThat(secondResult).isFalse();
  }

  @Test
  void getAlias() {
    dataService.addUrlAlias(testUrlAlias);
    var result = dataService.getUrlAlias(testUrlAlias.getAlias());

    assertThat(result.getUrl()).isEqualTo(testUrlAlias.getUrl());
  }

  @Test
  void getAliasIfAliasNotFound() {
    var result = dataService.getUrlAlias("wrongAlias");

    assertThat(result).isNull();
  }

  @Test
  void deleteAliasReturnsTrue() {
    dataService.addUrlAlias(testUrlAlias);
    var result = dataService.deleteUrlAlias(testUrlAlias.getAlias(), testUser.getUsername());

    assertThat(result).isTrue();
  }

  @Test
  void deleteAliasIfAliasNotFound() {
    var result = dataService.deleteUrlAlias(testUrlAlias.getAlias(), testUser.getUsername());

    assertThat(result).isFalse();
  }

  @Test
  void deleteAliasIfWrongUser() {
    dataService.addUrlAlias(testUrlAlias);
    var result = dataService.deleteUrlAlias(testUrlAlias.getAlias(), "wrongEmail");

    assertThat(result).isFalse();
  }

  @Test
  void deleteAlias() {
    dataService.addUrlAlias(testUrlAlias);
    dataService.deleteUrlAlias(testUrlAlias.getAlias(), testUser.getUsername());
    var result = dataService.getUrlAlias(testUrlAlias.getAlias());

    assertThat(result).isNull();
  }

  @Test
  void getUserAliases() {
    var user2 = "user2";
    dataService.addUser(new User(testUser.getUsername(), ""));
    dataService.addUser(new User(user2, ""));

    var testUserAliases = Arrays.asList("alias1", "alias2", "alias3");
    for (var alias: testUserAliases) {
      dataService.addUrlAlias(new UrlAlias(alias, "url", testUser.getUsername()));
    }
    dataService.addUrlAlias(new UrlAlias("alias4", "url", user2));
    dataService.addUrlAlias(new UrlAlias("alias5", "url", user2));

    var testUserUrlAliases = dataService.getUserAliases(testUser.getUsername());
    assertThat(testUserUrlAliases.size()).isEqualTo(3);
    assertThat(testUserUrlAliases.stream().map(UrlAlias::getAlias)
      .collect(Collectors.toList()).containsAll(testUserAliases)).isTrue();
  }
}
