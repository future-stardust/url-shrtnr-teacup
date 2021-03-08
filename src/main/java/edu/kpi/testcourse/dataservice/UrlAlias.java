package edu.kpi.testcourse.dataservice;

/**
 * Stores data about url aliases:
 *  <p>alias - unique key, shortened url;</p>
 *  <p>url - full url;</p>
 *  <p>user - email of the user, that created this alias</p>.
 */
public class UrlAlias {
  private String alias;
  private String url;
  private String user;

  /**
   * Class constructor with all fields filled.
   *
   * @param alias unique key, short name for the {@code url}
   * @param url full url
   * @param user email of user that created this alias
   */
  public UrlAlias(String alias, String url, String user) {
    this.alias = alias;
    this.url = url;
    this.user = user;
  }

  public String getAlias() {
    return alias;
  }

  public String getUrl() {
    return url;
  }

  public String getUser() {
    return user;
  }
}
