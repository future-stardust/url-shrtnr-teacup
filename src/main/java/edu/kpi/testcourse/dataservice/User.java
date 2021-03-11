package edu.kpi.testcourse.dataservice;

/**
 * Stores data about users:.
 *  <p>username - unique key of user;</p>
 *  <p>hash of the user password.</p>
 */
public class User {
  private String username;
  private String passwordHash;

  /**
   * Class constructor with all fields filled.
   *
   * @param username unique key
   * @param passwordHash hash of the password
   */
  public User(String username, String passwordHash) {
    this.username = username;
    this.passwordHash = passwordHash;
  }

  public String getUsername() {
    return username;
  }

  public String getPasswordHash() {
    return passwordHash;
  }
}
