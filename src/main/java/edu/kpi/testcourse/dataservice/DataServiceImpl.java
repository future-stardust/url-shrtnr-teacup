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
  private final String jsonFileExtension = ".json";
  private final String userFileExtension = ".usr";
  private final String rootPath = "./.data";
  private final File idsFile = new File(rootPath + "/ids" + jsonFileExtension);

  public DataServiceImpl() {
    createDirectory(rootPath);
  }

  @Override
  public boolean addUser(User user) {
    var userDir = getUserDirectory(user.getEmail());
    if (userDir.exists()) {
      return false;
    }
    userDir.mkdir();
    var userFile = getUserFile(user.getEmail());
    return saveToNewFile(user, userFile);
  }

  @Override
  public User getUser(String username) {
    var file = getUserFile(username);
    return readFromJsonFile(file, User.class);
  }

  @Override
  public boolean addUrlAlias(UrlAlias urlAlias) {
    var userDir = getUserDirectory(urlAlias.getUser());
    if (!userDir.exists()) {
      throw new IllegalArgumentException(
        String.format("Cannot add alias, user '%s' was not created", urlAlias.getUser()));
    }

    // if file with the same alias exists in the directory of any user
    if (getAliasFile(urlAlias.getAlias()) != null) {
      return false;
    }
    
    var file = getAliasFile(urlAlias.getAlias(), urlAlias.getUser());
    return saveToNewFile(urlAlias, file);
  }

  @Override
  public UrlAlias getUrlAlias(String alias) {
    var file = getAliasFile(alias);
    if (file == null) {
      return null;
    }
    return readFromJsonFile(file, UrlAlias.class);
  }

  @Override
  public boolean deleteUrlAlias(String alias, String user) {
    var file = getAliasFile(alias, user);
    if (!file.exists()) {
      return false;
    }
    var urlAlias = readFromJsonFile(file, UrlAlias.class);
    if (urlAlias.aliasIsGenerated()) {
      try {
        var idHelper = getIdHelper();
        idHelper.addAvailable(urlAlias.getId());
        writeToFile(idHelper, idsFile);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    file.delete();
    return true;
  }

  @Override
  public List<UrlAlias> getUserAliases(String user) {
    var userDir = getUserDirectory(user);
    var userFiles = userDir.listFiles(
        (dir, name) -> name.toLowerCase().endsWith(jsonFileExtension));
    var userUrls = new ArrayList<UrlAlias>();
    if (userFiles != null) {
      for (File file : userFiles) {
        var urlAlias = readFromJsonFile(file, UrlAlias.class);
        userUrls.add(urlAlias);
      }
    }

    return userUrls;
  }

  @Override
  public void clear() {
    var rootDir = new File(rootPath);
    var userDirs = rootDir.listFiles();
    if (userDirs != null) {
      for (var userDir : userDirs) {
        clearDirectory(userDir);
        userDir.delete();
      }
    }
    idsFile.delete();
  }

  @Override
  public int getNextId() throws IOException {
    var idHelper = getIdHelper();
    var result  = idHelper.getNextIdAndUpdate();
    writeToFile(idHelper, idsFile);
    return result;
  }

  private void clearDirectory(File dir) {
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

  private boolean saveToNewFile(Object src, File dest) {
    try {
      if (dest.createNewFile()) {
        writeToFile(src, dest);
        return true;
      } else {
        return false;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return false;
  }

  private void writeToFile(Object src, File dest) throws IOException {
    var g = new Gson();
    var writer = new FileWriter(dest);
    writer.write(g.toJson(src));
    writer.flush();
    writer.close();
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

  private IdHelper getIdHelper() throws IOException {
    if (idsFile.createNewFile()) {
      var idHelper = new IdHelper(new ArrayList<>(), 1);
      writeToFile(idHelper, idsFile);
      return idHelper;
    } else {
      return readFromJsonFile(idsFile, IdHelper.class);
    }
  }

  private File getUserDirectory(String username) {
    return new File(String.join("/", rootPath, username));
  }

  private File getUserFile(String username) {
    return new File(String.join("/", rootPath, username, username + userFileExtension));
  }

  private File getAliasFile(String alias, String user) {
    return new File(String.join("/", rootPath, user, alias + jsonFileExtension));
  }

  private File getAliasFile(String alias) {
    var rootDir = new File(rootPath);
    var users = rootDir.list();
    if (users != null) {
      for (var user : users) {
        var aliasFile = getAliasFile(alias, user);
        if (aliasFile.exists()) {
          return aliasFile;
        }
      }
    }
    return null;
  }

  private class IdHelper {
    private List<Integer> availableIds;
    private Integer nextId;

    public IdHelper(List<Integer> availableIds, Integer nextId) {
      this.availableIds = availableIds;
      this.nextId = nextId;
    }

    public void addAvailable(Integer id) {
      availableIds.add(id);
    }

    public Integer getNextIdAndUpdate() {
      if (availableIds.isEmpty()) {
        return nextId++;
      } else {
        return availableIds.remove(0);
      }
    }
  }
}
