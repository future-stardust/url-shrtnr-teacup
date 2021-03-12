package edu.kpi.testcourse.urlservice;

import edu.kpi.testcourse.dataservice.DataService;
import edu.kpi.testcourse.dataservice.UrlAlias;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
class UrlServiceImpl implements UrlService {

  @Inject
  private final DataService dataService;

  public UrlServiceImpl(DataService dataService) {
    this.dataService = dataService;
  }

  public String shortenUrlToken(Integer urlId) {
    var alphabet = "abcdefghijklmnopqrstuvwxyz0123456789";
    var base = alphabet.length();

    var buffer = new StringBuilder();
    while (urlId > 0) {
      buffer.append(alphabet.charAt(urlId % base));
      urlId /= base;
    }
    return buffer.reverse().toString();
  }

  public boolean isUserAliasValid(String alias) {
    return ((alias != null)
      && (!alias.equals(""))
      && (alias.matches("^[a-zA-Z0-9_]*$")));
  }

  @Override
  public String getUrl(String alias) {
    var urlAlias = dataService.getUrlAlias(alias);
    return urlAlias == null ? null : urlAlias.getUrl();
  }

  @Override
  public boolean addUrl(String alias, String url, String user) throws IllegalArgumentException {
    return dataService.addUrlAlias(new UrlAlias(alias, url, user));
  }

  @Override
  public String addUrl(String url, String user) throws IOException {
    var id = dataService.getNextId();
    var shortUrlToken = shortenUrlToken(id);
    while (!dataService.addUrlAlias(new UrlAlias(shortUrlToken, url, user))) {
      id = dataService.getNextId();
      shortUrlToken = shortenUrlToken(id);
    }
    return shortUrlToken;
  }

  @Override
  public boolean deleteAlias(String alias, String user) {
    return dataService.deleteUrlAlias(alias, user);
  }

  @Override
  public List<AliasInfo> getUserAliases(String user) {
    var result = dataService.getUserAliases(user);
    return result.stream()
      .map(urlAlias -> new AliasInfo(urlAlias.getAlias(), urlAlias.getUrl()))
      .collect(Collectors.toList());
  }

}
