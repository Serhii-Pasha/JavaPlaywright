package com.example.framework.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.stream.Stream;

public final class FileUtils {

    private FileUtils() {
    }
    public static void cleanDirectory(String dir) {
        Path dirPath = Paths.get(dir);
        try {
            if (Files.exists(dirPath)) {
               
                try (Stream<Path> walk = Files.walk(dirPath)) {
                    walk.sorted(Comparator.reverseOrder())
                        .filter(p -> !p.equals(dirPath))
                        .forEach(path -> {
                            try {
                                Files.deleteIfExists(path);
                            } catch (IOException ignored) {
                                
                            }
                        });
                }
            }
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }
        } catch (IOException ignored) {
           
        }
    }
}

