package de.haevn.utils;

import org.apache.commons.lang3.SystemUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public final class FileIO {
    private FileIO() {
    }


    public static String getFileName(String path) {
        return path.substring(path.lastIndexOf("\\") + 1);
    }

    public static String getFileNameWithoutExtension(String path) {
        return getFileName(path).substring(0, getFileName(path).lastIndexOf("."));
    }

    public static String getExtension(String path) {
        return getFileName(path).substring(getFileName(path).lastIndexOf(".") + 1);
    }

    public static String getDirectory(String path) {
        return path.substring(0, path.lastIndexOf("\\"));
    }

    public static URL getURI(String path) {
        return getURL(new File(path));
    }

    public static URL getURL(File file) {
        try {
            return file.toURL();
        } catch (MalformedURLException e) {
            return null;
        }
    }

    public static String getRootPath() {
        if (SystemUtils.IS_OS_WINDOWS) {
            return "./bin/data/";
        } else if (SystemUtils.IS_OS_MAC) {
            return "./bin/data/";
        }
        return "";
    }

    public static Tuple<Boolean, String> validate() {
        final String root = getRootPath();
        if (!Files.exists(Path.of(root))) return new Tuple<>(false, "Root path does not exist");

        if (!Files.exists(Path.of(root + "json"))) return new Tuple<>(false, "Json path does not exist");
        if (!Files.exists(Path.of(root + "json/dungeons_df_1.json")))
            return new Tuple<>(false, "Dungeons file does not exist");

        if (!Files.exists(Path.of(root + "production"))) return new Tuple<>(false, "Production path does not exist");
        if (!Files.exists(Path.of(root + "production/config.property")))
            return new Tuple<>(false, "Config file does not exist");
        if (!Files.exists(Path.of(root + "production/urls.property")))
            return new Tuple<>(false, "Urls file does not exist");
        return new Tuple<>(true, "All files exist");
    }

    public static void append(String filename, String data) {
        append(new File(filename), data);
    }

    public static void append(File file, String data) {
        try {
            createFile(file);
            Files.write(file.toPath(), data.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException ignored) {
        }
    }

    public static void createFile(File file) {
        if (file.exists()) {
            return;
        }
        try {
            createDirectory(file.getParentFile());
            file.createNewFile();
        } catch (IOException ignored) {
        }
    }

    public static void createDirectory(File directory) {
        if (directory.exists()) {
            return;
        }
        directory.mkdirs();
    }

    public static String readFile(String path) {
        try {
            String target = getRootPath() + path;
            var lines = Files.readAllLines(Paths.get(target));
            return String.join("\n", lines);
        } catch (IOException e) {
            return "";
        }
    }

    public static void store(String path, String data) {
        try {
            String target = getRootPath() + path;
            createFile(new File(target));
            Files.write(Paths.get(target), data.getBytes());
        } catch (IOException ignored) {
        }
    }


    public static void openDefaultApplication(File file){
        try {
            Desktop.getDesktop().open(file);
        } catch (IOException ignored) {
        }
    }
}
