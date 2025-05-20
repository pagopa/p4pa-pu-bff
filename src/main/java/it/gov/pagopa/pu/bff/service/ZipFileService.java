package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.bff.dto.FileResourceDTO;
import it.gov.pagopa.pu.bff.exception.PdfProcessingException;
import it.gov.pagopa.pu.bff.exception.ZipFileException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
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

  public Resource createZipFromResources(List<FileResourceDTO> pdfResources, Path workingDirectory, String zipFileName) {
    if (pdfResources == null){
      return null;
    }

    List<Path> pdfPaths = pdfResources.stream()
      .filter(f -> f.getResource() != null)
      .map(f -> {
          try (InputStream is = f.getResource().getInputStream()) {
            Path customPath = workingDirectory.resolve(f.getFileName());
            Files.copy(is, customPath, StandardCopyOption.REPLACE_EXISTING);
            return customPath;
          } catch (IOException e) {
            throw new PdfProcessingException("Failed to create or copy temporary PDF file");
          }
      })
      .toList();

    if (pdfPaths.isEmpty()){
      return null;
    }

    Path filePath = workingDirectory.resolve(zipFileName);
    return new FileSystemResource(zipAndCleanTmpFile(filePath, pdfPaths));
  }



}
