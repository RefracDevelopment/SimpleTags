package me.refracdevelopment.simpletags.utilities;

import me.refracdevelopment.simpletags.SimpleTags;
import net.byteflux.libby.BukkitLibraryManager;
import net.byteflux.libby.Library;

public class DownloadUtil {

    public static void downloadAndEnable() {
        BukkitLibraryManager libraryManager = new BukkitLibraryManager(SimpleTags.getInstance());
        Library lib = Library.builder()
                .groupId("org{}mariadb{}jdbc") // "{}" is replaced with ".", useful to avoid unwanted changes made by maven-shade-plugin
                .artifactId("mariadb-java-client")
                .version("3.1.4")
                .build();

        libraryManager.addMavenCentral();
        libraryManager.loadLibrary(lib);
    }
}