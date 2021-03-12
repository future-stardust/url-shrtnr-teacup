package edu.kpi.testcourse.urlservice;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import javax.inject.Inject;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@MicronautTest
public class ShortenUrlTests {
  @Inject
  UrlService urlService;

  @Test
  @DisplayName("Should return encoded url via id")
  void testShorten() {
    var testId = 5;
    var testId2 = 5;
    var testId3 = 2;

    var result1 = urlService.shortenUrlToken(testId);
    var result2 = urlService.shortenUrlToken(testId2);
    var result3 = urlService.shortenUrlToken(testId3);
    assertEquals(result1, result2, "Should be equal");
    assertNotEquals(result1, result3, "Should not be equals");
  }

  @Test
  @DisplayName("Should return true/false for user alias")
  void testCheckUserAlias() {
    var userAlias1 = "FluffY3";
    var userAlias2 = "G_der3";
    var userAlias3 = "Good_3!";

    assertTrue(urlService.isUserAliasValid(userAlias1));
    assertTrue(urlService.isUserAliasValid(userAlias2));
    assertFalse(urlService.isUserAliasValid(userAlias3), "Should return false");
  }

}
