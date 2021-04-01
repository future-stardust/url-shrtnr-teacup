package edu.kpi.testcourse.dataservice;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.quicktheories.QuickTheory.qt;
import static org.quicktheories.generators.SourceDSL.strings;
import java.util.Random;
import java.util.List;
import java.util.Arrays;

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



/*Uncontested*/
public static String Generator() {

  final String LOWER = "abcdefghijklmnopqrstuvwxyz";
  final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  final String DIGITS = "0123456789";
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



}
