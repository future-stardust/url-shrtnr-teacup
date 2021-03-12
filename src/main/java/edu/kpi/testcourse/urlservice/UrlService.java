package edu.kpi.testcourse.urlservice;

import edu.kpi.testcourse.dataservice.UrlAlias;
import java.io.IOException;
import java.util.List;

/**
 * Url Service manages url actions (e.g. shorten urls).
 *
 */
public interface UrlService {

  /**
   * Returns a url with given {@code alias}.
   *
   * @param alias alias of the url
   * @return full url that was saved under this {@code alias}.
   *     Returns {@code null} if there isn't a record with given {@code alias}.
   */
  String getUrl(String alias);

  /**
   * Adds url with user's alias to data storage.
   *
   * @param alias alias of the url
   * @param url url to be added
   * @param user user adding the url
   * @return
   *  {@code true} if alias was added successfully;
   *  {@code false} if record with the same alias is already exists.
   * @throws IllegalArgumentException if there is no user with this username
   */
  boolean addUrl(String alias, String url, String user) throws IllegalArgumentException;


  /**
   * Adds url with user's alias to data storage.
   *
   * @param url url to be added
   * @param user user adding the url
   * @return {@code String} short url token returned
   * @throws IOException in case of errors with file writing/reading
   * @throws IllegalArgumentException if there is no user with this username
   */
  String addUrl(String url, String user) throws IOException, IllegalArgumentException;

  /**
   * Deletes {@code alias}.
   *
   * @param alias key of object to be deleted
   * @param user username of user that tries to delete this {@code alias}
   * @return
   *  {@code true} if object was deleted successfully;
   *  {@code false} if there isn't a record with given {@code alias}
   *     created by specified {@code user}.
   * @see UrlAlias
   */
  boolean deleteAlias(String alias, String user);

  /**
   * Returns all aliases, created by user with given username.
   *
   * @param user username of user to be searched for
   * @return list of aliases created by given user.
   * @see AliasInfo
   */
  List<AliasInfo> getUserAliases(String user);

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
  boolean isUserAliasValid(String alias);

}
