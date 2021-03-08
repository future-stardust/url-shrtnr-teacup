package edu.kpi.testcourse.dataservice;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class DataServiceImplTest {

  @Test
  void checkValueSaving() {
    DataServiceImpl dataService = new DataServiceImpl();

    var alias = new UrlAlias("testKey", "testValue", "testUser");
    dataService.addUrlAlias(alias);
    var result = dataService.getUrlAlias("testKey");

    assertThat(result.getUrl()).isEqualTo("testValue");
  }

  @Test
  void checkAliasNotFound() {
    DataServiceImpl dataService = new DataServiceImpl();

    var alias = new UrlAlias("testValue", "testKey", "testUser");
    dataService.addUrlAlias(alias);
    var result = dataService.getUrlAlias("wrongKey");

    assertThat(result).isEqualTo(null);
  }

}
