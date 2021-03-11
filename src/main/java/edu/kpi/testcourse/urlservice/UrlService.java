package edu.kpi.testcourse.urlservice;

import java.io.IOException;

/**
 * Url Service manages url actions (e.g. shorten urls).
 *
 */
public interface UrlService {

  /**
   * Returns a url with given {@code alias}.
   *
   * @param alias alias of the url
   * @return {@code UrlAlias} if alias passed the check and
   */
  String getUrl(String alias);

  /**
   * Adds url with user's alias to data storage.
   * If alias has passed the check, the url is added with alias.
   *
   * @param alias alias of the url
   * @param url url to be added
   * @param user user adding the url
   */
  void addUrl(String alias, String url, String user);


  /**
   * Adds url with user's alias to data storage.
   *
   * @param url url to be added
   * @param user user adding the url
   * @return {@code String} short url token returned
   * @throws IOException if getNextId() call failed.
   */
  String addUrl(String url, String user) throws IOException;

  /**
   * Shortens a given url via it's ID.
   *
   * @param urlId ID of the needed to be shortened url
   * @return {@code String} shortened url token returned
   */
  String shortenUrlToken(Integer urlId);

  /**
   * Checks if the alias entered by the user doesn't contain not allowed chars.
   *
   * @param alias alias to be checked
   * @return
   *  {@code true} if alias has passed the check;
   *  {@code false} if alias contains at least 1 forbidden char.
   */
  boolean checkUserAlias(String alias);

}
