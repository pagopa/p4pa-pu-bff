package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.bff.dto.FileResourceDTO;
import it.gov.pagopa.pu.bff.exception.ZipFileException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@Slf4j
public class ZipFileService {

  public ByteArrayResource zipper(List<FileResourceDTO> filesToZip) {

    if (filesToZip == null || filesToZip.isEmpty()){
      return null;
    }

    try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
         ZipOutputStream zos = new ZipOutputStream(baos)) {

      for (FileResourceDTO dto : filesToZip) {

        try (InputStream is = dto.getResource().getInputStream()) {
          ZipEntry entry = new ZipEntry(dto.getFileName());
          zos.putNextEntry(entry);
          is.transferTo(zos);
          zos.closeEntry();
        }
      }

      zos.finish();
      return new ByteArrayResource(baos.toByteArray());

    } catch (IOException e) {
      throw new ZipFileException("Error while zipping");
    }
  }

}
