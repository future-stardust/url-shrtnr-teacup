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


  /**
   * Tests written by the Uncontested group
   */

  public static String Generator() {

    final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    final String DIGITS = "0123456789";
    final String PUNCTUATION = "!@#$%&*()_+-=[]|,./?><";
    final List<String> charCategories = Arrays.asList((LOWER + UPPER + DIGITS).split(""));
    int length = 6;

    // Variables.
    StringBuilder password = new StringBuilder(length);
    Random random = new Random(System.nanoTime());

    // Build the password.
    for (int i = 0; i < length; i++) {
      String charCategory = charCategories.get(random.nextInt(charCategories.size()));
      int position = random.nextInt(charCategory.length());
      password.append(charCategory.charAt(position));
    }
    return new String(password);
  }

  @Test
  void addUserTest() {

    User Pasha = new User(Generator(),Generator() );
    dataService.addUser(Pasha);
    System.out.println(Pasha.getPasswordHash() + "\n" + Pasha.getEmail());
    assertThat(dataService.getUser(Pasha.getEmail())).isNotNull();
  }
}
