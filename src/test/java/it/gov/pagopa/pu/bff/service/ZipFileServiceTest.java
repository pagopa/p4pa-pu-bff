package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.bff.dto.FileResourceDTO;
import it.gov.pagopa.pu.bff.exception.ZipFileException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.junit.jupiter.api.Assertions.*;

class ZipFileServiceTest {

  private final Path tempDir =  Path.of("build/tmp/test");

  private ZipFileService zipFileService;

  @BeforeEach
  void setUp() {
    zipFileService = new ZipFileService();
  }

  @Test
  void givenListOfFileResourceDTOWhenZipperThenCreatesValidZip() throws IOException {
    Path file3 = tempDir.resolve("file3.pdf");
    String expectedContent = "Dummy PDF content";
    Files.writeString(file3, expectedContent);
    Resource resource = new FileSystemResource(file3);
    FileResourceDTO fileResourceDTO = new FileResourceDTO(resource, "file3.pdf");

    ByteArrayResource result = zipFileService.zipper(List.of(fileResourceDTO));

    assertNotNull(result);

    try (InputStream is = result.getInputStream();
         ZipInputStream zis = new ZipInputStream(is)) {

      ZipEntry entry = zis.getNextEntry();
      assertNotNull(entry);
      assertEquals("file3.pdf", entry.getName());

      String content = new String(zis.readAllBytes());
      assertEquals(expectedContent, content);
    }
  }

  @Test
  void givenWrongListOfFileResourceDTOWhenZipperThenThrowZipFileException() {
    Path file3 = tempDir.resolve("file3");
    Resource resource = new FileSystemResource(file3);
    FileResourceDTO fileResourceDTO = new FileResourceDTO(resource, "file3");

    List<FileResourceDTO> resourceDTOList = List.of(fileResourceDTO);
    ZipFileException ex = assertThrows(ZipFileException.class, () -> zipFileService.zipper(resourceDTOList));
    assertEquals("Error while zipping", ex.getMessage());
  }

  @Test
  void givenNullListOfFileResourceDTOWhenZipperThenReturnNull() {
    ByteArrayResource result = zipFileService.zipper(null);
    assertNull(result);
  }

  @Test
  void givenEmptyListOfFileResourceDTOWhenZipperThenReturnNull() {

    ByteArrayResource result = zipFileService.zipper(new ArrayList<>());
    assertNull(result);
  }

}
