package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.bff.exception.ZipFileException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    assertEquals("Error while zipping: build\\tmp\\test\\output.zip" , ex.getMessage());
  }
}
