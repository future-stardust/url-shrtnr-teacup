package edu.kpi.testcourse.dataservice;

import edu.kpi.testcourse.auth.PasswordHash;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.netty.util.internal.StringUtil.length;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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
    var result = dataService.getUser(testUser.getEmail());

    assertThat(result.getPasswordHash()).isEqualTo(testUser.getPasswordHash());
  }

  @Test
  void getUserIfUserNotFound() {
    var result = dataService.getUser("wrongUsername");

    assertThat(result).isNull();
  }



}
