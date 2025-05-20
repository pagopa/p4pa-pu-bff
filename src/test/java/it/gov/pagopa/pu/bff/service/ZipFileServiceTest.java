package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.bff.dto.FileResourceDTO;
import it.gov.pagopa.pu.bff.exception.PdfProcessingException;
import it.gov.pagopa.pu.bff.exception.ZipFileException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ZipFileServiceTest {

  private final Path tempDir =  Path.of("build/tmp/test");

  private ZipFileService zipFileService;

  @BeforeEach
  void setUp() {
    zipFileService = new ZipFileService();
  }

  @Test
  void givenPathsWhenZipperThenReturnZipped() throws IOException {
    //given
    Path file1 = tempDir.resolve("file1.txt");
    Path file2 = tempDir.resolve("file2.txt");

    Files.writeString(file1, "Content of file1");
    Files.writeString(file2, "Content of file2");

    //when
    Path zipPath = tempDir.resolve("output.zip");
    File zipped = zipFileService.zipper(zipPath, List.of(file1, file2));

    //then
    assertTrue(file1.toFile().exists());
    assertTrue(file2.toFile().exists());
    assertTrue(zipped.exists());
    assertTrue(zipped.isFile());
  }

  @Test
  void givenPathsWhenZipAndCleanTmpFileThenReturnZippedAndCleanFiles() throws IOException {
    //given
    Path file1 = tempDir.resolve("file1.txt");
    Path file2 = tempDir.resolve("file2.txt");

    Files.writeString(file1, "Content of file1");
    Files.writeString(file2, "Content of file2");

    //when
    Path zipPath = tempDir.resolve("output.zip");
    File zipped = zipFileService.zipAndCleanTmpFile(zipPath, List.of(file1, file2));
    //then
    assertFalse(file1.toFile().exists());
    assertFalse(file2.toFile().exists());
    assertTrue(zipped.exists());
    assertTrue(zipped.isFile());
  }

  @Test
  void givenPathsWhenZipperThenThrowZipFileException() {
    //given
    Path file1 = tempDir.resolve("file1");
    Path file2 = tempDir.resolve("file2");

    //when
    Path zipPath = tempDir.resolve("output.zip");
    ZipFileException ex = assertThrows(ZipFileException.class, () -> zipFileService.zipper(zipPath, List.of(file1, file2)));
    assertTrue(ex.getMessage().contains("Error while zipping:"));
  }

  @Test
  void givenParametersWhenCreateZipFromResourcesThenReturnResource() throws IOException {
    //given
    Path file3 = tempDir.resolve("file3.pdf");
    Files.writeString(file3, "Dummy PDF content");
    Resource resource = new FileSystemResource(file3);
    FileResourceDTO fileResourceDTO = new FileResourceDTO(resource, "file3.pdf");
    Long organizationId = 1L;
    Long debtPositionId = 3L;

    //when
    Resource result = zipFileService.createZipFromResources(List.of(fileResourceDTO), tempDir, organizationId, debtPositionId);

    //then
    assertNotNull(result);
    assertEquals("1_3_PDF.zip", result.getFilename());
    assertTrue(result.exists());
    assertTrue(result.isFile());
    assertFalse(file3.toFile().exists());
  }

  @Test
  void givenParametersWhenCreateZipFromResourcesThenThrowPdfProcessingException() {
    //given
    Path file3 = tempDir.resolve("file3");
    Resource resource = new FileSystemResource(file3);
    FileResourceDTO fileResourceDTO = new FileResourceDTO(resource, "file3");
    Long organizationId = 1L;
    Long debtPositionId = 3L;

    //then
    PdfProcessingException ex = assertThrows(PdfProcessingException.class, () -> zipFileService.createZipFromResources(List.of(fileResourceDTO), tempDir, organizationId, debtPositionId));
    assertEquals("Failed to create or copy temporary PDF file", ex.getMessage());
  }

}
