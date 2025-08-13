package tr.bel.gaziantep.bysweb.core.controller;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 16.06.2025 11:39
 */
@Named(value = "initApp")
@ApplicationScoped
public class InitApp implements java.io.Serializable{
    @Serial
    private static final long serialVersionUID = 3814151748142455L;

    private static final String PATH_MAC = "/Users/omerfarukkurt/DisBirimler/config/application.properties";
    //Windows icin
    private static final String PATH_WINDOWS = "C:\\BYS\\config\\application.properties";

    @Getter
    @Setter
    private List<SyKullanici> syKullanicis;
    @Getter
    @Setter
    private String version;
    @Getter
    @Setter
    private String buildTime;
    @Getter
    @Setter
    private Properties applicationProp;
    @Getter
    @Setter
    private String path;



    @PostConstruct
    public void init() {
        Properties prop = loadManifestFile("version.properties");
        if (System.getProperty("os.name").equals("Mac OS X")) {
            path = PATH_MAC;
        } else {
            path = PATH_WINDOWS;
        }
        applicationProp = loadLocalFile(path);
        version = prop.getProperty("build.version");
        buildTime = prop.getProperty("build.timestamp");
        if (syKullanicis == null) syKullanicis = new ArrayList<>();
    }

    private Properties loadManifestFile(String fileName) {

        Properties prop = new Properties();
        try {
            InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(fileName);
            if (resourceAsStream != null) {
                prop.load(resourceAsStream);
            }
        } catch (IOException e) {
        }
        return prop;
    }

    private Properties loadLocalFile(String fileName){
        Properties prop = new Properties();
        try (InputStream input = new FileInputStream(fileName)) {
            prop.load(input);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return prop;
    }

    public String getProperty(String value) {
        return applicationProp.getProperty(value);
    }

    public void  setProperty(String key,String value){
        try (FileOutputStream outputStream = new FileOutputStream(path)) {
            applicationProp.setProperty(key,value);
            applicationProp.store(outputStream,null);

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

}
