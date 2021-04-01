package edu.kpi.testcourse.dataservice;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class DataServiceImplTest {

  protected DataService dataService = new DataServiceImpl();

  protected final User testUser = new User("testUsername", "testPasswordHash");
  protected final UrlAlias testUrlAlias = new UrlAlias("testAlias", "testUrl", testUser.getEmail());

  @Test
  void testClear() {
    dataService.addUser(testUser);
    dataService.clear();
    var result = dataService.getUser(testUser.getEmail());

    assertThat(result).isEqualTo(null);
  }

  /**
   * Tests written by the Uncontested group
   */

  @Test
  void userDataSave() {
    dataService.addUser(testUser);
    var dataUsername = dataService.getUser(testUser.getEmail());
    var dataPassword = dataService.getUser(testUser.getPasswordHash());
    //System.out.println(testUser.getPasswordHash());
    assertThat(dataUsername).isNotNull();
    assertThat(dataPassword).isNull();
  }

  @Test
  void aliasDataSave() {
    dataService.addUser(testUser);
    dataService.addUrlAlias(testUrlAlias);
    var dataUrlAlias = dataService.getUrlAlias(testUrlAlias.getAlias());
    var dataUrl = dataService.getUrlAlias(testUrlAlias.getUrl());

    //System.out.println(dataUrl);

    assertThat(dataUrlAlias).isNotNull();
  }


  @AfterEach
  void clear(){
    dataService.clear();
  }
}
