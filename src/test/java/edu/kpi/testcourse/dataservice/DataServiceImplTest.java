package edu.kpi.testcourse.dataservice;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class DataServiceImplTest {

  protected DataService dataService = new DataServiceImpl();

  protected final User testUser = new User("testEmail", "testPassword");
  protected final UrlAlias testUrlAlias = new UrlAlias("testAlias", "testUrl", testUser.getEmail());

  @Test
  void testClear() {
    dataService.addUser(testUser);
    dataService.clear();
    var result = dataService.getUser(testUser.getEmail());

    assertThat(result).isEqualTo(null);
  }

  @Test
  void getId() throws IOException {
    var firstId = dataService.getNextId();
    assertThat(firstId).isEqualTo(1);
    var secondId = dataService.getNextId();
    assertThat(secondId).isEqualTo(2);
    dataService.clear();
    var newId = dataService.getNextId();
    assertThat(newId).isEqualTo(1);
  }

  @AfterEach
  void clear(){
    dataService.clear();
  }
}
