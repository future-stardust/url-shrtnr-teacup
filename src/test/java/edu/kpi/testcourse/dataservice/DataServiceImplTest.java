package edu.kpi.testcourse.dataservice;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class DataServiceImplTest {

  DataService dataService = new DataServiceImpl();

  private final User testUser = new User("testEmail", "testPassword");
  private final UrlAlias testUrlAlias = new UrlAlias("testAlias", "testUrl", testUser.getEmail());

  @Test
  void testClear() {
    dataService.addUser(testUser);
    dataService.clear();
    var result = dataService.getUser(testUser.getEmail());

    assertThat(result).isEqualTo(null);
  }
  
  @Test
  void addUser() {
    var result = dataService.addUser(testUser);
    
    assertThat(result).isTrue();
  }

  @Test
  void addUserIfUserExists(){
    var firstResult = dataService.addUser(testUser);
    var secondResult = dataService.addUser(testUser);

    assertThat(secondResult).isFalse();
  }

  @Test
  void getUser() {
    dataService.addUser(testUser);
    var result = dataService.getUser(testUser.getEmail());

    assertThat(result.getPasswordHash()).isEqualTo(testUser.getPasswordHash());
  }

  @Test
  void getUserIfUserNotFound() {
    var result = dataService.getUser("wrongEmail");

    assertThat(result).isNull();
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
    var result = dataService.deleteUrlAlias(testUrlAlias.getAlias());

    assertThat(result).isTrue();
  }

  @Test
  void deleteAliasIfAliasNotFound() {
    var result = dataService.deleteUrlAlias(testUrlAlias.getAlias());

    assertThat(result).isFalse();
  }

  @Test
  void deleteAlias() {
    dataService.addUrlAlias(testUrlAlias);
    dataService.deleteUrlAlias(testUrlAlias.getAlias());
    var result = dataService.getUrlAlias(testUrlAlias.getAlias());
    
    assertThat(result).isNull();
  }
  
  @Test
  void getUserAliases() {
    var user1 = "user1";
    var user2 = "user2";
    var user1Aliases = Arrays.asList("alias1", "alias2", "alias3");
    for (var alias: user1Aliases) {
      dataService.addUrlAlias(new UrlAlias(alias, "url", user1));
    }
    dataService.addUrlAlias(new UrlAlias("alias4", "url", user2));
    dataService.addUrlAlias(new UrlAlias("alias5", "url", user2));

    var user1UrlAliases = dataService.getUserAliases(user1);
    assertThat(user1UrlAliases.size()).isEqualTo(3);
    assertThat(user1UrlAliases.stream().map(UrlAlias::getAlias)
      .collect(Collectors.toList()).containsAll(user1Aliases)).isTrue();
  }

  @AfterEach
  void clear(){
    dataService.clear();
  }
}
