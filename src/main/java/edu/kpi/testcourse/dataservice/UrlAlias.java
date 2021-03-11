package edu.kpi.testcourse.dataservice;

/**
 * Stores data about url aliases:
 *  <p>id - integer id from which alias was generated ot {@code -1} if
 *  alias was provided by user;</p>
 *  <p>alias - unique key, shortened url;</p>
 *  <p>url - full url;</p>
 *  <p>user - email of the user, that created this alias</p>.
 */
public class UrlAlias {
  private int id;
  private String alias;
  private String url;
  private String user;

  /**
   * Constructor for objects with automatically generated aliases.
   *
   * @param id unique integer id from which alias was generated
   * @param alias unique key, short name for the {@code url}
   * @param url full url
   * @param user email of user that created this alias
   */
  public UrlAlias(int id, String alias, String url, String user) {
    this.id = id;
    this.alias = alias;
    this.url = url;
    this.user = user;
  }

  /**
   * Constructor for objects with aliases, provided by user.
   * Id in this objects is equal to {@code -1}
   *
   * @param alias unique key, short name for the {@code url}
   * @param url full url
   * @param user email of user that created this alias
   */
  public UrlAlias(String alias, String url, String user) {
    this.id = -1;
    this.alias = alias;
    this.url = url;
    this.user = user;
  }

  public int getId() {
    return id;
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
