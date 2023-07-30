package TheShadowMod.helpers;

import com.badlogic.gdx.Gdx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class SaveConfig {
    private static final String EXTENSION = "properties";
    private Properties properties;
    private File file;
    private String filePath;

    public SaveConfig() throws IOException {
        this(new Properties());
    }

    public SaveConfig(Properties defaultProperties) throws IOException {
        this.properties = new Properties(defaultProperties);
        this.filePath = Gdx.files.local("preferences" + File.separator + "TheShadowMod.properties").path();
        this.file = new File(this.filePath);
        this.file.createNewFile();
        this.load();
    }

    public void load() throws IOException {
        this.properties.load(new FileInputStream(this.file));
    }

    public void save() throws IOException {
        this.properties.store(new FileOutputStream(this.file), (String) null);
    }

    public boolean has(String key) {
        return this.properties.containsKey(key);
    }

    public void remove(String key) {
        this.properties.remove(key);
    }

    public void clear() {
        this.properties.clear();
    }

    public String getString(String key) {
        return this.properties.getProperty(key);
    }

    public void setString(String key, String value) {
        this.properties.setProperty(key, value);
    }

    public boolean getBool(String key) {
        return Boolean.parseBoolean(this.getString(key));
    }

    public void setBool(String key, boolean value) {
        this.setString(key, Boolean.toString(value));
    }

    public int getInt(String key) {
        return Integer.parseInt(this.getString(key));
    }

    public void setInt(String key, int value) {
        this.setString(key, Integer.toString(value));
    }

    public float getFloat(String key) {
        return Float.parseFloat(this.getString(key));
    }

    public void setFloat(String key, float value) {
        this.setString(key, Float.toString(value));
    }
}
