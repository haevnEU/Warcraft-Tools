package de.haevn;

import de.haevn.utils.FileIO;
import lombok.SneakyThrows;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.URL;
import java.util.Arrays;

public class Launcher {
    public static void main(String[] args) {
        boolean scan = Arrays.stream(args).anyMatch(arg -> arg.equalsIgnoreCase("--repair"));
        boolean update = Arrays.stream(args).anyMatch(arg -> arg.equalsIgnoreCase("--update"));
        if (scan) {
            update();
            Main.main(args);
        } else if (update) {
            update();
        } else if(!new File(FileIO.DATA_ROOT, "firstrun").exists()){
            update();
            Main.main(args);
        }else {
            Main.main(args);
        }
    }


    @SneakyThrows
    private static void update() {
        // When the data directory is missing => create a new
        FileIO.createDirectoryIfNotExists(FileIO.DATA_ROOT);

        // Download the latest data from github
        final String url = "https://github.com/nimile/WarcraftToolsResources/archive/refs/heads/master.zip";
        File tempFile = File.createTempFile("wtData" + System.currentTimeMillis(), ".zip");
        FileUtils.copyURLToFile(
                new URL(url),
                tempFile,
                1000,
                1000);

        // Extract the downloaded zip file
        try (ZipFile zipFile = new ZipFile(tempFile)) {
            if (zipFile.isEncrypted()) {
                return;
            }
            zipFile.extractAll(FileIO.DATA_ROOT.getAbsolutePath());

            // Move the extracted files to the correct location and delete the temp directory
            final File dlRoot = new File(FileIO.DATA_ROOT, "WarcraftToolsResources-master");
            if(!new File(FileIO.DATA_ROOT, "json").exists())   FileUtils.moveDirectoryToDirectory(new File(dlRoot, "json"), FileIO.DATA_ROOT, true);
            if(!new File(FileIO.DATA_ROOT, "config").exists()) FileUtils.moveDirectoryToDirectory(new File(dlRoot, "config"), FileIO.DATA_ROOT, true);
            if(!new File(FileIO.DATA_ROOT, "styles").exists()) FileUtils.moveDirectoryToDirectory(new File(dlRoot, "styles"), FileIO.DATA_ROOT, true);

            FileUtils.deleteDirectory(new File(FileIO.DATA_ROOT, "WarcraftToolsResources-master"));
        } catch (ZipException e) {
            e.printStackTrace();
        }
    }
}
