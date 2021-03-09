package edu.kpi.testcourse.dataservice;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Singleton;

@Singleton
class DataServiceImpl implements DataService {
  private final String rootPath = "./.data";
  private final String usersDirPath = rootPath + "/users";
  private final String urlsDirPath = rootPath + "/urls";

  public DataServiceImpl() {
    createDirectory(rootPath);
    createDirectory(usersDirPath);
    createDirectory(urlsDirPath);
  }

  @Override
  public boolean addUser(User user) {
    var file = getUserFile(user.getEmail());
    return saveAsJsonFile(user, file);
  }

  @Override
  public User getUser(String email) {
    var file = getUserFile(email);
    return readFromJsonFile(file, User.class);
  }

  @Override
  public boolean addUrlAlias(UrlAlias urlAlias) {
    var file = getAliasFile(urlAlias.getAlias());
    return saveAsJsonFile(urlAlias, file);
  }

  @Override
  public UrlAlias getUrlAlias(String alias) {
    var file = getAliasFile(alias);
    return readFromJsonFile(file, UrlAlias.class);
  }

  @Override
  public boolean deleteUrlAlias(String alias) {
    var file = getAliasFile(alias);
    if (file.exists()) {
      file.delete();
      return true;
    } else {
      return false;
    }
  }

  @Override
  public List<UrlAlias> getUserAliases(String user) {
    var urlDir = new File(urlsDirPath);
    File[] allUrls = urlDir.listFiles();
    var userUrls = new ArrayList<UrlAlias>();
    if (allUrls != null) {
      for (File file : allUrls) {
        var urlAlias = readFromJsonFile(file, UrlAlias.class);
        if (urlAlias.getUser().equals(user)) {
          userUrls.add(urlAlias);
        }
      }
    }

    return userUrls;
  }

  @Override
  public void clear() {
    clearDirectory(usersDirPath);
    clearDirectory(urlsDirPath);
  }

  private void clearDirectory(String path) {
    var dir = new File(path);
    File[] allContents = dir.listFiles();
    if (allContents != null) {
      for (File file : allContents) {
        file.delete();
      }
    }
  }

  private void createDirectory(String path) {
    var dir = new File(path);
    if (!dir.exists()) {
      dir.mkdir();
    }
  }

  private boolean saveAsJsonFile(Object src, File dest) {
    var g = new Gson();
    try {
      if (dest.createNewFile()) {
        var writer = new FileWriter(dest);
        writer.write(g.toJson(src));
        writer.flush();
        writer.close();
        return true;
      } else {
        return false;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return false;
  }

  public <T> T readFromJsonFile(File src, Class<T> classOfT) {
    if (src.exists()) {
      try {
        var str = new String(Files.readAllBytes(src.toPath()));
        var g = new Gson();
        return g.fromJson(str, classOfT);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return null;
  }

  private File getUserFile(String email) {
    return new File(String.format("%s/%s.json", usersDirPath, email));
  }

  private File getAliasFile(String alias) {
    return new File(String.format("%s/%s.json", urlsDirPath, alias));
  }
}
