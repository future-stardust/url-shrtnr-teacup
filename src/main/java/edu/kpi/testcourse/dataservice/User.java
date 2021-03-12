package edu.kpi.testcourse.dataservice;

/**
 * Stores data about users:.
 *  <p>username - unique key of user;</p>
 *  <p>hash of the user password.</p>
 */
public class User {
  private String email;
  private String passwordHash;

  /**
   * Class constructor with all fields filled.
   *
   * @param username unique key
   * @param passwordHash hash of the password
   */
  public User(String username, String passwordHash) {
    this.email = username;
    this.passwordHash = passwordHash;
  }

  public String getEmail() {
    return email;
  }

  public String getPasswordHash() {
    return passwordHash;
  }
}
