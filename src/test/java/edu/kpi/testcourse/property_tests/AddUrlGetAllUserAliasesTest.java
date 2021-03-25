package edu.kpi.testcourse.property_tests;

import org.quicktheories.core.Gen;
import static org.quicktheories.QuickTheory.qt;
import static org.quicktheories.generators.SourceDSL.strings;
import edu.kpi.testcourse.dataservice.DataService;
import edu.kpi.testcourse.dataservice.UrlAlias;
import edu.kpi.testcourse.dataservice.User;
import edu.kpi.testcourse.urlservice.AliasInfo;
import edu.kpi.testcourse.urlservice.UrlService;
import io.micronaut.context.BeanContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class AddUrlGetAllUserAliasesTest {
  static DataService dataService = BeanContext.run().getBean(DataService.class);
  static UrlService urlService = BeanContext.run().getBean(UrlService.class);

  static Gen<String> aliases = strings().basicLatinAlphabet().ofLengthBetween(1, 10);
  static Gen<String> urls = strings().basicLatinAlphabet().ofLengthBetween(1, 10);

  @BeforeAll
  static void beforeAll() {
    dataService.clear();
    dataService.addUser(new User("vasya", "pupkin"));
  }

  @Test
  void testUrlServiceAddGetAll() {
    qt()
      .forAll(
        aliases,
        urls
      ).check((alias, url) -> {
        url = url.concat(".com");
        urlService.addUrl(alias, url, "vasya");
        var allUserAliases = urlService.getUserAliases("vasya");
        for (AliasInfo urlAlias : allUserAliases) {
          if (!urlAlias.alias().equals(alias) || !urlAlias.url().equals(url)) {
            return false;
          }
        }
        urlService.deleteAlias(alias, "vasya");
        return true;
    });
  }

  @Test
  void testDataServiceAddGetAll() {
    qt()
      .forAll(
        aliases,
        urls
      ).check((alias, url) -> {
        url = url.concat(".com");
        dataService.addUrlAlias(new UrlAlias(alias, url, "vasya"));
        for (UrlAlias urlAlias : dataService.getUserAliases("vasya")) {
          if (!urlAlias.getAlias().equals(alias) || !urlAlias.getUrl().equals(url)) {
            return false;
          }
        }
        urlService.deleteAlias(alias, "vasya");
        return true;
      });
  }
}
