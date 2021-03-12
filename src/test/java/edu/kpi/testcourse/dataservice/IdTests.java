package edu.kpi.testcourse.dataservice;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class IdTests extends DataServiceImplTest {

  @Test
  void getSerialIds() throws IOException {
    var firstId = dataService.getNextId();
    assertThat(firstId).isEqualTo(1);
    var secondId = dataService.getNextId();
    assertThat(secondId).isEqualTo(2);
    dataService.clear();
    var newId = dataService.getNextId();
    assertThat(newId).isEqualTo(1);
  }

  @Test
  void addIdAfterDeletingAlias() throws IOException {
    var generatedAlias = "test";
    var testId = 42;
    var urlAlias = new UrlAlias(testId, generatedAlias, "url", testUser.getUsername());
    dataService.addUser(testUser);
    dataService.addUrlAlias(urlAlias);
    dataService.deleteUrlAlias(generatedAlias, testUser.getUsername());
    var nextId = dataService.getNextId();
    assertThat(nextId).isEqualTo(testId);
  }
}
