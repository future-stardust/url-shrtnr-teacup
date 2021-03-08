package edu.kpi.testcourse.urlservice;

import edu.kpi.testcourse.dataservice.DataService;
import edu.kpi.testcourse.dataservice.UrlAlias;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class UrlServiceImpl implements UrlService {

  @Inject
  private final DataService dataService;

  public UrlServiceImpl(DataService dataService) {
    this.dataService = dataService;
  }

  @Override
  public String getUrl(String alias) {
    var urlAlias = dataService.getUrlAlias(alias);
    return urlAlias == null ? null : urlAlias.getUrl();
  }

  @Override
  public void addUrl(String alias, String url) {
    dataService.addUrlAlias(new UrlAlias(alias, url, "John Doe"));
  }
}
