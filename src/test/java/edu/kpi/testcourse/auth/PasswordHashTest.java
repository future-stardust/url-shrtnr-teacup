package edu.kpi.testcourse.auth;

import org.junit.jupiter.api.Test;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PasswordHashTest {
  @Test
  void checkIfComparingIsCorrect() throws InvalidKeySpecException, NoSuchAlgorithmException {
    String secret = "testSecret";
    String secretHash = PasswordHash.createHash(secret);
    boolean validation = PasswordHash.validatePassword(secret, secretHash);

    assertThat(validation).isTrue();
  }
}
