package edu.kpi.testcourse.auth;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import org.junit.jupiter.api.Test;

public class PasswordHashTest {
  @Test
  void checkIfComparingIsCorrect() throws InvalidKeySpecException, NoSuchAlgorithmException {
    String secret = "testSecret";
    String secretHash = PasswordHash.createHash(secret);
    boolean validation = PasswordHash.validatePassword(secret, secretHash);

    assertThat(validation).isTrue();
  }
}
