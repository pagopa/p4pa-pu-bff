package it.gov.pagopa.pu.bff.enums;

import it.gov.pagopa.pu.bff.dto.generated.IngestionFlowFile;
import it.gov.pagopa.pu.processexecutions.dto.generated.IngestionFlowFile.StatusEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StatusEnumTest {
  @Test
  void testConversion(){
    for (StatusEnum value : StatusEnum.values()) {
      Assertions.assertDoesNotThrow(() -> IngestionFlowFile.StatusEnum.valueOf(value.name()));
    }

  }
}
