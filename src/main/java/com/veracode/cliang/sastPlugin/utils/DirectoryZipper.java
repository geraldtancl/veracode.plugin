package com.veracode.cliang.sastPlugin.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class DirectoryZipper extends SimpleFileVisitor<Path> {

    private static final Class c = DirectoryZipper.class;

    private static ZipOutputStream zos;

    private File zipOutputDir;
    private Path sourceDir;

    public DirectoryZipper(File dirToZip, File zipOutputDir) {
        this.sourceDir = dirToZip.toPath();
        this.zipOutputDir = zipOutputDir;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) {

        try {
            Path targetFile = sourceDir.relativize(file);

            zos.putNextEntry(new ZipEntry(targetFile.toString()));

            byte[] bytes = Files.readAllBytes(file);
            zos.write(bytes, 0, bytes.length);
            zos.closeEntry();

        } catch (IOException ex) {
            PluginLogger.error(c, ex.getMessage(), ex);

        }

        return FileVisitResult.CONTINUE;
    }

    public void createZip() {
        try {

            zos = new ZipOutputStream(new FileOutputStream(zipOutputDir));

            Files.walkFileTree(sourceDir, this);

            zos.close();
        } catch (IOException ex) {
            System.err.println("I/O Error: " + ex);
        }

    }

}
