package it.gov.pagopa.pu.bff.enums;

import it.gov.pagopa.pu.bff.dto.generated.IngestionFlowFile;
import it.gov.pagopa.pu.bff.dto.generated.ExportFile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StatusEnumTest {
  @Test
  void testConversionIngestionFlowFile(){
    for (
      it.gov.pagopa.pu.processexecutions.dto.generated.IngestionFlowFile.StatusEnum value : it.gov.pagopa.pu.processexecutions.dto.generated.IngestionFlowFile.StatusEnum.values()) {
      Assertions.assertDoesNotThrow(() -> IngestionFlowFile.StatusEnum.valueOf(value.name()));
    }

  }

  @Test
  void testConversionExportFile(){
    for (it.gov.pagopa.pu.processexecutions.dto.generated.ExportFile.StatusEnum value : it.gov.pagopa.pu.processexecutions.dto.generated.ExportFile.StatusEnum.values()) {
      Assertions.assertDoesNotThrow(() -> ExportFile.StatusEnum.valueOf(value.name()));
    }

  }
}
