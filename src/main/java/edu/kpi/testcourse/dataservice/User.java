package edu.kpi.testcourse.dataservice;

/**
 * Stores data about users:.
 *  <p>email - unique key of user;</p>
 *  <p>hash of the user password.</p>
 */
public class User {
  private String email;
  private String passwordHash;

  /**
   * Class constructor with all fields filled.
   *
   * @param email unique key, email of user
   * @param passwordHash hash of the password
   */
  public User(String email, String passwordHash) {
    this.email = email;
    this.passwordHash = passwordHash;
  }

  public String getEmail() {
    return email;
  }

  public String getPasswordHash() {
    return passwordHash;
  }
}
