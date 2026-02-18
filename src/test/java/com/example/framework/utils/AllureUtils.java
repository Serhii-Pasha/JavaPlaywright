package com.example.framework.utils;

import io.qameta.allure.Allure;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;

/**
 * Helper methods for attaching files to Allure reports.
 */
public final class AllureUtils {

    private AllureUtils() {
    }

    /**
     * Attach file with automatically detected content type based on extension.
     */
    public static void attachFile(String name, String filePath) {
        if (filePath == null) {
            return;
        }
        attachFile(name, new File(filePath));
    }

    public static void attachFile(String name, Path path) {
        if (path == null) {
            return;
        }
        attachFile(name, path.toFile());
    }

    public static void attachFile(String name, File file) {
        if (file == null || !file.exists() || !file.isFile()) {
            return;
        }
        String extension = getExtension(file.getName());
        String type = detectContentType(extension);
        try (InputStream is = new FileInputStream(file)) {
            Allure.addAttachment(name, type, is, extension);
        } catch (FileNotFoundException e) {
            // ignore if file not found
        } catch (Exception e) {
            // swallow attachment errors to avoid breaking tests
        }
    }

    private static String getExtension(String fileName) {
        int dot = fileName.lastIndexOf('.');
        if (dot > 0 && dot < fileName.length() - 1) {
            return fileName.substring(dot + 1);
        }
        return "";
    }

    private static String detectContentType(String extension) {
        if (extension == null) {
            return "application/octet-stream";
        }
        String ext = extension.toLowerCase();
        switch (ext) {
            case "png":
                return "image/png";
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "webm":
                return "video/webm";
            case "mp4":
                return "video/mp4";
            case "zip":
                return "application/zip";
            case "json":
                return "application/json";
            default:
                return "application/octet-stream";
        }
    }
}


