package edu.kpi.testcourse.dataservice;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserTests extends DataServiceImplTest{
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
    var result = dataService.getUser(testUser.getUsername());

    assertThat(result.getPasswordHash()).isEqualTo(testUser.getPasswordHash());
  }

  @Test
  void getUserIfUserNotFound() {
    var result = dataService.getUser("wrongUsername");

    assertThat(result).isNull();
  }
}
