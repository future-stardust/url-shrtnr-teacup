package edu.kpi.testcourse.dataservice;

import java.io.IOException;
import java.util.List;

/**
 * Data Service manages file storage actions (e.g storing user data).
 */
public interface DataService {

  /**
   * Adds user to data storage.
   *
   * @param user a user to be add
   * @return
   *  {@code true} if user was added successfully;
   *  {@code false} if user with the same username is already exists.
   * @see User
   */
  boolean addUser(User user) throws IllegalArgumentException;

  /**
   * Returns a user with given {@code username}.
   *
   * @param username username of user to be found
   * @return {@link User} with given username.
   *     Returns {@code null} if there isn't a user with given {@code username}
   * @see User
   */
  User getUser(String username);

  /**
   * Adds alias of url, created by certain user to data storage.
   *
   * @param urlAlias object to be added
   * @return
   *  {@code true} if object was added successfully;
   *  {@code false} if record with the same alias is already exists.
   * @throws IllegalArgumentException if there is no user with username
   *      specified in {@code urlAlias}
   * @see UrlAlias
   */
  boolean addUrlAlias(UrlAlias urlAlias) throws IllegalArgumentException;

  /**
   * Returns {@link UrlAlias} object that contains URL, corresponding to given alias and username
   * of user that created it.
   *
   * @param alias key of object to be found
   * @return {@link UrlAlias} object with given key.
   *     Returns {@code null} if there isn't a record with given {@code alias}.
   * @see UrlAlias
   */
  UrlAlias getUrlAlias(String alias);

  /**
   * Deletes {@link UrlAlias} object for given {@code alias}.
   *
   * @param alias key of object to be deleted
   * @return
   *  {@code true} if object was deleted successfully;
   *  {@code false} if there isn't a record with given {@code alias}.
   * @see UrlAlias
   */
  boolean deleteUrlAlias(String alias);

  /**
   * Returns all aliases, created by user with given username.
   *
   * @param user username of user to be searched for
   * @return list of aliases created by given user.
   * @see UrlAlias
   */
  List<UrlAlias> getUserAliases(String user);

  /**
   * Deletes all users and created url aliases.
   */
  void clear();

  /**
   * Returns unique integer number on each call.
   *
   * @return unique integer number
   * @throws IOException in case of errors with file writing/reading
   */
  int getNextId() throws IOException;
}
