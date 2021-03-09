package edu.kpi.testcourse.dataservice;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.stream.Collectors;

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

  @AfterEach
  void clear(){
    dataService.clear();
  }
}
