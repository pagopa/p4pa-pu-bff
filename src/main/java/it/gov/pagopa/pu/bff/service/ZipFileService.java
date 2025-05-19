package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.bff.exception.ZipFileException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@Slf4j
public class ZipFileService {

  public File zipper(Path zipFilePath, List<Path> filesToZip) {
    try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFilePath.toFile()))) {
      for (Path file : filesToZip) {
        ZipEntry zipEntry = new ZipEntry(file.getFileName().toString());
        zos.putNextEntry(zipEntry);
        Files.copy(file, zos);
      }
      return zipFilePath.toFile();
    } catch (IOException e) {
      throw new ZipFileException("Error while zipping: " + zipFilePath);
    }
  }

  public File zipAndCleanTmpFile(Path zipFilePath, List<Path> filesToZip) {
    try {
        return zipper(zipFilePath, filesToZip);
    } finally {
      for (Path path : filesToZip) {
        try {
          Files.deleteIfExists(path);
        } catch (IOException e) {
          log.info("Failed to delete temp file: {} - {}", path, e.getMessage());
        }
      }
    }
  }

}
